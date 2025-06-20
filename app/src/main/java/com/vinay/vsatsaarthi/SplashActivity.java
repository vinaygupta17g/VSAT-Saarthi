package com.vinay.vsatsaarthi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView=findViewById(R.id.logo);
        Thread thrd=new Thread() {
            public void run() {
                try {
                    Animation animation =AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
                    animation.setDuration(1000);
                    imageView.startAnimation(animation);
                    Thread.sleep(1000);
                    Animation animation1 =AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right);
                    imageView.startAnimation(animation1);
                    animation.setDuration(1000);
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG);
                }
                finally {
                    Intent intent=new Intent(SplashActivity.this,SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thrd.start();
    }
}