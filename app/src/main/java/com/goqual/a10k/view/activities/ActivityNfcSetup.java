package com.goqual.a10k.view.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityNfcSetupBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Nfc;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.presenter.NfcTagAddEditPresenter;
import com.goqual.a10k.presenter.impl.NfcTagAddEditPresenterImpl;
import com.goqual.a10k.util.KeyPadUtil;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.dialog.CustomDialog;

/**
 * NFC 태그에 대한 스위치 설정 액티비티. <br />
 * TAG의 ID String{@code ActivityNfcSetup.EXTRA_NFC_TAG_ID}와
 * 설정할 스위치{@code ActivityNfcSetup.EXTRA_SWITCH} 객체{@see com.goqual.a10k.model.entity.Switch}를
 * Extra로 받아서 설정 후 설정된 객체를 TAG의 이름{@code ActivityNfcSetup.EXTRA_NFC_TAG_TITLE}과 함께 리턴합니다.
 * @author Hanwool
 * @version 1
 * @date 2017. 2. 28.
 */

public class ActivityNfcSetup extends BaseActivity<ActivityNfcSetupBinding>
    implements NfcTagAddEditPresenter.View {

    public static final String TAG = ActivityNfcSetup.class.getSimpleName();

    public static final String EXTRA_NFC_TAG_ID = "EXTRA_NFC_TAG_ID";
    public static final String EXTRA_NFC_TAG_TITLE = "EXTRA_NFC_TAG_TITLE";
    public static final String EXTRA_SWITCH = "EXTRA_SWITCH";

    private Switch mSwitch;
    private int mSwitchPosition;
    private String mNfcId;
    private NfcTagAddEditPresenter mPresenter = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nfc_setup;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwitchPosition = getIntent().getIntExtra(EXTRA_SWITCH, -1);
        mSwitch = SwitchManager.getInstance().getItem(mSwitchPosition);
        mNfcId = getIntent().getStringExtra(EXTRA_NFC_TAG_ID);
        initView();
    }

    private void initView() {
        mBinding.setActivity(this);
        mBinding.setItemSwitch(mSwitch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.switch_btn_1:
                mSwitch.setBtn1(!mSwitch.isBtn1());
                break;
            case R.id.switch_btn_2:
                mSwitch.setBtn2(!mSwitch.isBtn2());
                break;
            case R.id.switch_btn_3:
                mSwitch.setBtn3(!mSwitch.isBtn3());
                break;
            case R.id.toolbar_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.toolbar_save:
                CustomDialog dialog = new CustomDialog(this);
                DialogInterface.OnClickListener onClickListener = (dialog1, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            endSetting(dialog.getEditTextMessage());
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                    KeyPadUtil.KeyPadDown(getApplicationContext(), dialog.getEditText());
                    dialog.dismiss();
                };
                dialog.isEditable(true)
                        .setTitleText(R.string.nfc_rename_info_title)
                        .setMessageText(R.string.nfc_rename_info_content)
                        .setEditTextHint(R.string.nfc_rename_hint)
                        .setPositiveButton(getString(R.string.common_save), onClickListener)
                        .setNegativeButton(getString(R.string.common_delete), onClickListener)
                        .show();


                break;
        }
        mBinding.setItemSwitch(mSwitch);
    }

    private void endSetting(String tagTitle) {
        getPresenter().add(new Nfc(
                mSwitch.get_bsid(),
                mSwitch.isBtn1(),
                mSwitch.isBtn2(),
                mSwitch.isBtn3(),
                mNfcId,
                tagTitle));
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
        finish();
    }

    private NfcTagAddEditPresenter getPresenter() {
        if (mPresenter == null)
            mPresenter = new NfcTagAddEditPresenterImpl(getApplicationContext(), this);
        return mPresenter;
    }
}
