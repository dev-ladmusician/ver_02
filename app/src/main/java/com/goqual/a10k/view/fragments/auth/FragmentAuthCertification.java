package com.goqual.a10k.view.fragments.auth;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentAuthCertificationBinding;
import com.goqual.a10k.view.activities.ActivityPhoneAuth;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.interfaces.IAuthActivityInteraction;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hanwool on 2017. 2. 24..
 */

public class FragmentAuthCertification extends BaseFragment<FragmentAuthCertificationBinding>{
    
    public static final String TAG = FragmentAuthCertification.class.getSimpleName();

    public static final String AUTH_KEY = "auth_key";
    public static final String PHONE_NUMBER = "phone_number";

    private String authKey;
    private String phoneNumber;
    private int mMinueteCount = 60;
    private Timer secondeTick;
    private Handler mHandler;

    public static FragmentAuthCertification newInstance(String phoneNumber, String authKey) {
        
        Bundle args = new Bundle();
        
        FragmentAuthCertification fragment = new FragmentAuthCertification();

        args.putString(AUTH_KEY, authKey);
        args.putString(PHONE_NUMBER, phoneNumber);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_auth_certification;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            authKey = getArguments().getString(AUTH_KEY, "");
            phoneNumber = getArguments().getString(PHONE_NUMBER, "");
        }
        mHandler = new Handler();
        secondeTick = new Timer();

        TimerTask secondTickTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mMinueteCount == 0) {
                            secondeTick.cancel();
                            secondeTick.purge();
                            mBinding.authCertiEdit.setEnabled(false);
                            ((ActivityPhoneAuth)getActivity()).onErrorAuthProcess(ActivityPhoneAuth.ERROR_REASON.TIMEOUT,
                                    getString(R.string.auth_certi_dialog_certifinum_timeover));
                        }
                        mBinding.authCertiTxtCount.setText(String.format(Locale.KOREA, "%d%s", mMinueteCount--, getString(R.string.auth_certi_unit)));
                    }
                });
            }
        };
        secondeTick.schedule(secondTickTask, 1000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setFragment(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        secondeTick.cancel();
        secondeTick.purge();
    }

    public void onBtnClick(View view) {
        if(view.getId() == R.id.auth_certi_btn_next) {
            if(authKey.equals(mBinding.authCertiEdit.getText().toString())){
                ((IAuthActivityInteraction)getActivity()).requestAppAuthToken(phoneNumber, authKey);
            }
            else {
                Snackbar.make(mBinding.getRoot(), R.string.auth_certi_dialog_incorrect_certifinum, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}