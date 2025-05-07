package com.vinay.vsatsaarthi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinay.vsatsaarthi.databinding.FragmentAddSatelliteBinding;

public class AddSatellite extends Fragment {
    FragmentAddSatelliteBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding =FragmentAddSatelliteBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}