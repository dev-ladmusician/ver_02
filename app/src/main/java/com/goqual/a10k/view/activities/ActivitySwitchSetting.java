package com.goqual.a10k.view.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.goqual.a10k.R;
import com.goqual.a10k.databinding.ActivitySwitchSettingBinding;
import com.goqual.a10k.helper.PreferenceHelper;
import com.goqual.a10k.model.SwitchManager;
import com.goqual.a10k.model.entity.Switch;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.util.event.EventToolbarClick;
import com.goqual.a10k.view.adapters.AdapterPager;
import com.goqual.a10k.view.base.BaseActivity;
import com.goqual.a10k.view.base.BaseFragment;
import com.goqual.a10k.view.fragments.setting.FragmentSettingAbsence;
import com.goqual.a10k.view.fragments.setting.FragmentSettingAdmin;
import com.goqual.a10k.view.fragments.setting.FragmentSettingHistory;
import com.goqual.a10k.view.fragments.setting.FragmentSettingNfc;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IToolbarClickListener;
import com.goqual.a10k.view.interfaces.IToolbarInteraction;

import static com.goqual.a10k.view.interfaces.IToolbarClickListener.STATE.EDIT;

/**
 * Created by hanwool on 2017. 2. 28..
 */

public class ActivitySwitchSetting extends BaseActivity<ActivitySwitchSettingBinding>
implements IActivityInteraction, IToolbarInteraction {

    public static final String TAG = ActivitySwitchSetting.class.getSimpleName();

    public static final String ITEM_SWITCH = "item_switch";

    private AdapterPager mAdapterPage;
    private Switch mSwitch;
    private int mSwitchPosition;

    private EventToolbarClick mEventToolbarClick;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_switch_setting;
    }

    @Override
    public void setTitle(CharSequence title) {
        mBinding.toolbarTitle.setText(title);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mSwitchPosition = getIntent().getExtras().getInt(ITEM_SWITCH);
            mSwitch = SwitchManager.getInstance().getItem(mSwitchPosition);

            if (mSwitch == null) {
                throw new IllegalStateException("Empty INTENT!");
            }

            mBinding.toolbarTitle.setText(mSwitch.getTitle());
        }
        initViewPager();
        initTab();
        setTitle(mSwitch.getTitle());

        if (mSwitch.isadmin()) mBinding.setEditSwitchState(IToolbarClickListener.STATE.DONE);
        else mBinding.setEditSwitchState(IToolbarClickListener.STATE.HIDE);
    }

    private void initViewPager() {
        mEventToolbarClick = new EventToolbarClick(IToolbarClickListener.STATE.DONE);
        //mBinding.setEditSwitchState(mEventToolbarClick.getState());
        mBinding.setActivity(this);
        mAdapterPage = new AdapterPager(getSupportFragmentManager());
        mAdapterPage.addItem(FragmentSettingAdmin.newInstance(mSwitchPosition));
        mAdapterPage.addItem(FragmentSettingNfc.newInstance(mSwitchPosition));
        if (mSwitch.isadmin) mAdapterPage.addItem(FragmentSettingAbsence.newInstance(mSwitchPosition));
        mAdapterPage.addItem(FragmentSettingHistory.newInstance(mSwitchPosition));
        mBinding.settingContainer.setAdapter(mAdapterPage);
        mBinding.settingContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(((BaseFragment) mAdapterPage.getItem(position)).getTitle());

                try {
                    invalidateFragmentMenus(position);
                    mBinding.settingTabs.getTabAt(position).select();
                } catch (NullPointerException e){
                    LogUtil.e(TAG, e.getMessage(), e);
                }

                if(((BaseFragment)mAdapterPage.getItem(position)).hasToolbarMenus()) {
                    mBinding.toolbarEditContainer.setVisibility(View.VISIBLE);
                }  else {
                    mBinding.toolbarEditContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            private void invalidateFragmentMenus(int position){
                for(int i = 0; i < mAdapterPage.getCount(); i++){
                    mAdapterPage.getItem(i).setHasOptionsMenu(i == position);
                }
                invalidateOptionsMenu(); //or respectively its support method.
            }
        });
        mBinding.settingContainer.setOffscreenPageLimit(3);
    }

    private void initTab() {
        mBinding.settingTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.settingContainer.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mBinding.settingTabs.addTab(mBinding.settingTabs.newTab().setText(R.string.tab_title_admin));
        mBinding.settingTabs.addTab(mBinding.settingTabs.newTab().setText(R.string.tab_title_nfc));
        if (mSwitch.isadmin) mBinding.settingTabs.addTab(mBinding.settingTabs.newTab().setText(R.string.tab_title_absence));
        mBinding.settingTabs.addTab(mBinding.settingTabs.newTab().setText(R.string.tab_title_history));
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_edit_container:
                if (mEventToolbarClick.getState() == EDIT)
                    mEventToolbarClick.setState(IToolbarClickListener.STATE.DONE);
                else
                    mEventToolbarClick.setState(EDIT);
                passToolbarClickEvent(mEventToolbarClick.getState());
                break;
        }
    }

    /**
     * toolbar 버튼 클릭 이벤트 frag한테 보내기
     * @param state
     */
    private void passToolbarClickEvent(IToolbarClickListener.STATE state) {
        ((IToolbarClickListener)mAdapterPage.getItem(mBinding.settingContainer.getCurrentItem())).onClickEdit(state);
    }

    /**
     * toolbar 상태 변경
     * @param state
     */
    @Override
    public void setToolbarEdit(IToolbarClickListener.STATE state) {
        if (state == IToolbarClickListener.STATE.DONE)
            mBinding.toolbarEdit.setText(getString(R.string.toolbar_edit));
        else if (state == EDIT)
            mBinding.toolbarEdit.setText(getString(R.string.toolbar_done));
        mEventToolbarClick.setState(state);
        mBinding.setEditSwitchState(state);
    }

    @Override
    public int getCurrentPage() {
        return mBinding.settingContainer.getCurrentItem();
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
    public void setTitle(String title) {
    }

    @Override
    public void finishApp() {
    }

    @Override
    public PreferenceHelper getPreferenceHelper() {
        return new PreferenceHelper(this);
    }

    @Override
    public void onBackPressed() {
        if(!((FragmentSettingAdmin)mAdapterPage.getItem(0)).getSwitch().isadmin())
            super.onBackPressed();

        if (mEventToolbarClick.getState() == EDIT) {
            mEventToolbarClick.setState(IToolbarClickListener.STATE.DONE);
            passToolbarClickEvent(mEventToolbarClick.getState());
            return;
        } else {
            super.onBackPressed();
        }
    }
}
