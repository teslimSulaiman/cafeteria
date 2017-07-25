package com.example.user.cafeteria;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 7/24/2017.
 */

public class SignUpLoader extends AsyncTaskLoader<List<User>> {
    private static final String LOG_TAG = "errors";
    private URL REQUEST_URL;

    public SignUpLoader(Context context, URL url) {
        super(context);
        REQUEST_URL = url;
    }

    @Override
    public List<User> loadInBackground() {
        URL url = (REQUEST_URL);
        Log.d(LOG_TAG, REQUEST_URL.toString()+" got here");
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = Utility.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to the server", e);
        }
        // Extract relevant fields from the JSON response and create an {@link Review} object
        return extractFeatureFromJson(jsonResponse);
    }

    private List<User> extractFeatureFromJson(String bookJSON) {
        List<User> users = new ArrayList<>() ;
        final String name = "name";
        final String phoneNumber = "phoneNumber";
        final String code = "code";
        final String status = "status";
        final String password = "password";


        //if json string is empty or null return early
        try {
        JSONObject jObj = new JSONObject(bookJSON);
//
//            String userName = jObj.getString(name);

           // JSONObject user = jObj.getJSONObject("user");
            String username = jObj.getString(name);
            String userPhoneNumber = jObj.getString(phoneNumber);
            String userCode = jObj.getString(code);
            String userPassword = jObj.getString(password);
            String userStatus = jObj.getString(status);



                // Create a trailer {@link Trailer} object

                users.add(new User(userPhoneNumber, username,userCode,userPassword,userStatus));

           // }
        } catch (JSONException e) {
            //Utility. setServerStatus(getContext(),Utility.LOCATION_STATUS_SERVER_INVALID);
          //  Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return users;
    }


}
