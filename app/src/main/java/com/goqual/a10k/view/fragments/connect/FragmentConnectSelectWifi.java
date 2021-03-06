package com.goqual.a10k.view.fragments.connect;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentConnectSelectWifiBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.presenter.WifiPresenter;
import com.goqual.a10k.presenter.impl.WifiPresenterImpl;
import com.goqual.a10k.util.KeyPadUtil;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivitySwitchConnection;
import com.goqual.a10k.view.adapters.AdapterWifiScanResult;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IActivityFragmentPageChangeListener;
import com.goqual.a10k.view.interfaces.IAuthInteraction;

/**
 * Created by hanwool on 2017. 2. 23..
 */

public class FragmentConnectSelectWifi extends BaseFragment<FragmentConnectSelectWifiBinding>
        implements WifiPresenter.View {

    public static final String TAG = FragmentConnectSelectWifi.class.getSimpleName();

    private WifiPresenter mPresenter;
    private AdapterWifiScanResult mAdapter;
    private static final int REQ_LOCATION_PERMMISION = 111;

    private ScanResult mSelectedWifi;
    private String mSelectedWifiPasswd;

    public static FragmentConnectSelectWifi newInstance() {
        Bundle args = new Bundle();

        FragmentConnectSelectWifi fragment = new FragmentConnectSelectWifi();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentConnectSelectWifi() {
    }

    @Override
    public boolean hasToolbarMenus() {
        return false;
    }

    @Override
    public void addAP(ScanResult bs) {
        getAdapter().addItem(bs);
    }

    @Override
    public void set10KAP(ScanResult ap) {
        ((IAuthInteraction)getActivity()).set10KAP(ap);
    }

    @Override
    public void refresh() {
        getAdapter().refresh();
    }

    @Override
    public void loadingStart() {
        if(mBinding!=null) {
            mBinding.loading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadingStop() {
        mBinding.loading.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_select_wifi;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LOCATION_PERMMISION);
            } else {
                scanWifi();
            }
        } else {
            scanWifi();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        LogUtil.d(TAG, "initView");
        mBinding.setFragment(this);
        mBinding.toolbarInclude.toolbarTitle.setText(getString(R.string.select_wifi_title));
        mBinding.listContainer.setAdapter(getAdapter());
        mBinding.listContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        getAdapter().setOnRecyclerItemClickListener((viewId, position) -> {
            if(viewId == R.id.item_wifi_container) {
                getPresenter().onClick(position);
                mSelectedWifi = getAdapter().getItem(position);
            }
        });
        mBinding.toolbarInclude.toolbarBack.setOnClickListener(v -> {
            ((ActivitySwitchConnection)getActivity()).changePage(getResources().getInteger(R.integer.frag_info));
        });
    }

    private void scanWifi() {
        getAdapter().clear();
        getAdapter().notifyDataSetChanged();
        getPresenter().startScan();
    }

    private WifiPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new WifiPresenterImpl(this, getActivity());
        }
        return mPresenter;
    }

    private AdapterWifiScanResult getAdapter() {
        if(mAdapter == null) {
            mAdapter = new AdapterWifiScanResult(getActivity());
        }
        return mAdapter;
    }

    @Override
    public void noSwitchFound() {
        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    getAdapter().clear();
                    getAdapter().notifyDataSetChanged();
                    getPresenter().startScan();
                    dialog.dismiss();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    ((IActivityFragmentPageChangeListener)getActivity()).changePage(getResources().getInteger(R.integer.frag_info));
                    break;
            }
        };

        try {
            CustomDialog dialog = new CustomDialog(getActivity())
                    .isEditable(false)
                    .setTitleText(R.string.select_wifi_error_dialog_title)
                    .setMessageText(R.string.select_wifi_error_dialog_content)
                    .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                    .setPositiveButton(getString(R.string.common_retry), onClickListener);
            dialog.show();
        } catch (Exception e) {
        }
    }

    @Override
    public void onBtnClick(View view) {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void openPassDialog(String ssid) {
        CustomDialog customDialog = new CustomDialog(getActivity());

        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    mSelectedWifiPasswd = customDialog.getEditTextMessage();
                    getPresenter().connect10K();
                    PreferenceHelper.getInstance(getActivity()).put(getString(R.string.arg_wifi_pass) + ssid, customDialog.getEditTextMessage());
                    break;
            }
            KeyPadUtil.KeyPadDown(getActivity(), customDialog.getEditText());
            dialog.dismiss();
        };

        customDialog.isEditable(true)
                .setTitleText(String.format("%s%s", getString(R.string.select_wifi_pass_dialog_title), ssid))
                .setEditTextMessage(PreferenceHelper.getInstance(getActivity()).getStringValue(getString(R.string.arg_wifi_pass) + ssid, ""))
                .setMessageText(R.string.select_wifi_pass_dialog_content)
                .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                .setPositiveButton(getString(R.string.common_ok), onClickListener)
                .setEditPasswdType(true);

        customDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQ_LOCATION_PERMMISION) {
            if(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!Settings.System.canWrite(getActivity())) {
                        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        LogUtil.e("MainActivity", "error starting permission intent", e);
                                    }
                                    break;
                            }
                            dialog.dismiss();
                        };
                        CustomDialog customDialog = new CustomDialog(getActivity());
                        customDialog.isEditable(false)
                                .isEditable(false)
                                .setTitleText(R.string.permission_dialog_title)
                                .setMessageText(R.string.permission_dialog_message)
                                .setPositiveButton(getString(R.string.common_allow), onClickListener)
                                .show();
                    }
                }
                scanWifi();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void changeFragSetSwitch() {
        ((IAuthInteraction)getActivity()).setSelectedWifi(mSelectedWifi, mSelectedWifiPasswd);
        ((IActivityFragmentPageChangeListener)getActivity()).changePage(getResources().getInteger(R.integer.frag_set_switch));
    }

    @Override
    public void onErrorConnectAP() {
        DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    getPresenter().connect10K();
                    dialog.dismiss();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    ((IActivityFragmentPageChangeListener)getActivity()).changePage(getResources().getInteger(R.integer.frag_info));
                    break;
            }
        };

        CustomDialog dialog = new CustomDialog(getActivity())
                .isEditable(false)
                .setTitleText(R.string.connect_select_wifi_not_connect_title)
                .setMessageText(R.string.connect_select_wifi_not_connect_content)
                .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                .setPositiveButton(getString(R.string.common_retry), onClickListener);
        dialog.show();
    }
}
