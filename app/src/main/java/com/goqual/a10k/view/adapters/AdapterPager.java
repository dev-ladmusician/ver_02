package com.goqual.a10k.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.goqual.a10k.view.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by HanWool on 2017. 2. 17..
 */

public class AdapterPager extends FragmentStatePagerAdapter {
    private static final String TAG = AdapterPager.class.getSimpleName();

    ArrayList<BaseFragment> fragmentLists;

    public AdapterPager(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragmentLists = new ArrayList<>();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentLists.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return fragmentLists.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentLists.get(position);
    }

    public void addItem(BaseFragment item) {
        fragmentLists.add(item);
        refresh();
    }

    public ArrayList<BaseFragment> getFragmentLists() {
        return fragmentLists;
    }

    public void clear() {
        fragmentLists.clear();
        refresh();
    }

    public void refresh() {
        notifyDataSetChanged();
    }


}
