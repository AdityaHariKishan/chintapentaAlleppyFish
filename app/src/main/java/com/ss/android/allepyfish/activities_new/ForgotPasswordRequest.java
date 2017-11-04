package com.ss.android.allepyfish.activities_new;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.LoginActivity;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;
import com.ss.android.allepyfish.utils.NoDefaultSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordRequest extends AppCompatActivity implements View.OnClickListener {

    String TAG = ForgotPasswordRequest.class.getSimpleName();
    //Declaring EditText
    private EditText editTextEmail;
    private Spinner selectYourRequestTypeSpinner;
    private EditText editTextMessage;

    //Send button
    private Button buttonSend;

    String selectState = "Select your change request";

    ArrayAdapter<CharSequence> selectYourRequestTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_forgot_password_request);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //Initializing the views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        selectYourRequestTypeSpinner = (Spinner) findViewById(R.id.selectYourRequestTypeSpinner);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);

        buttonSend = (Button) findViewById(R.id.buttonSend);

        selectYourRequestTypeAdapter = ArrayAdapter.createFromResource(this, R.array.user_resquests, android.R.layout.simple_spinner_item);
        selectYourRequestTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectYourRequestTypeSpinner.setPrompt(selectState);

        selectYourRequestTypeSpinner.setAdapter((new NoDefaultSpinner(selectYourRequestTypeAdapter, R.layout.user_requests_custom_spinner, this)));

        //Adding click listener
        buttonSend.setOnClickListener(this);
    }


    private void sendEmail() {
        //Getting content for email
        String email = editTextEmail.getText().toString().trim();
        String subject = selectYourRequestTypeSpinner.getSelectedItem().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    @Override
    public void onClick(View v) {
        makeChangeRequest();
    }

    private void makeChangeRequest() {

        final String email = editTextEmail.getText().toString().trim();
        final String subject = selectYourRequestTypeSpinner.getSelectedItem().toString().trim();
        final String message = editTextMessage.getText().toString().trim();


        String tag_string_req = "req_register";

//        pDialog.setMessage("Registering ...");
        final String is_working = "0";
//        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.POST_USER_REQUESTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        Toast.makeText(getApplicationContext(), "Your Request Received Will get back you in soon", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ForgotPasswordRequest.this, LoginActivity.class); // Your list's Intent
                        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                        startActivity(i);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            //            $name, $email, $phone_no, $password, $city, $address_1, $address_2, $profile_pic_url
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_email", email);
                params.put("request_subject", subject);
                params.put("request_reason", message);
                params.put("request_status", "Pending");
                params.put("responded_by", "Pending");


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}