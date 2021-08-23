package com.root.mssm.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.root.mssm.ui.specification.discription.DiscriptionFragment;
import com.root.mssm.ui.specification.moreinfo.MoreInfoFragment;
import com.root.mssm.ui.specification.speccificationdetail.SpecificationDetailFragment;

public class AdapterSpecificationTab extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public AdapterSpecificationTab(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DiscriptionFragment discriptionFragment = new DiscriptionFragment();
                return discriptionFragment;
            case 1:
                SpecificationDetailFragment specificationDetailFragment = new SpecificationDetailFragment();
                return specificationDetailFragment;
            case 2:
                MoreInfoFragment moreInfoFragment = new MoreInfoFragment();
                return moreInfoFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}