package com.csedevs.covid_riskmangement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class volunteerActivity extends AppCompatActivity {

    Button signup;
    TextInputLayout mail,pwd;
    String Mail, Pwd;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        mail = findViewById(R.id.mail);
        pwd = findViewById(R.id.pwd);
        signup = findViewById(R.id.signup);

        databaseReference = FirebaseDatabase.getInstance().getReference("Signers/volunteers");
        firebaseAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting the texts from the edit text fields...
                Mail=mail.getEditText().getText().toString();
                Pwd=pwd.getEditText().getText().toString();

                if(!Mail.isEmpty() && !Pwd.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(Mail, Pwd)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        //on signup success..
                                        startActivity(new Intent(volunteerActivity.this, VolunteerOptionsActivity.class));
                                    }else{
                                        Toast.makeText(volunteerActivity.this, "SignUp failed", Toast.LENGTH_SHORT).show();
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
}
