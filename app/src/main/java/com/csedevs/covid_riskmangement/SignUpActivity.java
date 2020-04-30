package com.csedevs.covid_riskmangement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    //Components...
    EditText namegetter, agegetter, mailgetter, pwdgetter, phgetter;
    Button getin;

    //Auth Related inits...
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    //Var to hold hardcoded values....
    String name, age, mail, pwd, phone;

    //DataModel class....
    SignUpData signUpData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Lnking the xml to hard coded java....
        namegetter = findViewById(R.id.namegetter);
        agegetter = findViewById(R.id.agegetter);
        mailgetter = findViewById(R.id.mailgetter);
        pwdgetter = findViewById(R.id.pwdgetter);
        phgetter = findViewById(R.id.phgetter);
        getin = findViewById(R.id.getin);


        //initialising fireBase data...
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("root/users");

        getin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                1. Get input from the components..
                2. Create an Auth with the given mail id....
                3. Also create a dataModel in realTime DB and Upload the other information accordingly...
                */

                //First Step..
                getData();

                /*Second Step*/
                if(!mail.isEmpty() && !pwd.isEmpty()){
                    firebaseAuth.createUserWithEmailAndPassword(mail,pwd)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                        /*Third Step*/
                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        databaseReference.child(firebaseUser.getUid()).setValue(signUpData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(SignUpActivity.this,"Successful",Toast.LENGTH_LONG);
                                                        updateUI();
                                                    }
                                                });


                                    }else{
                                        Toast.makeText(SignUpActivity.this, "Failed SignUp", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Snackbar snackbar = Snackbar
                            .make(view, "Fill all fields..", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });

    }

    private void updateUI() {
        startActivity(new Intent(SignUpActivity.this, NeedActivity.class));
    }

    private void uploadData() {

        //Using the mail part instead of the uniqueId..
        //removing the @domainName.com from the mail Id....


    }

    private void getData() {

        /*First Step*/
        name = namegetter.getText().toString();
        age = agegetter.getText().toString();
        mail = mailgetter.getText().toString();
        pwd = pwdgetter.getText().toString();
        phone = phgetter.getText().toString();

        //instantiating the object with the strings above to be sent the RTDB...
        signUpData = new SignUpData();

        //Using the setters....
        signUpData.setName(name);
        signUpData.setAge(age);
        signUpData.setPhone(phone);

    }

}
