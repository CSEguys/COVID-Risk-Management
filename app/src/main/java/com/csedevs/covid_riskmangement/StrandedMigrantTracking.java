package com.csedevs.covid_riskmangement;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StrandedMigrantTracking extends Fragment {

    private EditText details;
    private Button enroll;
    private Double Lat,Lon;
    
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private GeoFire geoFire;
    private String data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stranded_migrant_tracking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        
        fusedLocationProviderClient = new FusedLocationProviderClient(getActivity());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("root/stranded");
        geoFire = new GeoFire(databaseReference);
        
        getLocation();
        
        enroll = view.findViewById(R.id.enroll);
        details = view.findViewById(R.id.details);


        
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = details.getText().toString();
                geoFire.setLocation(firebaseUser.getUid(), new GeoLocation(Lat, Lon), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if(!data.isEmpty()){
                            databaseReference.child("detail").setValue(data);
                            details.setText(" ");
                            details.setHint("Give some details if you want!..");
                        }
                        Toast.makeText(getActivity(), "Updated Your Whereabouts", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        
    }

    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Lat = location.getLatitude();
                Lon = location.getLongitude();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                
            }
        });
    }
}
