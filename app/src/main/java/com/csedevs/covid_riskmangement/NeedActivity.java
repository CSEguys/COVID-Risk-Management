package com.csedevs.covid_riskmangement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NeedActivity extends AppCompatActivity  {

    Button button, button2;

    String Lat, Lan;

    private FusedLocationProviderClient fusedLocationClient;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("root/FoodNeeds");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //toGet the Location...Using FusedLocationAPI
        getLocation();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                latlng l = new latlng();
                l.setLan(Lan);
                l.setLat(Lat);
                databaseReference.child(firebaseUser.getUid()).setValue(l);

            }
        });

    }

    private void getLocation() {

        //To create the permission Dialog...if it has no permission...
        ActivityCompat.requestPermissions(NeedActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Lat = Double.toString(location.getLatitude());
                        Lan = Double.toString(location.getLongitude());
                    }
                });
    }

    class latlng {
        String Lat, Lan;

        public String getLat() {
            return Lat;
        }

        public void setLat(String lat) {
            Lat = lat;
        }

        public String getLan() {
            return Lan;
        }

        public void setLan(String lan) {
            Lan = lan;
        }
    }

}
