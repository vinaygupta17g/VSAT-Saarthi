package com.vinay.vsatsaarthi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinay.vsatsaarthi.databinding.FragmentLossCalculatorBinding;

public class LossCalculator extends Fragment {
    FragmentLossCalculatorBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =FragmentLossCalculatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}