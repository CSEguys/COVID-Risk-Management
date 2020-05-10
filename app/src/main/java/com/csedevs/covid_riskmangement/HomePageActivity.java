package com.csedevs.covid_riskmangement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.csedevs.covid_riskmangement.databinding.ActivityHomePageBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityHomePageBinding binder;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private List<String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binder = ActivityHomePageBinding.inflate(getLayoutInflater());
        View view = binder.getRoot();
        setContentView(view);

        //As the toolbar is removed setting an custom one...
        setSupportActionBar(binder.toolbar);

        binder.navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binder.drawerlayout, binder.toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        binder.drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            binder.navigationView.setCheckedItem(R.id.homeFragment);
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("root/users/"+firebaseUser.getUid());
        userData = new ArrayList<String>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    userData.add(data.getValue().toString());
                }
                TextView textView = findViewById(R.id.namer);
                textView.setText(userData.get(1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeFragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
                binder.drawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.volunteerFragment:
                if(getPref()){
                    //to assign him as an volunteer in out RTdatabase...
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("root/volunteers");
                    databaseReference.child(firebaseUser.getUid()).setValue("volunteer!");

                    //placing the fragemtn as needed...
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new VolunteerFragment()).commit();
                    binder.drawerlayout.closeDrawer(GravityCompat.START);
                    break;
                }
                break;
            case R.id.laborShelterMapping:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LaborShelterMapping()).commit();
                binder.drawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.strandedMigrantTracking:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StrandedMigrantTracking()).commit();
                binder.drawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.needFood:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NeedFood()).commit();
                binder.drawerlayout.closeDrawer(GravityCompat.START);
                break;
        }

        return true;
    }

    private boolean getPref() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return true;
    }

    private boolean getConfession() {
        final boolean[] answer = {true};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you ready to be a Volunteer??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                answer[0] =!answer[0];
                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(getString(R.string.volunteer),1);
                editor.commit();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                answer[0] = false;
            }
        });
        builder.show();

        return answer[0];

    }

    @Override
    public void onBackPressed() {
        if(binder.drawerlayout.isDrawerOpen(GravityCompat.START)){
            binder.drawerlayout.closeDrawer(GravityCompat.START);
        }else super.onBackPressed();
    }
}
