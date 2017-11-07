package com.ss.android.allepyfish.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";

    Button submmitPasswordBtn;

    EditText fpEmailIdEdt,fpPasswordEdt;

    Intent intent;

    String myMailIdPWD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        myMailIdPWD = intent.getStringExtra("myMailId");

        fpEmailIdEdt = (EditText)findViewById(R.id.fpEmailIdEdt);
        fpEmailIdEdt.setEnabled(false);
        fpEmailIdEdt.setText(myMailIdPWD);
        fpPasswordEdt = (EditText)findViewById(R.id.fpPasswordEdt);

        submmitPasswordBtn = (Button)findViewById(R.id.submmitPasswordBtn);
        submmitPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String fpEmailIdStr = fpEmailIdEdt.getText().toString().trim();
//                String fpEmailIdStr = myMailIdPWD.trim();
                String fpEmailIdStr = fpEmailIdEdt.getText().toString().trim();
                String fpPasswordStr = fpPasswordEdt.getText().toString().trim();
                changePwd(fpEmailIdStr,fpPasswordStr);
            }
        });
    }

    private void changePwd(final String fpEmailIdStr, final String fpPasswordStr) {
        String tag_string_req = "req_login";

//        pDialog.setMessage("Logging in ...");
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FORGOT_PWD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
//                hideDialog();

                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                finish();
               /* try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String phoneNo = user.getString("phone_no");
                        String loginType = user.getString("login_type");
                        String created_at = user
                                .getString("created_at");

                        if(loginType.equals("Manager")){
                            startActivity(new Intent(ForgotPasswordActivity.this,ManagerLandingScreen.class));
                            finish();
                        }else if (loginType.equals("Fisher Man")){
                            startActivity(new Intent(ForgotPasswordActivity.this,MainActivity.class));
                            finish();
                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }*/

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", fpEmailIdStr);
                params.put("password", fpPasswordStr);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        finish();
        return true;
    }
}