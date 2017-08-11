package com.example.user.cafeteria;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by USER on 7/29/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
       // Log.d("not", "onMessageReceived: "+ remoteMessage.getNotification().getBody());
       // showNotification(remoteMessage.getNotification().get("message"),remoteMessage.getData().get("location")
       // ,remoteMessage.getData().get("headline"));


        Map<String, String> data = remoteMessage.getData();
        String message = data.get("message");
        String location = data.get("location");
        String headline = data.get("headline");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        double lat = Double.longBitsToDouble(prefs.getLong(getApplicationContext().getString(R.string.pref_location_key)
                , Double.doubleToLongBits(-10.0)));
        if(lat != -10.0){
            if(lat < 40.85 && location.equalsIgnoreCase(Utility.ACADEMY_HIGH_SCHOOL )){
                showNotification(message,location,headline);
            }
            if(lat > 40.85 && location.equalsIgnoreCase(Utility.HIGH_SCHOOL)){

                showNotification(message,location,headline);
            }
        }


       // showNotification(message,location,headline);

    }

    private void showNotification(String message, String location, String headline) {

        Intent i = new Intent(this,InfoActivity.class);
        i.putExtra("location",location);
        i.putExtra("headline",headline);
        i.putExtra("message",message);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
//        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
//        wakeLock.acquire();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(getBaseContext().getString(R.string.app_name))
                .setContentText(headline)
                .setSmallIcon(R.drawable.info)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
        //googleApiClient.disconnect();
    }

//    @Override
//    public void  handleIntent(Intent intent){
//
//        Log.d("fcm", "handleIntent: "+ intent.getExtras().get("message"));
//    }



}