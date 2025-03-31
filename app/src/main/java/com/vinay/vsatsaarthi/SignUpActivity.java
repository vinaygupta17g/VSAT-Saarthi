package com.vinay.vsatsaarthi;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.vinay.vsatsaarthi.Models.UsersModel;
import com.vinay.vsatsaarthi.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.name.getText().toString().isEmpty())
                    binding.name.setError("");
                else if(binding.mobile.getText().toString().isEmpty())
                    binding.mobile.setError("");
                else if(binding.email.getText().toString().isEmpty())
                        binding.email.setError("");
                else if(binding.password.getText().toString().isEmpty())
                    binding.password.setError("");
                else
                {
                    auth.createUserWithEmailAndPassword(binding.email.getText().toString(),binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                String uid=task.getResult().getUser().getUid();
                                UsersModel user=new UsersModel(uid,binding.name.getText().toString(),binding.mobile.getText().toString(),binding.email.getText().toString(),binding.password.getText().toString());
                                database.getReference().child("Users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(SignUpActivity.this,"User Successfully Created", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(SignUpActivity.this, SignInActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }
}