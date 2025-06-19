package com.vinay.vsatsaarthi.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vinay.vsatsaarthi.databinding.FragmentLossCalculatorBinding;
import org.json.JSONObject;
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
                EditText[] ids ={binding.diameter,binding.frequency,binding.wavelength};
                if(checkNull(ids))
                    gaincalculator(binding.diameter.getText().toString(),binding.frequency.getText().toString(),binding.gain);
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
                    setVisibility(binding.sr2,binding.sr1);
                    binding.srrate.setText(symbol_rate);
                }
            }
        });
        binding.calcfspl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.distance.getText().toString().isEmpty())
                    binding.distance.setError("");
                else if(binding.flossfrequency.getText().toString().isEmpty())
                    binding.flossfrequency.setError("");
                else if(binding.transmitgain.getText().toString().isEmpty())
                    binding.transmitgain.setError("");
                else if(binding.receivegain.getText().toString().isEmpty())
                    binding.receivegain.setError("");
                else
                {
                    double fspl1 = 20 * Math.log10(Double.parseDouble(binding.distance.getText().toString())) + 20 * Math.log10(Double.parseDouble(binding.flossfrequency.getText().toString())*Math.pow(10,9)) + 20 * Math.log10((4 * Math.PI) /(3*Math.pow(10,8)))-(Double.parseDouble(binding.transmitgain.getText().toString()))-(Double.parseDouble(binding.receivegain.getText().toString()));
                    String fspl =String.format("%.2f",fspl1);
                    binding.fspl.setText(fspl);
                    setVisibility(binding.fspl2,binding.fspl1);
                    binding.calculatedfspl.setText(fspl);
                }
            }
        });
        binding.calccnratio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double k = -228.6;
                double CN = (Double.parseDouble(binding.eirp.getText().toString())) - (Double.parseDouble(binding.calculatedfspl.getText().toString())) + (Double.parseDouble(binding.gtratio.getText().toString())) - k-(Double.parseDouble(binding.bandwidth.getText().toString()));
                binding.cnratio.setText(CN+"");
                setVisibility(binding.cn2,binding.cn1);
                binding.scrollbar.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.scrollbar.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
        return binding.getRoot();
    }
    public void gaincalculator(String diameter, String frequency, EditText id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JSONObject jsonObject =new JSONObject();try
        {
            jsonObject.put("diameter",diameter);
            jsonObject.put("frequency",frequency);
        }
        catch(Exception e) {e.printStackTrace();}
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,"http://ec2-3-7-254-184.ap-south-1.compute.amazonaws.com:5001/gain/gaincalculator",jsonObject, response -> {
            try {
                id.setText(response.getString("gain"));
                setVisibility(binding.root,binding.root1);
            }
            catch (Exception e) {e.printStackTrace();}
        },error -> {error.printStackTrace();});
        requestQueue.add(jsonObjectRequest);
    }
    public void setVisibility(LinearLayout id1, TextView id2) {
        id1.setVisibility(View.VISIBLE);
        id2.setVisibility(View.VISIBLE);
    }
    public boolean checkNull(EditText [] ids) {
        for(int i=0;i<ids.length;i++) {
            if(ids[i].getText().toString().isEmpty()) {
                ids[i].setError("");
                return false;
            }
        }
        return true;
    }
}