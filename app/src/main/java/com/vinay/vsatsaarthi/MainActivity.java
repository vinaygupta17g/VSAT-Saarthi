package com.vinay.vsatsaarthi;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.vinay.vsatsaarthi.databinding.ActivityMainBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<String> satname=new ArrayList<>();
    ArrayList<String> dishname=new ArrayList<>();
    Location location;
    ImageView world;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        world = findViewById(R.id.earth);
        float screenWidth =getResources().getDisplayMetrics().widthPixels;
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(world, "translationX", 1,-screenWidth );
        objectAnimator2.setDuration(20000);
        objectAnimator2.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator2.start();
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
                        binding.setlonglat.setBackgroundDrawable(getDrawable(R.drawable.btn_clicked_bg));
                        binding.userlongitude.setText(location.getLongitude()+"");
                        binding.userLatitude.setText(location.getLatitude()+"");
                    }
                    else
                        Toast.makeText(MainActivity.this, "Give Access for location", Toast.LENGTH_SHORT).show();
                }
            }
        });
        satname.add("Select Satellite ----");
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,R.layout.spinner_item,satname);
        binding.spinner.setAdapter(adapter);

        //API IMPLEMENTATION
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.GET, "https://satellite-detail.onrender.com/satellite/getsatellite", null, response -> {
            try {
                satname.clear();
                for (int i=0;i<response.length();i++)
                {
                    JSONObject jsonObject=response.getJSONObject(i);
                    String name=jsonObject.getString("satname");
                    satname.add(name);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Log.e("Error message",e.getMessage()+"");
            }
        }, error -> Log.d("Error message",error.getMessage()+""));
        requestQueue.add(jsonArrayRequest);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem=parent.getItemAtPosition(position).toString();
                RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
                JsonArrayRequest jsonArrayRequest =new JsonArrayRequest(Request.Method.GET, "https://satellite-detail.onrender.com/satellite/getsatellite", null, response -> {
                    try {
                        for (int i=0;i<response.length();i++)
                        {
                            JSONObject jsonObject=response.getJSONObject(i);
                            String name=jsonObject.getString("satname");
                            String latitude=jsonObject.getString("satlatitude");
                            String longitude=jsonObject.getString("satlongitude");
                            String altitude=jsonObject.getString("sataltitude");
                            if(selectedItem.equals(name))
                            {
                                binding.satlatitude.setText(latitude);
                                binding.satLongitude.setText(longitude);
                                binding.sataltitude.setText(altitude);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("Error message",e.getMessage()+"");
                    }
                }, error -> Log.d("Error message",error.getMessage()+""));
                requestQueue.add(jsonArrayRequest);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        dishname.add("Select Service Provider ----");
        dishname.add("Dish TV");
        dishname.add("DD Free Dish");
        dishname.add("TATA Play");
        ArrayAdapter<String> satdishadapter =new ArrayAdapter<>(this, R.layout.spinner_item,dishname);
        binding.satdish.setAdapter(satdishadapter);
        binding.satdish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.spinner.setSelection(adapter.getPosition(satName(parent.getItemAtPosition(position).toString())));
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
                    binding.calculate.setBackgroundDrawable(getDrawable(R.drawable.btn_clicked_bg));
                    binding.angle.setVisibility(View.VISIBLE);
                    binding.compass.setVisibility(View.VISIBLE);
                    Double longitude=Double.parseDouble(binding.userlongitude.getText().toString());
                    Double latitude=Double.parseDouble(binding.userLatitude.getText().toString());
                    Double satlong=Double.parseDouble(binding.satLongitude.getText().toString());
                    Double satlat=Double.parseDouble(binding.satlatitude.getText().toString());
                    Double altitude=Double.parseDouble(binding.sataltitude.getText().toString());
                    CalculateLookUpAngle(latitude,longitude,satlat,satlong,altitude);
                }
            }
        });
        binding.compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.elevation.getText().toString().isEmpty())
                    binding.elevation.setError("");
                else if(binding.azimuth.getText().toString().isEmpty())
                    binding.azimuth.setError("");
                else{
                    binding.compass.setBackgroundDrawable(getDrawable(R.drawable.btn_clicked_bg));
                    Intent intent=new Intent(MainActivity.this,CompassActivity.class);
                    intent.putExtra("Azismuth",binding.azimuth.getText().toString());
                    intent.putExtra("Elevation",binding.elevation.getText().toString());
                    intent.putExtra("Longitude",binding.userlongitude.getText().toString());
                    intent.putExtra("Latitude",binding.userLatitude.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    public void CalculateLookUpAngle(Double latitude ,Double longitude,Double satlat,Double SatLong,Double sataltitude)
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
        ((TextView)findViewById(R.id.azimuth)).setText(String.format("%.9f", azimuth));
        ((TextView)findViewById(R.id.elevation)).setText(String.format("%.9f",elevation));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this,SignInActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
    public String satName(String serviceprovidername)
    {
        String satName="";
        if(serviceprovidername.equals("Dish TV")||serviceprovidername.equals("DD Free Dish"))
            return satName="GSAT-15";
        else if (serviceprovidername.equals("TATA Play"))
            return  satName="GSAT-24";
        return satName;
    }
}