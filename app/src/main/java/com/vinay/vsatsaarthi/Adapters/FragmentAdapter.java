package com.vinay.vsatsaarthi.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vinay.vsatsaarthi.AddSatellite;
import com.vinay.vsatsaarthi.Satellites;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return new AddSatellite();
        else if (position==1)
            return new Satellites();
        return new AddSatellite();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int positionn)
    {
        String title="Add Satellite";
        if(positionn==0)
            title="Add Satellite";
        else if (positionn==1)
            title="Satellite";
        return title;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
