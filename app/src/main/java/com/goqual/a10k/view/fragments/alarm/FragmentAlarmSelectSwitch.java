package com.goqual.a10k.view.fragments.alarm;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.FragmentAlarmSelectSwitchBinding;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.view.adapters.AdapterAlarmSwitchSelect;
import com.goqual.a10k.view.adapters.AdapterSwitch;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.dialog.CustomDialog;
import com.goqual.a10k.view.interfaces.IAlarmInteraction;

/**
 * Created by ladmusician on 2016. 12. 29..
 */

public class FragmentAlarmSelectSwitch extends BaseFragment<FragmentAlarmSelectSwitchBinding> {
    public static final String TAG = FragmentAlarmSelectSwitch.class.getSimpleName();
    private AdapterAlarmSwitchSelect mSwitchAdapter;

    public static FragmentAlarmSelectSwitch newInstance() {
        
        Bundle args = new Bundle();
        
        FragmentAlarmSelectSwitch fragment = new FragmentAlarmSelectSwitch();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alarm_select_switch;
    }

    @Override
    public void onBtnClick(View view) {

    }

    @Override
    public boolean hasToolbarMenus() {
        return false;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mBinding.listContainer.setAdapter(getSwitchAdapter());
        mBinding.listContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private AdapterAlarmSwitchSelect getSwitchAdapter() {
        if(mSwitchAdapter == null) {
            mSwitchAdapter = new AdapterAlarmSwitchSelect(getActivity());
            mSwitchAdapter.updateItems(SwitchManager.getInstance().getList());
            mSwitchAdapter.setOnRecyclerItemClickListener((viewId, position) -> {
                Switch item = SwitchManager.getInstance().getItem(position);
                DialogInterface.OnClickListener onClickListener = (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            ((IAlarmInteraction)getActivity()).goBtnPage(position);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                    dialog.dismiss();
                };
                new CustomDialog(getActivity())
                        .isEditable(false)
                        .setTitleText(R.string.alarm_select_switch_title)
                        .setMessageText(String.format("%s %s", item.getTitle(), getString(R.string.alarm_select_switch_content)))
                        .setPositiveButton(getString(R.string.common_ok), onClickListener)
                        .setNegativeButton(getString(R.string.common_cancel), onClickListener)
                        .show();
            });
        }
        return mSwitchAdapter;
    }
}