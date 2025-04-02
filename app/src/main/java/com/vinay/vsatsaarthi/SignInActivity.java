package com.vinay.vsatsaarthi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.vinay.vsatsaarthi.databinding.ActivitySignInBinding;
import com.vinay.vsatsaarthi.databinding.ActivitySignUpBinding;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    Intent intent;
    Context context;
    FirebaseAuth auth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=this;
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(binding.email.getText().toString().isEmpty())
                  binding.email.setError("");
              else if(binding.password.getText().toString().isEmpty())
                  binding.password.setError("");
              else
              {
                  auth.signInWithEmailAndPassword(binding.email.getText().toString(),binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if(task.isSuccessful())
                          {
                              intent=new Intent(context,MainActivity.class);
                              startActivity(intent);
                              finish();
                          }
                          else
                          {
                              Toast.makeText(context,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                          }
                      }
                  });
              }
            }
        });
        if(auth.getCurrentUser()!=null)
        {
            if(auth.getCurrentUser().getEmail().equals("vinaykumarmitrc@gmail.com"))
            {
                intent=new Intent(context,MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(context, "Sign in as admin", Toast.LENGTH_SHORT).show();
            }

        }
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(context,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}