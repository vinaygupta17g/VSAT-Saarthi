package com.vinay.vsatsaarthi.Fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.vinay.vsatsaarthi.CompassActivity;
import com.vinay.vsatsaarthi.R;
import com.vinay.vsatsaarthi.databinding.FragmentLookUpAngleBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
public class LookUpAngle extends Fragment {
    FragmentLookUpAngleBinding binding;
    public static final String URL = "http://ec2-3-7-254-184.ap-south-1.compute.amazonaws.com:5001/satellite/getsatellite";
    ArrayList<String> satname = new ArrayList<>();
    ArrayList<String> dishname = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLookUpAngleBinding.inflate(inflater,container,false);
        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(binding.earth, "translationX", 1, -screenWidth);
        objectAnimator2.setDuration(20000);
        objectAnimator2.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator2.start();
        satname.add("Satellite");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, satname);
        binding.spinner.setAdapter(adapter);
        binding.setlonglat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
                } else {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                binding.userlongitude.setText(String.valueOf(location.getLongitude()));
                                binding.userLatitude.setText(String.valueOf(location.getLatitude()));
                            } else {
                                Toast.makeText(getContext(), "Location not available. Try moving outdoors.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        getname();
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String name = jsonObject.getString("satname");
                            String latitude = jsonObject.getString("satlatitude");
                            String longitude = jsonObject.getString("satlongitude");
                            String altitude = jsonObject.getString("sataltitude");
                            if (selectedItem.equals(name)) {
                                binding.satlatitude.setText(latitude);
                                binding.satLongitude.setText(longitude);
                                binding.sataltitude.setText(altitude);
                            }
                        }
                    } catch (JSONException e) {e.printStackTrace();}
                }, error -> {error.printStackTrace();});
                requestQueue.add(jsonArrayRequest);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        dishname.add("Provider");
        dishname.add("Dish TV");
        dishname.add("DD Free Dish");
        dishname.add("TATA Play");
        ArrayAdapter<String> satdishadapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, dishname);
        binding.satdish.setAdapter(satdishadapter);

        binding.satdish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedIndex = satname.indexOf(satName(parent.getItemAtPosition(position).toString()));
                if (selectedIndex != -1) {
                    binding.spinner.setSelection(selectedIndex);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText[] ids ={binding.userlongitude,binding.userLatitude,binding.satLongitude,binding.satlatitude,binding.sataltitude};
                if (LossCalculator.checkNull(ids)){
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("longitude", binding.userlongitude.getText().toString());
                        jsonObject.put("latitude", binding.userLatitude.getText().toString());
                        jsonObject.put("satlatitude", binding.satlatitude.getText().toString());
                        jsonObject.put("satlongitude", binding.satLongitude.getText().toString());
                        jsonObject.put("sataltitude", binding.sataltitude.getText().toString());
                    } catch (Exception e) {
                        Log.e("Error message", e.getMessage() + "");
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://ec2-3-7-254-184.ap-south-1.compute.amazonaws.com:5001/angle/calculatelookupangle", jsonObject, response -> {
                        try {
                            String azimuth = response.getString("azimuth");
                            String elevation = response.getString("elevation");
                            azimuth = azimuth.replaceAll("(\\.\\d{5})\\d+", "$1");
                            elevation = elevation.replaceAll("(\\.\\d{5})\\d+", "$1");
                            binding.azimuth.setText(azimuth);
                            binding.elevation.setText(elevation);
                            if (!azimuth.isEmpty() && !elevation.isEmpty()) {
                                binding.angleCard.setVisibility(View.VISIBLE);
                                binding.compass.setVisibility(View.VISIBLE);
                                binding.scrollbar.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.scrollbar.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        error.printStackTrace();
                    });
                    requestQueue1.add(jsonObjectRequest);
                }
            }
        });

        binding.compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CompassActivity.class);
                    intent.putExtra("Azimuth", binding.azimuth.getText().toString());
                    intent.putExtra("Elevation", binding.elevation.getText().toString());
                    intent.putExtra("Longitude", binding.userlongitude.getText().toString());
                    intent.putExtra("Latitude", binding.userLatitude.getText().toString());
                    startActivity(intent);
                    getActivity().finish();
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();

    }
    public String satName(String serviceprovidername) {
        String satName = "";
        if (serviceprovidername.equals("Dish TV") || serviceprovidername.equals("DD Free Dish"))
            return satName = "GSAT-15";
        else if (serviceprovidername.equals("TATA Play"))
            return satName = "GSAT-24";
        return satName;
    }
    public void getname() {
        satname.clear();
        satname.add("Satellite");
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    satname.add(jsonObject.getString("satname"));
                }
                // Update the adapter after data is fetched
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, satname);
                binding.spinner.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        });
        requestQueue.add(jsonArrayRequest);
    }
}