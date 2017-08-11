package com.example.user.cafeteria;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        SharedPreferences.OnSharedPreferenceChangeListener{


    //    @Bind(R.id.get_barcode)
//    Button getBarcodeButton;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final String TAG = "main";
    @Bind(R.id.tvNumber)
    TextView tvNumber;

    @Bind(R.id.ivBarcode)
    ImageView ivBarcode;
    String codeNumber;
    @Bind(R.id.tvText)
    TextView tvText;
    private final String LOG_TAG = "Cafeteria";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final String PLACEHOLDER_TEXT = "This is your code ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


//        LinearLayout l = new LinearLayout(this);
//        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        l.setOrientation(LinearLayout.VERTICAL);
//
//        setContentView(l);
        Intent intent = getIntent();
        if (intent != null) {
            String code = intent.getStringExtra("code");
            String label = intent.getStringExtra("name");
            tvText.setText(PLACEHOLDER_TEXT + label);

            // barcode data
            String barcode_data = "123456";

            // barcode image
            Bitmap bitmap = null;
            // ImageView iv = new ImageView(this);

            try {

                bitmap = encodeAsBitmap(code, BarcodeFormat.CODE_128, 900, 700);
                ivBarcode.setImageBitmap(bitmap);

            } catch (WriterException e) {
                e.printStackTrace();
            }

            //l.addView(iv);

            //barcode text
//        TextView tv = new TextView(this);
//        tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tvNumber.setText(code);

            // l.addView(tv);


        }


    }

    /**************************************************************
     * getting from com.google.zxing.client.android.encode.QRCodeEncoder
     *
     * See the sites below
     * http://code.google.com/p/zxing/
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/EncodeActivity.java
     * http://code.google.com/p/zxing/source/browse/trunk/android/src/com/google/zxing/client/android/encode/QRCodeEncoder.java
     */

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Intent intent = getIntent();
        if (intent != null) {
             codeNumber = intent.getStringExtra("phoneNumber");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            String phoneNumber = getIntent().getStringExtra("phoneNumber");
            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
            intent.putExtra("phoneNumber",phoneNumber);
            Log.d(TAG, "onOptionsItemSelected: "+phoneNumber);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_contact) {
            Intent intent = new Intent(getApplicationContext(), Contact.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG_TAG, "connected");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10); // Update location every second


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(LOG_TAG, "onLocationChanged");

        Log.i(LOG_TAG, location.toString());
        //txtOutput.setText(location.toString());

       // tvNumber.setText(Double.toString(location.getLongitude()));
        setLocationStatus(this, location.getLatitude());
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        Log.i(LOG_TAG, "started");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
        Log.i(LOG_TAG, "stopped");
    }
    @Override
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
    public void setLocationStatus(Context c, double lat){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        if(lat > 4.0) {
            SharedPreferences.Editor spe = sp.edit();
            spe.putLong(c.getString(R.string.pref_location_key), Double.doubleToRawLongBits(lat));
            spe.apply();
        }
    }

}