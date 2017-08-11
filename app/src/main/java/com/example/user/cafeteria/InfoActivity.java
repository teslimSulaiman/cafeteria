package com.example.user.cafeteria;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class InfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    double[] schoolA = {40.9067022,-73.9050421};
    double[] schoolB = {40.7699135,-73.7074416};
    String schoolAAddress ="503 W 259th St, Bronx, NY 10471, USA";
    String schoolBAddress ="400 N Service Rd, Great Neck, NY 11020, USA";
    TextView addressTextView, messageTextView, headlineTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        addressTextView = (TextView) findViewById(R.id.address);
        messageTextView = (TextView) findViewById(R.id.message);
        headlineTextView = (TextView) findViewById(R.id.headline);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        String location = intent.getStringExtra("location");
        String headline = intent.getStringExtra("headline");
        String address = getAddress(location);

        addressTextView.setText(address);
        messageTextView.setText(message);
        headlineTextView.setText(headline);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Intent intent = getIntent();
        if (intent != null) {

            String location = intent.getStringExtra("location");
          double[] school =  getLocationCoordinates(location);

            LatLng sydney ;
            if(!Utility.isNetworkAvailable(getApplicationContext())){
                 sydney = new LatLng(school[0], school[1]);
            }
            else {

                 sydney = getLocationFromAddress(getApplicationContext(),getLocationString(location));
            }

      // LatLng sydney = getLocationFromAddress(this,"503 W 259th St, Bronx, NY 10471, USA");
       // 40.907364,-73.905448,18

        //map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        map.addMarker(new MarkerOptions()
                .title("Riverdale")
                .snippet("The Riverdale cafeteria.")
                .position(sydney));
        }
    }
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    private double[] getLocationCoordinates(String location){
        if(location.equalsIgnoreCase(Utility.ACADEMY_HIGH_SCHOOL)){
            return schoolB;
        }
        else return schoolA;

    }

    private String getLocationString(String location){
        if(location.equalsIgnoreCase(Utility.ACADEMY_HIGH_SCHOOL)){
            return schoolBAddress;
        }
        else return schoolAAddress;

    }
    private String getAddress(String location){
        if(location.equalsIgnoreCase(Utility.ACADEMY_HIGH_SCHOOL)){
            return Utility.ACADEMY_HIGH_SCHOOL;
        }
        else return Utility.ADDRESS_HIGH_SCHOOL;

    }
}

    //onCreateOptionsMenu: no changes
    //onOptionsItemSelected: no changes
