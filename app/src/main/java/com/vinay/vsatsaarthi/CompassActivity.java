package com.vinay.vsatsaarthi;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.vinay.vsatsaarthi.databinding.ActivityCompassBinding;
public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    ActivityCompassBinding binding;
    float accelerometer[]=new float[3];
    float magnetometer[]=new float[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCompassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(findViewById(R.id.toolbar));
        Intent intent=getIntent();
        Double azimuth=Double.parseDouble(intent.getStringExtra("Azimuth"));
        Double elevation=Double.parseDouble(intent.getStringExtra("Elevation"));
        intent.getStringExtra("Elevation");
        binding.satazimuth.setText(Math.round(azimuth)+"");
        binding.satelevation.setText(Math.round(elevation)+"");
        binding.longitude.setText(intent.getStringExtra("Longitude"));
        binding.latitude.setText(intent.getStringExtra("Latitude"));
        SensorManager sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelarometer =sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnetometer =sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(accelarometer!=null && magnetometer!=null)
        {
            sensorManager.registerListener(this,accelarometer,SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(this,magnetometer,SensorManager.SENSOR_DELAY_NORMAL);
        }
        else
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            accelerometer[0]=event.values[0];
            accelerometer[1]=event.values[1];
            accelerometer[2]=event.values[2];
        }
        else if (event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
        {
            magnetometer[0]=event.values[0];
            magnetometer[1]=event.values[1];
            magnetometer[2]=event.values[2];
        }
        if(accelerometer[0]!=0 && accelerometer[1]!=0 && accelerometer[2]!=0 && magnetometer[0]!=0 && magnetometer[1]!=0 && magnetometer[2]!=0)
        {
            float rotationMatrix[]=new float[9];
            float orientationValues[]=new float[3];
            boolean success=SensorManager.getRotationMatrix(rotationMatrix,null,new float[]{accelerometer[0],accelerometer[1],accelerometer[2]},new float[]{magnetometer[0],magnetometer[1],magnetometer[2]});
            if(success)
            {
                SensorManager.getOrientation(rotationMatrix,orientationValues);
                int azimuth=(int) Math.toDegrees(orientationValues[0]);
                int elevation=(int) Math.toDegrees(orientationValues[1]);
                azimuth=(azimuth+360)%360;
                binding.compass.setRotation(-azimuth);
                binding.userazimuth.setText(azimuth+"");
                binding.userelevation.setText(elevation+"");
                if(binding.satazimuth.getText().toString().equals(binding.userazimuth.getText().toString())&&binding.satelevation.getText().toString().equals(binding.userelevation.getText().toString()))
                    mediaplayer(true);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    public void mediaplayer(boolean flag)
    {
        MediaPlayer mediaPlayer =new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        String audiopath="android.resource://com.vinay.vsatsaarthi/raw/beepsound";
        Uri audio=Uri.parse(audiopath);
        try {
            mediaPlayer.setDataSource(this,audio);
            mediaPlayer.prepare();}
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(flag) mediaPlayer.start();
        else mediaPlayer.stop();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(CompassActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}