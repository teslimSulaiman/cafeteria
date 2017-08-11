package com.example.user.cafeteria;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChangePassword extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";
    private static final String REGISTER_URL = "http://shark-mobile1990.000webhostapp.com/cafeteria/Riverdale/changePassword.php";
    @Bind(R.id.initial_password) EditText _initialPassword;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_change_password) Button btn_change_password;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
         phoneNumber = getIntent().getStringExtra("phoneNumber");

        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }
        if(!Utility.isNetworkAvailable(getApplicationContext())){

            return;
        }
        btn_change_password.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ChangePassword.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Changing Password...");
        // progressDialog.show();


        String initialPassword = _initialPassword.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        changePassword(phoneNumber,initialPassword,password);
        Log.d(TAG, "signupn: "+ phoneNumber);



    }
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Changing Password failed", Toast.LENGTH_LONG).show();

        btn_change_password.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String initialPassword = _initialPassword.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (initialPassword.isEmpty() || initialPassword.length() < 3) {
            _initialPassword.setError("at least 3 characters");
            valid = false;
        } else {
            _initialPassword.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10  characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    private void changePassword(final String phoneNumber, String password, String newPassword) {

        class ChangePasswordAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;
            RegisterUserClass ruc = new RegisterUserClass();
            final ProgressDialog progressDialog = new ProgressDialog(ChangePassword.this,
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
                        Toast.makeText(getApplicationContext(),"Incorrect Initial Password ",Toast.LENGTH_LONG).show();
                        onSignupFailed();

                    }
                    else if(status.equalsIgnoreCase("2")) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Password successfully changed",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_LONG).show();
                    btn_change_password.setEnabled(true);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                //data.put("name",params[0]);
                data.put("phoneNumber",params[0]);
                data.put("initialPassword",params[1]);
                data.put("newPassword",params[2]);


                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        ChangePasswordAsync ru = new ChangePasswordAsync();
        ru.execute(phoneNumber,password,newPassword);
    }
}
