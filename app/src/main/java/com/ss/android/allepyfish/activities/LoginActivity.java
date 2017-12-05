package com.ss.android.allepyfish.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.admin.AdminActivity;
import com.ss.android.allepyfish.activities.office_boy.OfficeBoyLandingScreen;
import com.ss.android.allepyfish.activities_new.FisherManScreen;
import com.ss.android.allepyfish.activities_new.ForgotPasswordRequest;
import com.ss.android.allepyfish.activities_new.ManagerMyUploads;
import com.ss.android.allepyfish.activities_new.ManagerOrderRequest;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;
import com.ss.android.allepyfish.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );

    //    @BindViews(R.id.input_email)
    EditText _emailText;

    //    @BindViews(R.id.input_password)
    EditText _passwordText;

    //    @BindViews(R.id.btn_login)
    Button _loginButton;

    //    @BindViews(R.id.btn_forgot_password)
    Button btn_login_mgnr;

    //    @BindViews(R.id.link_signup)
    Button _signupLink;

    private SessionManager session;
    private SQLiteHandler db;

    String loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        ButterKnife.bind(this);

        // SQLite database handler
        _loginButton = (Button) findViewById(R.id.btn_login);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _emailText = (EditText) findViewById(R.id.input_email);
        btn_login_mgnr = (Button) findViewById(R.id.btn_forgot_password);
        _signupLink = (Button) findViewById(R.id.link_signup);


        db = new SQLiteHandler(getApplicationContext());

// Session manager
        session = new SessionManager(getApplicationContext());

// Check if user is already logged in or not
        Intent intentLogin;
        if (session.isLoggedIn()) {

            HashMap<String, String> user = db.getUserDetails();

            String loginTypeStr = user.get("loginType").toString().trim();

            if (loginTypeStr.equals("Fisher Man")) {

//                intentLogin = new Intent(LoginActivity.this, MainActivity.class);
                intentLogin = new Intent(LoginActivity.this, FisherManScreen.class); // temp
//                intentLogin = new Intent(LoginActivity.this, ManagerLandingScreen.class);
                startActivity(intentLogin);
                finish();
            }
            if (loginTypeStr.equals("Manager")) {

//                intentLogin = new Intent(LoginActivity.this, ManagerLandingScreenItemCount.class);
//                intentLogin = new Intent(LoginActivity.this, ManagerOrderRequest.class);
                intentLogin = new Intent(LoginActivity.this, ManagerMyUploads.class);
                startActivity(intentLogin);
                finish();
            }
            if (loginTypeStr.equals("Office Boy")) {
//                intentLogin = new Intent(LoginActivity.this, OfficeBoyLandingScreen.class);
                intentLogin = new Intent(LoginActivity.this, FisherManScreen.class);
                startActivity(intentLogin);
                finish();
            }
            if (loginTypeStr.equals("Fisher Man")) {
//                intentLogin = new Intent(LoginActivity.this, OfficeBoyLandingScreen.class);
                intentLogin = new Intent(LoginActivity.this, FisherManScreen.class);
                startActivity(intentLogin);
                finish();
            }
            if (loginTypeStr.equals("Admin")) {
//                intentLogin = new Intent(LoginActivity.this, OfficeBoyLandingScreen.class);
                intentLogin = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intentLogin);
                finish();
            }

            Log.i(TAG, "Pull Name :: " + loginTypeStr + "\n Email is ::: ");
        }




       /* btn_login_mgnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this,ManagerLandingScreen.class));
//                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                startActivity(new Intent(LoginActivity.this,ForgotPasswordRequest.class));
//                sendEmail();

            }
        });*/

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                login();

                String emailStr = _emailText.getText().toString().trim();
                String pwdStr = _passwordText.getText().toString().trim();

                if (!(emailStr.length() == 0)) {

                    if (checkEmail(emailStr)) {
                        if (!(pwdStr.length() == 0)) {
                            checkLogin(emailStr, pwdStr);
                        } else {
                            _passwordText.setError("Password Should not be empty");
                        }
                    } else {
                        _emailText.setError("Please Enter Proper Email Id");
                    }

                } else {
                    _emailText.setError("Email Id Should empty");
                }

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private boolean checkEmail(String emailStr) {
        return EMAIL_ADDRESS_PATTERN.matcher(emailStr).matches();
    }

    private void sendEmail() {

        Log.i("Send email", "");
        String[] TO = {"alleppeyfishsales@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear Team, I've forgotted my password kindly share my password here is my login Id ::  ");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(LoginActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private void checkLogin(final String emailStr, final String pwdStr) {

        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    Log.i(TAG, "The Erro Value is :: " + error);

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String phoneNo = user.getString("phone_no");
                        loginType = user.getString("login_type");
                        String citySQL = user.getString("city");
                        String stateSQL = user.getString("state");
                        String districtSql = user.getString("district");
                        String profile_pic = user.getString("profile_pic_url");
                        String created_at = user
                                .getString("created_at");

                        db.addUser(name, email, phoneNo, loginType, stateSQL, districtSql, citySQL, created_at, profile_pic);
//                        db.addUser(name, email, uid, loginType, created_at);

                        if (loginType.equals("Manager")) {

                            startActivity(new Intent(LoginActivity.this, ManagerMyUploads.class));
                            finish();
                        } else if (loginType.equals("Fisher Man")) {

                            startActivity(new Intent(LoginActivity.this, FisherManScreen.class));
                            finish();
                        } else if (loginType.equals("Office Boy")) {

                            startActivity(new Intent(LoginActivity.this, FisherManScreen.class));
                            finish();
                        }
                        if (loginType.equals("Admin")) {

//                intentLogin = new Intent(LoginActivity.this, OfficeBoyLandingScreen.class);
                            Intent intentLogin = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intentLogin);
                            finish();
                        }


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");

                        JSONObject jObj1 = new JSONObject(response);
                        String message = jObj1.getString("error_msg");

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        Log.i(TAG, "Login Error 298 " + message);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();

                    try {
                        JSONObject jsonObject = new JSONObject((response));
                        String erroMsg = jsonObject.getString("error_msg");
                        Log.i(TAG, "Login Error MSG :: 298 :: " + erroMsg);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                   /* String errorMsg = jObj.getString("error_msg");
                    Log.i(TAG,"Login Error "+message);*/
                }

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

                params.put("email", emailStr);
                params.put("password", pwdStr);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
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
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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
}