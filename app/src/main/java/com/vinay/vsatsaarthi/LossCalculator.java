package com.vinay.vsatsaarthi;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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
        binding.frequency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!binding.frequency.getText().toString().isEmpty()){
                    Double wavelength1=(3*Math.pow(10,8)/(Double.parseDouble(binding.frequency.getText().toString())*Math.pow(10,9)));
                    binding.wavelength.setText(wavelength1+"");
                }
                else
                    binding.wavelength.setText("");
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return binding.getRoot();
    }
}