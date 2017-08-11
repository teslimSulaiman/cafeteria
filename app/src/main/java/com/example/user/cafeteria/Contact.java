package com.example.user.cafeteria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Contact extends AppCompatActivity implements OnMapReadyCallback {

    TextView addressTextView, messageTextView, headlineTextView, headlineTextView2, messageTextView2;
    String schoolAAddress ="503 W 259th St, Bronx, NY 10471, USA";
    String schoolBAddress ="400 N Service Rd, Great Neck, NY 11020, USA";
    double[] schoolA = {40.9067022,-73.9050421};
    double[] schoolB = {40.7699135,-73.7074416};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        addressTextView = (TextView) findViewById(R.id.address);
        messageTextView = (TextView) findViewById(R.id.message);
        headlineTextView = (TextView) findViewById(R.id.headline);
        messageTextView2 = (TextView) findViewById(R.id.message2);
        headlineTextView2 = (TextView) findViewById(R.id.headline2);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Intent intent = getIntent();
        if (intent != null) {

//            String location = intent.getStringExtra("location");
//            double[] school =  getLocationCoordinates(location);

            LatLng sydney ;
            if(!Utility.isNetworkAvailable(getApplicationContext())){
                sydney = new LatLng(schoolA[0], schoolA[1]);
            }
            else {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        double lat = Double.longBitsToDouble(prefs.getLong(getApplicationContext().getString(R.string.pref_location_key)
                , Double.doubleToLongBits(-10.0)));
                    if(lat > 40.85  && lat != -10.0){

                        sydney = getLocationFromAddress(getApplicationContext(),schoolAAddress);
                        addressTextView.setText(schoolAAddress);

                     }else if(lat < 40.85 && lat != -10.0){
                        sydney = getLocationFromAddress(getApplicationContext(),schoolBAddress);
                        addressTextView.setText(schoolBAddress);

                    }else {
                        sydney = getLocationFromAddress(getApplicationContext(),schoolAAddress);
                    }

            }


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
