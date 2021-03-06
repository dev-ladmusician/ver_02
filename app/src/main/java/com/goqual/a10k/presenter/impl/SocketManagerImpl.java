package com.goqual.a10k.presenter.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.goqual.a10k.R;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.SocketData;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.SocketManager;
import com.goqual.a10k.util.HttpResponseCode;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.SocketIoManager;
import com.goqual.a10k.util.SocketProtocols;
import com.goqual.a10k.util.interfaces.ISocketIoConnectionListener;

/**
 * Created by HanWool on 2017. 2. 21..
 */

public class SocketManagerImpl implements SocketManager, ISocketIoConnectionListener {
    public static final String TAG = SocketManager.class.getSimpleName();
    private View mView = null;
    private Context mContext = null;
    private SocketIoManager mSocketManager;

    public SocketManagerImpl(View mView, Context ctx) {
        LogUtil.e(TAG, "SocketManagerImpl");
        this.mView = mView;
        mContext = ctx;
        mSocketManager = SocketIoManager.getInstance(ctx, this);
    }

    public void tryReconnect() {
        if(mSocketManager == null) {
            mSocketManager = SocketIoManager.getInstance(mContext, this);
        }
        mSocketManager.disconnect();
        mSocketManager.connect();
    }

    @Override
    public void operationOnOff(Switch item, int btnNumber) {
        LogUtil.d(TAG, "ITEM : " + item.toString() + "\n BTN NUMBER : " + btnNumber);
        if (btnNumber > 0) {
            Gson gson = new Gson();
            String btnState;
            switch (btnNumber) {
                case 1:
                    btnState = !item.isBtn1() ? mContext.getString(R.string.btn_str_on) : mContext.getString(R.string.btn_str_off);
                    break;
                case 2:
                    btnState = !item.isBtn2() ? mContext.getString(R.string.btn_str_on) : mContext.getString(R.string.btn_str_off);
                    break;
                case 3:
                    btnState = !item.isBtn3() ? mContext.getString(R.string.btn_str_on) : mContext.getString(R.string.btn_str_off);
                    break;
                default:
                    btnState = mContext.getString(R.string.btn_str_on);
            }
            SocketData data = new SocketData(item.getMacaddr(),
                    PreferenceHelper.getInstance(mContext).getStringValue(
                            mContext.getString(R.string.arg_user_token), ""
                    ), btnClickHandler(item, btnNumber),
                    btnState, 0);
            mSocketManager.emit(SocketProtocols.SOCKET_PT_REQ_OPERATION, gson.toJson(data));
        }
    }

    @Override
    public void onConnected() {
        LogUtil.e(TAG, "SOCKET onConnected");
        mView.onConnected();
        mSocketManager.joinSwitchRoom();
    }

    @Override
    public void onConnectionTimeOut() {
        LogUtil.e(TAG, "SOCKET onConnectionTimeOut");
    }

    @Override
    public void onDisconnect() {
        LogUtil.e(TAG, "SOCKET onDisconnect");
        if(mSocketManager != null) {
            mSocketManager.leaveAllSwitchRooms();
        }
    }

    @Override
    public void onError() {
        LogUtil.e(TAG, "SOCKET onError");
        mView.onConnectionError();
    }

    @Override
    public void onReceiveMessage(String type, SocketData data) {
        LogUtil.d(TAG, "SOCKET onReceiveMessage");
        switch (data.getCode()) {
            case HttpResponseCode.OK:
                switch (type) {
                    case SocketProtocols.SOCKET_PT_RES_OPERATION:
                        updateSwitchState(data);
                }
                break;
            case HttpResponseCode.ERROR_BAD_REQUEST:
            case HttpResponseCode.ERROR_UNAUTHORIZED:
            case HttpResponseCode.ERROR_NOT_FOUND:
            case HttpResponseCode.ERROR_INTERNAL_SERVER:
            case HttpResponseCode.ERROR_BAD_GATEWAY:
                mView.onServerError(data.getCode());
                break;
        }
    }

    private void updateSwitchState(SocketData data) {
        Switch item = SwitchManager.getInstance().getSwitchByMacAddr(data.getMacaddr());

        LogUtil.e(TAG, "received btncount :: " + item.getBtnCount());
        LogUtil.e(TAG, "received btn :: " + btnClickHandler(item, data.getBtn()));

        if(item != null) {
            switch (btnClickHandler(item, data.getBtn())) {
                case 1:
                    item.setBtn1(data.getOperation().equals("1"));
                    break;
                case 2:
                    item.setBtn2(data.getOperation().equals("1"));
                    break;
                case 3:
                    item.setBtn3(data.getOperation().equals("1"));
                    break;
            }
        }
        mView.refreshViews();
    }

    /**
     * 1,2,3구일때 btn 번호 바뀌는 것 처리하는 로직
     * @param item
     * @param clickedBtn
     * @return
     */
    private int btnClickHandler(Switch item, int clickedBtn) {
        int opBtn = -1;

        switch (item.getBtnCount()) {
            case 1:
                if(clickedBtn == 1) {
                    // 1구일 때 1구 스위치를 req 할 때
                    opBtn = 2;
                } else if (clickedBtn == 2) {

                    opBtn = 1;
                }
                break;
            case 2:
                if (clickedBtn == 1) {
                    opBtn = 1;
                } else if (clickedBtn == 2) {
                    opBtn = 3;
                } else if (clickedBtn == 3) {
                    opBtn = 2;
                }
                break;
            case 3:
                opBtn = clickedBtn;
                break;
        }

        return opBtn;
    }

    @Override
    public void onReconnect() {
        LogUtil.e(TAG, "SOCKET onReconnect");
    }

    @Override
    public void onReconnectionFailed() {
        LogUtil.e(TAG, "SOCKET onReconnectionFailed");
    }

    @Override
    public void refreshConnectedRoom() {
        mSocketManager.joinSwitchRoom();
    }

    @Override
    public void destroySocketConnection() {
        if(mSocketManager != null) {
            mSocketManager.destroy();
            mSocketManager = null;
        }
    }

    @Override
    public boolean isConnected() {
        if(mSocketManager != null) {
            return mSocketManager.isConnected();
        }
        else {
            return false;
        }
    }
}
