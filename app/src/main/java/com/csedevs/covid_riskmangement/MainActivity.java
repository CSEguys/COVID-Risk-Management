package com.csedevs.covid_riskmangement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.csedevs.covid_riskmangement.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String email,pwd;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(MainActivity.this,HomePageActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();

        activityMainBinding.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValues();
                firebaseAuth.createUserWithEmailAndPassword(email,pwd)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                updateUI();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Failed SignUp, MightBe weak password!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void updateUI() {
        startActivity(new Intent(MainActivity.this,GetValueActivity.class));
        finish();
    }

    private void getValues() {

        email = activityMainBinding.mail.getText().toString();
        pwd = activityMainBinding.pwd.getText().toString();

    }
}
