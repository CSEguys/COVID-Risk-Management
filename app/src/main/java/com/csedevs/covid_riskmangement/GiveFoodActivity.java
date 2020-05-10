package com.csedevs.covid_riskmangement;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GiveFoodActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GeoFire geoFire;
    private DatabaseReference databaseReference,ref;

    private Double Lat = VolunteerFragment.Lat, Lan = VolunteerFragment.Lan;

    private String keys;
    private List<LatLng> twokm;
    private List<LatLng> fourkm;
    private List<LatLng> otherkm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_food);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        databaseReference = FirebaseDatabase.getInstance().getReference("root/needFood");
        ref = FirebaseDatabase.getInstance().getReference("root/foodies");
        geoFire = new GeoFire(databaseReference);
        getneeds();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getneeds() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    keys = data.getKey();
                    geoFire.getLocation(keys, new LocationCallback() {
                        @Override
                        public void onLocationResult(String key, GeoLocation location) {

                            Location here  = new Location("");
                            here.setLatitude(Lat);
                            here.setLongitude(Lan);

                            Location other = new Location("");
                            other.setLatitude(location.latitude);
                            other.setLongitude(location.longitude);

                            float dist = here.distanceTo(other);
                            if(dist<=0.2)twokm.add(new LatLng(Lat,Lan));
                            else if(dist<=0.4)fourkm.add(new LatLng(Lat,Lan));
                            else if(dist>0.4)fourkm.add(new LatLng(Lat,Lan));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng current =new LatLng(VolunteerFragment.Lat,VolunteerFragment.Lan);
        mMap.addMarker(new MarkerOptions().position(current).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        Circle circle = mMap.addCircle(new CircleOptions()
                    .strokeColor(Color.RED)
                    .fillColor(Color.TRANSPARENT)
                    .radius(1000)
                    .center(current));

    }
}
