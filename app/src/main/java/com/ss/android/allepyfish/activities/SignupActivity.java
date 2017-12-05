package com.ss.android.allepyfish.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;
import com.ss.android.allepyfish.utils.NoDefaultSpinner;
import com.ss.android.allepyfish.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;



public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    int PICK_IMAGE_REQUEST = 9999;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private String userChoosenTask;

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private Bitmap bitmap;

//    @BindViews(R.id.input_name)
    EditText _nameText;

//    @BindViews(R.id.input_address)
    EditText _addressText;

//    @BindViews(R.id.input_address2)
    EditText _addressText2;

//    @BindViews(R.id.input_email)
    EditText _emailText;

//    @BindViews(R.id.input_city)
    EditText cityEdt;

//    @BindViews(R.id.input_mobile)
    EditText _mobileText;

//    @BindViews(R.id.input_password)
    EditText _passwordText;

//    @BindViews(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;

//    @BindViews(R.id.input_login_type)
    EditText loginTypeEdt;

//    @BindViews(R.id.btn_signup)
    Button _signupButton;

//    @BindViews(R.id.link_login)
    TextView _loginLink;

//    @BindViews(R.id.loadPicIB)
    ImageButton _loadPicIB;

    Spinner registeredAsSpinner, districtSpinner, stateSpinner;

    String registerAs = "Register As";
    String selectState = "Select your state";

    String selectedStateStr;

    ArrayAdapter<CharSequence> adapterProductUnits, selectStateAdapter, selectdistrictAdapter;

    String districtStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        ButterKnife.BindViews(this);
//        ButterKnife.bind(this);

        _loadPicIB = (ImageButton) findViewById(R.id.loadPicIB);
        _loginLink = (TextView) findViewById(R.id.link_login);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        loginTypeEdt = (EditText) findViewById(R.id.input_login_type);
        _reEnterPasswordText = (EditText) findViewById(R.id.input_reEnterPassword);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _mobileText = (EditText) findViewById(R.id.input_mobile);
        cityEdt = (EditText) findViewById(R.id.input_city);
        _emailText = (EditText) findViewById(R.id.input_email);
        _addressText2 = (EditText) findViewById(R.id.input_address2);
        _addressText = (EditText) findViewById(R.id.input_address);
        _nameText = (EditText) findViewById(R.id.input_name);


        registeredAsSpinner = (Spinner) findViewById(R.id.registeredAsSpinner);
        districtSpinner = (Spinner) findViewById(R.id.districtSpinner);
        stateSpinner = (Spinner) findViewById(R.id.stateSpinner);

        adapterProductUnits = ArrayAdapter.createFromResource(this, R.array.register_as, android.R.layout.simple_spinner_item);
        adapterProductUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registeredAsSpinner.setPrompt(registerAs);

        registeredAsSpinner.setAdapter((new NoDefaultSpinner(adapterProductUnits, R.layout.register_as_custom_spinner, this)));


        selectStateAdapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        selectStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setPrompt(selectState);

        stateSpinner.setAdapter((new NoDefaultSpinner(selectStateAdapter, R.layout.register_state_custom_spinner, this)));

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
                uploadProfilePiic();


            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _loadPicIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();

            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                selectedStateStr = stateSpinner.getSelectedItem().toString().trim();

//                selectedStateStr = stateSpinner.getSelectedItem().toString().trim();

                if(position == 1){
//                    stateSpin = String.valueOf(adapter.getItem(1));
//                    Log.i("Selected Spinner Value ","Value of Spinner is ...."+stateSpin);

                    selectedStateStr = String.valueOf(selectStateAdapter.getItem(0));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();
                    String[] biharStateValues = res.getStringArray(R.array.ap);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignupActivity.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    districtSpinner.setAdapter(dataAdapter);
                }
                if(position == 2){
//                    stateSpin = String.valueOf(adapter.getItem(1));
//                    Log.i("Selected Spinner Value ","Value of Spinner is ...."+stateSpin);

                    selectedStateStr = String.valueOf(selectStateAdapter.getItem(1));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();
                    String[] biharStateValues = res.getStringArray(R.array.Karnataka);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignupActivity.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    districtSpinner.setAdapter(dataAdapter);
                }
                if(position == 3){
//                    stateSpin = String.valueOf(adapter.getItem(1));
//                    Log.i("Selected Spinner Value ","Value of Spinner is ...."+stateSpin);

                    selectedStateStr = String.valueOf(selectStateAdapter.getItem(2));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();
                    String[] biharStateValues = res.getStringArray(R.array.Kerala);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignupActivity.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    districtSpinner.setAdapter(dataAdapter);
                }
                if(position == 4){
//                    stateSpin = String.valueOf(adapter.getItem(1));
//                    Log.i("Selected Spinner Value ","Value of Spinner is ...."+stateSpin);

                    selectedStateStr = String.valueOf(selectStateAdapter.getItem(3));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();
                    String[] biharStateValues = res.getStringArray(R.array.Lakshadweep);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignupActivity.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    districtSpinner.setAdapter(dataAdapter);
                }
                if(position == 5){
//                    stateSpin = String.valueOf(adapter.getItem(1));
//                    Log.i("Selected Spinner Value ","Value of Spinner is ...."+stateSpin);

                    selectedStateStr = String.valueOf(selectStateAdapter.getItem(4));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();
                    String[] biharStateValues = res.getStringArray(R.array.Puducherry);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignupActivity.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    districtSpinner.setAdapter(dataAdapter);
                }
                if(position == 6){
//                    stateSpin = String.valueOf(adapter.getItem(1));
//                    Log.i("Selected Spinner Value ","Value of Spinner is ...."+stateSpin);

                    selectedStateStr = String.valueOf(selectStateAdapter.getItem(5));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();
                    String[] biharStateValues = res.getStringArray(R.array.TamilNadu);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignupActivity.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    districtSpinner.setAdapter(dataAdapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        districtStr = districtSpinner.getSelectedItem().toString().trim();

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                districtStr = districtSpinner.getSelectedItem().toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

//        marshMallowPermission = new MarshMallowPermission(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(SignupActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
//                        galleryIntent();
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void uploadProfilePiic() {

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.UPLOAD_PROFILE_PIC_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
//                        loading.dismiss();
                        Log.i("Response 123", "Response in Volley " + s);

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
//                        Toast.makeText(SignupActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                        Log.i("Error 123", "Error in Volley " + volleyError.getMessage().toString().trim());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = _nameText.getText().toString().trim();

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String phone_no = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String address2 = _addressText2.getText().toString();
//        String login_type = loginTypeEdt.getText().toString().trim();
        String login_type = registeredAsSpinner.getSelectedItem().toString().trim();
        String cityTxt = cityEdt.getText().toString().trim();


        // TODO: Implement your own signup logic here.
        //$name, $email, $password, $, $city, $address_1, $address_2, $profile_pic_url, $
        registerUser(name, email, password, phone_no, address, address2, selectedStateStr, districtStr, login_type, cityTxt);


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
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

    private void registerUser(final String name, final String email, final String password, final String phone_no, final String address, final String address2, final String selectedStateStr, final String districtStr, final String login_type, final String cityTxt) {
//    private void registerUser() {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

//        pDialog.setMessage("Registering ...");
        final String is_working = "0";
//        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

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
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String phone_no = user.getString("phone_no");
                        String is_Working = user.getString("city");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
//                        db.addUser(name, phone_no, uid, is_Working, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
//                        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            //            $name, $email, $phone_no, $password, $city, $address_1, $address_2, $profile_pic_url
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("phone_no", phone_no);
                params.put("email", email);
                params.put("password", password);
                params.put("city", cityTxt);
                params.put("address_1", address);
                params.put("address_2", address2);
                params.put("state", selectedStateStr);
                params.put("district", districtStr);
                params.put("profile_pic_url", "http://ec2-54-146-152-155.compute-1.amazonaws.com/alleppyfish/uploads/" + name + ".png");
                params.put("login_type", login_type);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        _loadPicIB.setImageBitmap(bitmap);
    }

    private void onSelectFromGalleryResult(Intent data) {

//        Bitmap bm=null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        _loadPicIB.setImageBitmap(bitmap);
    }
}