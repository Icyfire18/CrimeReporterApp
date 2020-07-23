package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity<mDatabase> extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button button;
    private LocationManager locationManager;
    private LocationListener listener;
    private Button button_check;
    private FloatingActionButton button_nav;
    //location



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //location


        //location edit ends
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button = (Button) findViewById(R.id.button_report);
        button.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               startActivity(new Intent(MapsActivity.this,Signup_form.class));

           }

        });
        button_check = (Button) findViewById(R.id.button_check);
        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(MapsActivity.this, history.class);
                startActivity(inte);
            }
        });
        button_nav = (FloatingActionButton)findViewById(R.id.button_nav);
        button_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(MapsActivity.this,MapsActivity.class);
                startActivity(inte);
                finish();
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

        //location
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        //location ends

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(11.076, 76.99);
        LatLng sydney = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));


        UiSettings mapSettings;
        mapSettings = mMap.getUiSettings();
        mapSettings.setRotateGesturesEnabled(true);
        //mapSettings.setZoomControlsEnabled(true);
        mapSettings.setCompassEnabled(true);
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
    }


}
