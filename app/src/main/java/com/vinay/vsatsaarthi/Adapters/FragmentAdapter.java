package com.vinay.vsatsaarthi.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vinay.vsatsaarthi.Fragments.LookUpAngle;
import com.vinay.vsatsaarthi.Fragments.LossCalculator;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0)
            return new LookUpAngle();
        else
            return new LossCalculator();
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        if (position==0)
            title="Look Up Angle";
        else if(position==1)
            title="Loss Calculator";
        return title;
    }
}
