package com.goqual.a10k.view.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goqual.a10k.R;
import com.goqual.a10k.util.LogUtil;
import com.goqual.a10k.view.activities.ActivityMain;
import com.goqual.a10k.view.interfaces.IActivityInteraction;
import com.goqual.a10k.view.interfaces.IFragmentInteraction;

import java.util.ArrayList;

/**
 * Created by HanWool on 2017. 2. 17..
 */

public abstract class BaseFragment<B extends ViewDataBinding> extends Fragment
        implements IFragmentInteraction{

    protected B mBinding;

    protected abstract int getLayoutId();
    public abstract void onBtnClick(View view);
    public abstract boolean hasToolbarMenus();

    protected IActivityInteraction mActivityInteraction;

    private boolean mFragmentVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityInteraction = (IActivityInteraction)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         mBinding = DataBindingUtil.inflate(
                inflater, getLayoutId(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 탭을 생성합니다.
     * 탭레이아웃의 파라미터와 각 셋팅을 설정해서 AppBarLayout에 add합니다.
     * @param texts 탭 텍스트들
     * @param icons @Nullable 탭 아이콘셋
     * @param tabSelectedListener 탭 선택 리스너.
     * @return TabLayout created TabLayout
     */
    @SuppressWarnings("Deprecations")
    public TabLayout addTab(@NonNull ArrayList<String> texts,
                            @Nullable ArrayList<Integer> icons,
                            @NonNull TabLayout.OnTabSelectedListener tabSelectedListener,
                            @ColorRes int normalColor,
                            @ColorRes int selectedColor
    ) {
        TabLayout tabLayout = new TabLayout(getActivity());
        AppBarLayout appBarLayout = mActivityInteraction.getAppbar();
        ViewGroup.LayoutParams params = appBarLayout.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        tabLayout.setLayoutParams(params);
        if(Build.VERSION.SDK_INT>=23) {
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark, null));
            tabLayout.setTabTextColors(getResources().getColor(normalColor, null), getResources().getColor(selectedColor, null));
            tabLayout.setElevation(1);
        }
        else {
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
            tabLayout.setTabTextColors(getResources().getColor(normalColor), getResources().getColor(selectedColor));
        }
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        for(String text : texts) {
            TabLayout.Tab tab = tabLayout.newTab().setText(text);
            if(icons != null){
                tab.setIcon(icons.get(texts.indexOf(text)));
            }
            tabLayout.addTab(tab);
        }
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
        appBarLayout.addView(tabLayout);
        return tabLayout;
    }

    @Override
    public abstract String getTitle();

    @Override
    public void setFragmentVisible(boolean isVisible) {
        LogUtil.d("setFragmentVisible", "VISIBLE::" + isVisible);
        mFragmentVisible = isVisible;
    }

    public boolean isFragmentVisible() {
        return mFragmentVisible;
    }
}
