package com.example.user.cafeteria;

/**
 * Created by USER on 7/21/2017.
 */



import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;

    public class LoginActivity extends AppCompatActivity  {
        private static final String TAG = "LoginActivity";
        private static final int REQUEST_SIGNUP = 0;
        private static final String REGISTER_URL = "http://shark-mobile1990.000webhostapp.com/cafeteria/Riverdale/login.php";

        @Bind(R.id.input_email)
        EditText _emailText;
        @Bind(R.id.input_password)
        EditText _passwordText;
        @Bind(R.id.btn_login)
        Button _loginButton;
        @Bind(R.id.link_signup)
        TextView _signupLink;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);

            FirebaseMessaging.getInstance().subscribeToTopic("info");
            FirebaseInstanceId.getInstance().getToken();

            _loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });

            _signupLink.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                    finish();
                    overridePendingTransition(R.transition.push_left_in, R.transition.push_left_out);
                }
            });
        }

        public void login() {
            Log.d(TAG, "Login");

            if (!validate()) {
                onLoginFailed();
                return;
            }
            if(!Utility.isNetworkAvailable(getApplicationContext())){
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
                return;
            }

            _loginButton.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            //progressDialog.show();

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            // TODO: Implement your own authentication logic here.


            login(email,password);

//            new android.os.Handler().postDelayed(
//                    new Runnable() {
//                        public void run() {
//                            // On complete call either onLoginSuccess or onLoginFailed
//                            onLoginSuccess();
//                            // onLoginFailed();
//                            progressDialog.dismiss();
//                        }
//                    }, 3000);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_SIGNUP) {
                if (resultCode == RESULT_OK) {

                    // TODO: Implement successful signup logic here
                    // By default we just finish the Activity and log them in automatically
                    this.finish();
                }
            }
        }

        @Override
        public void onBackPressed() {
            // Disable going back to the MainActivity
            moveTaskToBack(true);
        }

        public void onLoginSuccess() {
            _loginButton.setEnabled(true);
            finish();
        }

        public void onLoginFailed() {
            //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

            _loginButton.setEnabled(true);
        }

        public boolean validate() {
            boolean valid = true;

            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();

            if (email.isEmpty()) {
                _emailText.setError("enter a valid email address");
                valid = false;
            } else {
                _emailText.setError(null);
            }

            if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                _passwordText.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                _passwordText.setError(null);
            }

            return valid;
        }

        private void login(final String phoneNumber, String password) {

            class LoginAsync extends AsyncTask<String, Void, String> {

                private Dialog loadingDialog;
                RegisterUserClass ruc = new RegisterUserClass();
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                        R.style.AppTheme_Dark_Dialog);


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    // loading = ProgressDialog.show(SignupActivity.this, "Please Wait",null, true, true);

                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    progressDialog.dismiss();
                    try {
                        JSONObject jObj = new JSONObject(s);
                        String status = jObj.getString("status");
                        String name = jObj.getString("name");
                        String code = jObj.getString("code");

                        if (status.equalsIgnoreCase("1")){
                            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                            onLoginFailed();

                        }
                        else if(status.equalsIgnoreCase("2")) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("code",code);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"user login",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"Server error",Toast.LENGTH_LONG).show();
                        _loginButton.setEnabled(true);
                    }

//                    progressDialog.dismiss();
//                    if (s.equalsIgnoreCase("user login")){
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.putExtra("name","smith");
//                        intent.putExtra("code","123456");
//                        startActivity(intent);
//                    }
//                    else {
//                        onLoginFailed();
//                        s = "login failed";
//                    }
//                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(String... params) {

                    HashMap<String, String> data = new HashMap<String,String>();
                    //data.put("name",params[0]);
                    data.put("phoneNumber",params[0]);
                    data.put("password",params[1]);
                    // data.put("email",params[3]);

                    String result = ruc.sendPostRequest(REGISTER_URL,data);

                    return  result;
                }
            }

            LoginAsync ru = new LoginAsync();
            ru.execute(phoneNumber,password);
        }

        @Override
        protected void onResume() {
            super.onResume();
            _loginButton.setEnabled(true);
            _emailText.setText("");
            _passwordText.setText("");
        }
    }



