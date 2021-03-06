package com.goqual.a10k.view.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivityPhoneAuthBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.presenter.impl.PhoneAuthPresenterImpl;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.presenter.PhoneAuthPresenter;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.fragments.auth.FragmentAuthCertification;
import com.goqual.a10k.view.fragments.auth.FragmentAuthPhone;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IAuthActivityInteraction;

/**
 * Created by hanwool on 2017. 2. 24..
 */

public class ActivityPhoneAuth extends BaseActivity<ActivityPhoneAuthBinding>
        implements IActivityInteraction, PhoneAuthPresenter.View, IAuthActivityInteraction{

    private final String fragPhoneTag = "frag_phone_tag";

    public enum ERROR_REASON{
        CONNECTION_ERROR,
        TIMEOUT,
        WRONG_NUMBER_FORMAT
    }
    public static final String TAG = ActivityPhoneAuth.class.getSimpleName();

    private static final int PHONE_NUMBER_PERMISSION_REQ = 112;

    private PhoneAuthPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_auth;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_NUMBER_PERMISSION_REQ);
        }

        initFirstFragment();
    }

    public void initFirstFragment() {
        BaseFragment baseFragment = FragmentAuthPhone.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(mBinding.fragmentContainer.getId(), baseFragment, fragPhoneTag)
                .commit();
    }

    @Override
    public void setInitPage() {
        changeCurrentPage(FragmentAuthPhone.newInstance());
    }

    @Override
    public void changeCurrentPage(Fragment frag) {
        getSupportFragmentManager().beginTransaction()
                .replace(mBinding.fragmentContainer.getId(), frag)
                .commit();
    }

    @Override
    public AppBarLayout getAppbar() {
        return null;
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public void onSuccessAuthProcess() {
        startActivity(new Intent(this, ActivityMain.class));
        finish();
    }

    @Override
    public void onErrorAuthProcess(ERROR_REASON reson, String message) {
        CustomDialog customDialog = new CustomDialog(this);
        DialogInterface.OnClickListener onClickListener = (dialog, which) ->  {
            dialog.dismiss();
            changeCurrentPage(FragmentAuthPhone.newInstance());
        };
        customDialog.isEditable(false)
                .setTitleText(R.string.auth_certi_title)
                .setMessageText(message)
                .setPositiveButton(false)
                .setNegativeButton(getString(R.string.common_ok), onClickListener)
                .show();

    }

    @Override
    public void loadingStart() {
        mBinding.authLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadingStop() {
        mBinding.authLoading.setVisibility(View.GONE);
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e(TAG, e.getMessage(), e);

        CustomDialog customDialog = new CustomDialog(this);
        DialogInterface.OnClickListener onClickListener = (dialog, which) ->  {
            dialog.dismiss();

        };
        customDialog.isEditable(false)
                .setTitleText(R.string.auth_certi_title)
                .setMessageText(R.string.auth_phone_error_content)
                .setPositiveButton(getString(R.string.common_retry), onClickListener)
                .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                .show();
    }

    @Override
    public void requestSmsToken(String phoneNumber) {
        getPresenter().requestSmsToken(phoneNumber);
    }

    @Override
    public void requestAppAuthToken(String phoneNumber, String smsToken) {
        getPresenter().join(phoneNumber, smsToken);
    }

    /**
     * 인증번호 요청 후 성공하면 인증번호 입력하는 화면으로 이동
     * @param phoneNumber
     * @param auth
     */
    @Override
    public void onSuccessPhoneNumberAuth(String phoneNumber, String auth) {
        changeCurrentPage(FragmentAuthCertification.newInstance(phoneNumber, auth));
    }

    /**
     * 핸드폰 번호 가져오는 권한을 요청 후 피드백
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PHONE_NUMBER_PERMISSION_REQ) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LogUtil.d(TAG, "onRequestPermissionsResult");
                FragmentAuthPhone frag = (FragmentAuthPhone)getSupportFragmentManager().findFragmentByTag(fragPhoneTag);
                frag.setCountryCode(getPresenter().getPhoneNumberCountryCode());
                frag.setPhoneNumber(getPresenter().getPhoneNumber());
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private PhoneAuthPresenter getPresenter() {
        if(mPresenter == null) {
            mPresenter = new PhoneAuthPresenterImpl(this, this);
        }
        return mPresenter;
    }

    @Override
    public PreferenceHelper getPreferenceHelper() {
        return null;
    }

    @Override
    public int getCurrentPage() {
        return 0;
    }

    @Override
    public void requestStartAppAuth() {
    }

    @Override
    public void onEndAuthProcess() {
    }

    @Override
    public void addItem(Object item) {
    }

    @Override
    public void refresh() {
    }

    @Override
    public void setTitle(String title) {
    }

    @Override
    public void finishApp() {
    }

    @Override
    public void onBtnClick(View view) {
    }
}
