package com.vinay.vsatsaarthi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vinay.vsatsaarthi.databinding.FragmentSatellitesBinding;

public class Satellites extends Fragment {
    FragmentSatellitesBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSatellitesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}