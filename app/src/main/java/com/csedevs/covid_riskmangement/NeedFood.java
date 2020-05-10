package com.csedevs.covid_riskmangement;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.csedevs.covid_riskmangement.databinding.FragmentNeedFoodBinding;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NeedFood extends Fragment implements View.OnClickListener {

    FragmentNeedFoodBinding binding;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private GeoFire geoFire;
    private DatabaseReference databaseReference,ref;
    private FirebaseUser firebaseUser;

    static Double Lat,Lan;

    private Button myloc, diffloc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_need_food, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myloc = view.findViewById(R.id.myloc);
        diffloc = view.findViewById(R.id.diffloc);

        databaseReference = FirebaseDatabase.getInstance().getReference("root/needFood");
        ref = FirebaseDatabase.getInstance().getReference("root/foodies");
        geoFire = new GeoFire(databaseReference);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getCurrentLoc();

        myloc.setOnClickListener(this);
        diffloc.setOnClickListener(this);

    }

    private void getCurrentLoc() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.e("HEY","seeing if it gets here...");
                Lat = location.getLatitude();
                Lan = location.getLongitude();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myloc:
                uploadLoc();
                break;
            case R.id.diffloc:
                startActivity(new Intent(getActivity(),DiffLocACtivity.class));
                break;
        }

    }

    private void uploadLoc() {
        ref.child(firebaseUser.getUid()).setValue(firebaseUser.getEmail());
        geoFire.setLocation(firebaseUser.getUid(), new GeoLocation(Lat,Lan), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Log.e("HHHHHHEY","seeing if it gets here...");
                Toast.makeText(getActivity(), "Uploaded your location...", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
