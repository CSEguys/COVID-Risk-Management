package com.csedevs.covid_riskmangement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.csedevs.covid_riskmangement.databinding.ActivityGetValueBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetValueActivity extends AppCompatActivity {

    ActivityGetValueBinding activityGetValueBinding;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    String name,phone,age;
    BasicData basicData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "Red Colored Component is neccessary", Toast.LENGTH_SHORT).show();
        
        activityGetValueBinding = ActivityGetValueBinding.inflate(getLayoutInflater());
        View view = activityGetValueBinding.getRoot();
        setContentView(view);

        basicData = new BasicData();
        databaseReference = FirebaseDatabase.getInstance().getReference("root/users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        activityGetValueBinding.giveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getvalues();
                databaseReference.child(firebaseUser.getUid()).setValue(basicData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateUI();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });

    }

    private void updateUI() {
        startActivity(new Intent(GetValueActivity.this,HomePageActivity.class));
        finish();
    }

    private void getvalues() {

        //Getting data through the view binder named 'activityGetValueBinding'
        name = activityGetValueBinding.name.getText().toString();
        phone = activityGetValueBinding.phone.getText().toString();
        age = activityGetValueBinding.age.getText().toString();

        //banging it to the created class...
        basicData.setName(name);
        basicData.setPhone(phone);
        basicData.setAge(age);

    }
}
