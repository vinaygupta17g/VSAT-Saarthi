package com.vinay.vsatsaarthi;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vinay.vsatsaarthi.Models.SatelliteModel;
import com.vinay.vsatsaarthi.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<String> satname=new ArrayList<>();
    Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        binding.setlonglat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager =(LocationManager) getSystemService(LOCATION_SERVICE);
                if(ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PackageManager.PERMISSION_GRANTED);
                }
                else {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location!=null){
                        binding.userlongitude.setText(location.getLongitude()+"");
                        binding.userLatitude.setText(location.getLatitude()+"");
                    }
                    else
                        Toast.makeText(MainActivity.this, "Give Access for location", Toast.LENGTH_SHORT).show();
                }
                }
        });
        satname.add("Select Satellite ----");
        database.getReference().child("Satellite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    SatelliteModel model=snapshot1.getValue(SatelliteModel.class);
                    model.setId(snapshot1.getKey());
                    satname.add(model.getSatelliteName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,satname);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem=parent.getItemAtPosition(position).toString();
            database.getReference().child("Satellite").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1:snapshot.getChildren())
                    {
                        SatelliteModel model=snapshot1.getValue(SatelliteModel.class);
                        if(model.getSatelliteName().equals(selectedItem))
                        {
                            binding.satLongitude.setText(model.getLongitude());
                            binding.satlatitude.setText(model.getLatetude());
                            binding.sataltitude.setText(model.getAltitude());
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.userlongitude.getText().toString().isEmpty())
                    binding.userlongitude.setError("");
                else if(binding.userLatitude.getText().toString().isEmpty())
                    binding.userLatitude.setError("");
                else if(binding.satLongitude.getText().toString().isEmpty())
                    binding.satLongitude.setError("");
                else if(binding.satlatitude.getText().toString().isEmpty())
                    binding.satlatitude.setError("");
                else
                {
                    binding.elevation.setVisibility(View.VISIBLE);
                    binding.azismuth.setVisibility(View.VISIBLE);
                    binding.compass.setVisibility(View.VISIBLE);
                    Double longitude=Double.parseDouble(binding.userlongitude.getText().toString());
                    Double latitude=Double.parseDouble(binding.userLatitude.getText().toString());
                    Double satlong=Double.parseDouble(binding.satLongitude.getText().toString());
                    Double satlat=Double.parseDouble(binding.satlatitude.getText().toString());
                    Double altitude=Double.parseDouble(binding.sataltitude.getText().toString());
                    //CalculateLookUpAngle(latitude,longitude,satlat,satlong,altitude);
                }
            }
        });
        binding.compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Compass is ready to show", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public String CalculateLookUpAngle(Double latitude ,Double longitude,Double satlat,Double SatLong,Double sataltitude)
    {
        double groundLatRad = Math.toRadians(latitude);
        double groundLonRad = Math.toRadians(longitude);
        double satLatRad = Math.toRadians(satlat);
        double satLonRad = Math.toRadians(SatLong);

        double earthRadius = 6378.0; // km
        double satRadius = earthRadius+sataltitude;
        // Calculate the range vector components in the Earth-centered, Earth-fixed (ECEF) frame.
        double groundX = earthRadius * Math.cos(groundLatRad) * Math.cos(groundLonRad);
        double groundY = earthRadius * Math.cos(groundLatRad) * Math.sin(groundLonRad);
        double groundZ = earthRadius * Math.sin(groundLatRad);

        double satX = satRadius * Math.cos(satLatRad) * Math.cos(satLonRad);
        double satY = satRadius * Math.cos(satLatRad) * Math.sin(satLonRad);
        double satZ = satRadius * Math.sin(satLatRad);

        // Calculate the range vector (satellite - ground).
        double rangeX = satX - groundX;
        double rangeY = satY - groundY;
        double rangeZ = satZ - groundZ;

        // Calculate the local horizontal coordinate system (LHCS) unit vectors.
        double eastX = -Math.sin(groundLonRad);
        double eastY = Math.cos(groundLonRad);
        double eastZ = 0.0;

        double northX = -Math.sin(groundLatRad) * Math.cos(groundLonRad);
        double northY = -Math.sin(groundLatRad) * Math.sin(groundLonRad);
        double northZ = Math.cos(groundLatRad);

        double upX = Math.cos(groundLatRad) * Math.cos(groundLonRad);
        double upY = Math.cos(groundLatRad) * Math.sin(groundLonRad);
        double upZ = Math.sin(groundLatRad);
        // Calculate the azimuth and elevation.
        double east = rangeX * eastX + rangeY * eastY + rangeZ * eastZ;
        double north = rangeX * northX + rangeY * northY + rangeZ * northZ;
        double up = rangeX * upX + rangeY * upY + rangeZ * upZ;
        double azimuth = Math.toDegrees(Math.atan2(east, north));
        if (azimuth < 0) {
            azimuth += 360.0;
        }
        double range = Math.sqrt(rangeX * rangeX + rangeY * rangeY + rangeZ * rangeZ);
        double elevation = Math.toDegrees(Math.asin(up / range));

        ((TextView)findViewById(R.id.azismuth)).setText(azimuth+"");
        ((TextView)findViewById(R.id.elevation)).setText(elevation+"");
        return "Elevation is "+elevation +" Azismuth angle is "+azimuth;
    }
}