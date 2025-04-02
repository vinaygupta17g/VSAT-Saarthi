package com.vinay.vsatsaarthi;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuWrapperICS;

import com.vinay.vsatsaarthi.databinding.ActivityCompassBinding;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {
ActivityCompassBinding binding;
    float accelerometerx=0f;
    float accelerometery=0f;
    float accelerometerz=0f;
    float magnetometerx=0f;
    float magnetometery=0f;
    float magnetometerz=0f;
    public void mediaplayer(boolean value)
    {
        MediaPlayer mp=new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        String audiopath="android.resource://com.vinay.vsatsaarthi/raw/beepsound";
        Uri audio=Uri.parse(audiopath);
        try {
            mp.setDataSource(this,audio);
            mp.prepare();
        }
        catch(Exception e)
        {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if(value)
            mp.start();
        else
            mp.stop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCompassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        Double azismuth=Double.parseDouble(intent.getStringExtra("Azismuth"));
        Double elevation=Double.parseDouble(intent.getStringExtra("Elevation"));
        intent.getStringExtra("Elevation");
        binding.satazismuth.setText(Math.round(azismuth)+"");
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
        {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            accelerometerx=event.values[0];
            accelerometery=event.values[1];
            accelerometerz=event.values[2];
        }
        else if (event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
        {
            magnetometerx=event.values[0];
            magnetometery=event.values[1];
            magnetometerz=event.values[2];
        }
        if(accelerometerx!=0 && accelerometery!=0 && accelerometerz!=0 && magnetometerx!=0 && magnetometery!=0 && magnetometerz!=0)
        {
            float rotationMatrix[]=new float[9];
            float orientationValues[]=new float[3];
            boolean success=SensorManager.getRotationMatrix(rotationMatrix,null,new float[]{accelerometerx,accelerometery,accelerometerz},new float[]{magnetometerx,magnetometery,magnetometerz});
            if(success)
            {
                SensorManager.getOrientation(rotationMatrix,orientationValues);
                int azimuth=(int) Math.toDegrees(orientationValues[0]);
                int elevation=(int) Math.toDegrees(orientationValues[1]);

                azimuth=(azimuth+360)%360;
                binding.compass.setRotation(-azimuth);
                binding.userazismuth.setText(azimuth+"");
                binding.userelevation.setText(elevation+"");
                if (binding.satazismuth.getText().toString().equals(binding.userazismuth.getText().toString())&&binding.satelevation.getText().toString().equals(binding.userelevation.getText().toString()))
                {
                    mediaplayer(true);
                }
                else
                {
                    mediaplayer(false);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}