package com.vinay.vsatsaarthi;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar; // Import ProgressBar
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.vinay.vsatsaarthi.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static final String URL = "https://satellite-detail.onrender.com/satellite/getsatellite";
    ArrayList<String> satname = new ArrayList<>();
    ArrayList<String> dishname = new ArrayList<>();
    Location location;
    ImageView world;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        world = findViewById(R.id.earth);
        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(world, "translationX", 1, -screenWidth);
        objectAnimator2.setDuration(20000);
        objectAnimator2.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator2.start();

        satname.add("Satellite");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, satname);
        binding.spinner.setAdapter(adapter);

        binding.setlonglat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
                } else {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                binding.userlongitude.setText(String.valueOf(location.getLongitude()));
                                binding.userLatitude.setText(String.valueOf(location.getLatitude()));
                                getname();
                            } else {
                                Toast.makeText(MainActivity.this, "Location not available. Try moving outdoors.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        getname(); // Call getname() initially to load satellite list

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    error.printStackTrace();
                });
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
        ArrayAdapter<String> satdishadapter = new ArrayAdapter<>(this, R.layout.spinner_item, dishname);
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
                if (binding.userlongitude.getText().toString().isEmpty())
                    binding.userlongitude.setError("");
                else if (binding.userLatitude.getText().toString().isEmpty())
                    binding.userLatitude.setError("");
                else if (binding.satLongitude.getText().toString().isEmpty())
                    binding.satLongitude.setError("");
                else if (binding.satlatitude.getText().toString().isEmpty())
                    binding.satlatitude.setError("");
                else {
                    RequestQueue requestQueue1 = Volley.newRequestQueue(MainActivity.this);
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
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://satellite-detail.onrender.com/angle/calculatelookupangle", jsonObject, response -> {
                        try {
                            String azimuth = response.getString("azimuth");
                            String elevation = response.getString("elevation");
                            azimuth = azimuth.replaceAll("(\\.\\d{5})\\d+", "$1");
                            elevation = elevation.replaceAll("(\\.\\d{5})\\d+", "$1");
                            ((TextView) findViewById(R.id.azimuth)).setText(azimuth);
                            ((TextView) findViewById(R.id.elevation)).setText(elevation);
                            if (!azimuth.isEmpty() && !elevation.isEmpty()) {
                                binding.angleCard.setVisibility(View.VISIBLE);
                                binding.compass.setVisibility(View.VISIBLE);
                                ((ScrollView)findViewById(R.id.scrollbar)).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ScrollView) findViewById(R.id.scrollbar)).fullScroll(View.FOCUS_DOWN);
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
                if (binding.elevation.getText().toString().isEmpty())
                    binding.elevation.setError("");
                else if (binding.azimuth.getText().toString().isEmpty())
                    binding.azimuth.setError("");
                else {
                    Intent intent = new Intent(MainActivity.this, CompassActivity.class);
                    intent.putExtra("Azismuth", binding.azimuth.getText().toString());
                    intent.putExtra("Elevation", binding.elevation.getText().toString());
                    intent.putExtra("Longitude", binding.userlongitude.getText().toString());
                    intent.putExtra("Latitude", binding.userLatitude.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
        return true;
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    satname.add(jsonObject.getString("satname"));
                }
                // Update the adapter after data is fetched
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, satname);
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