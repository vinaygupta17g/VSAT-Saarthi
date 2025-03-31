package com.vinay.vsatsaarthi;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vinay.vsatsaarthi.databinding.ActivityCompassBinding;

public class CompassActivity extends AppCompatActivity {
ActivityCompassBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCompassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        intent.getStringExtra("Azismuth");
        intent.getStringExtra("Elevation");
    }
}