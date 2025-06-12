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
        binding.calcgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.diameter.getText().toString().isEmpty())
                binding.diameter.setError("");
                else if(binding.frequency.getText().toString().isEmpty())
                    binding.frequency.setError("");
                else if(binding.wavelength.getText().toString().isEmpty())
                    binding.wavelength.setError("");
                else{
                    binding.root1.setVisibility(View.VISIBLE);
                    binding.root.setVisibility(View.VISIBLE);
                    double gain=0.7*(22/7)*(22/7)*(Math.pow((Double.parseDouble(binding.diameter.getText().toString()))/(Double.parseDouble(binding.wavelength.getText().toString())),2));
                    int gaininGHZ=(int)Math.floor(10*Math.log10(gain));
                    binding.gain.setText(gaininGHZ+"");
                }
            }
        });
        binding.symbolrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.datarate.getText().toString().isEmpty())
                binding.datarate.setError("");
                else if(binding.modulationfactor.getText().toString().isEmpty())
                    binding.modulationfactor.setError("");
                else if(binding.fec.getText().toString().isEmpty())
                    binding.fec.setError("");
                else
                {
                    Double sr = (Double.parseDouble(binding.datarate.getText().toString()))/((Double.parseDouble(binding.modulationfactor.getText().toString()))*(Double.parseDouble(binding.fec.getText().toString())));
                    String symbol_rate = String.format("%.7f ",sr);
                    binding.sr1.setVisibility(View.VISIBLE);
                    binding.sr2.setVisibility(View.VISIBLE);
                    binding.srrate.setText(symbol_rate);
                }
            }
        });
        return binding.getRoot();
    }
}