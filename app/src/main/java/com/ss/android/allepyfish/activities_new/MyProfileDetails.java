package com.ss.android.allepyfish.activities_new;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.ForgotPasswordActivity;
import com.ss.android.allepyfish.activities.LoginActivity;
import com.ss.android.allepyfish.activities.SignupActivity;
import com.ss.android.allepyfish.activities.admin.AdminActivity;
import com.ss.android.allepyfish.fragments.MyProfile;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;
import com.ss.android.allepyfish.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MyProfileDetails extends AppCompatActivity {

    String TAG = MyProfileDetails.class.getSimpleName();

    String userName;
    String userEmail;
    String profile_pic;

    SQLiteHandler db;
    ImageView profilePicFMIV;
    ImageButton uploadNewProfilePic;
    private String userChoosenTask;
    private Bitmap bitmap;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    LinearLayout userContactNumberLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profilePicFMIV = (ImageView) findViewById(R.id.profilePicFMIV);
        uploadNewProfilePic = (ImageButton) findViewById(R.id.uploadNewProfilePic);

        userContactNumberLayout = (LinearLayout)findViewById(R.id.userContactNumberLayout);

        profilePicFMIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                uploadNewProfilePic.setVisibility(View.VISIBLE);
            }
        });

        uploadNewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePiic();
            }
        });
        db = new SQLiteHandler(this);

        TextView myProfileFManTV = (TextView) findViewById(R.id.myProfileFManTV);
        TextView myProfileuserEmailTV = (TextView) findViewById(R.id.myProfileuserEmailTV);
        TextView myProfileuserPhoneTV = (TextView) findViewById(R.id.myProfileuserPhoneTV);


        String userPhoneNo = null;
        List<ContactInfo> contacts = db.getAllContacts();

        for (ContactInfo cn : contacts) {
            userName = cn.getName();
            userEmail = cn.getEmail();
            userPhoneNo = cn.getPhone_no();
            profile_pic = cn.getprofile_pic_url();
            // Writing Contacts to log
            Log.d("Name: userName :: ", userName + " " + userEmail + " ProfilePic URL " + profile_pic);
        }

        myProfileFManTV.setText(userName);
        myProfileuserEmailTV.setText(userEmail);
        myProfileuserPhoneTV.setText(userPhoneNo);
        String s1 = profile_pic;
//        Picasso.with(getContext()).load("http://"+s1).into(profilePicFMIV);
//        Picasso.with(getContext()).load("http://192.168.1.5/alleppyfish/uploads/aditya_pp3.jpg").into(profilePicFMIV);
//        Picasso.with(profile_pic).into(profilePicFMIV);

        Picasso.with(this)
                .load(profile_pic)
                .into(profilePicFMIV);

        userContactNumberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyProfileDetails.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog_phno, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.enterNewPhnoEdt);



                dialogBuilder.setTitle("Enter Your New Phone No");

                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //do something with edt.getText().toString();

                        String newPhno = edt.getText().toString().trim();

                        changePhno(newPhno, userEmail);
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });


    }

    private void changePhno(final String newPhno, final String userEmail) {

        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PHONE_NO_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    Log.i(TAG, "The Error Value is :: "+error);

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session


                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String phoneNo = user.getString("phone_no");
                        String citySQL = user.getString("city");
                        String stateSQL = user.getString("state");
                        String districtSql = user.getString("district");
                        String profile_pic = user.getString("profile_pic_url");
                        String created_at = user
                                .getString("created_at");


                        db.upfateUserPhno(phoneNo);

                        Intent intent1 = getIntent();
                        overridePendingTransition(0, 0);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent1);


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");

                        JSONObject jObj1 = new JSONObject(response);
                        String message = jObj1.getString("error_msg");

                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();

                        Log.i(TAG,"Login Error "+message);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();

                    try {
                        JSONObject jsonObject = new JSONObject((response));
                        String erroMsg = jsonObject.getString("error_msg");
                        Log.i(TAG,"Login Error MSG :: 298 :: "+erroMsg);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
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

                params.put("email", userEmail);
                params.put("phone_no", newPhno);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout_ob) {

            return true;
        }

        if (id == R.id.change_password) {

            Intent changPWDIntent = new Intent(MyProfileDetails.this, ForgotPasswordActivity.class);
            changPWDIntent.putExtra("myMailId", userEmail);
            startActivity(changPWDIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_my_profile, menu);
        return true;
    }

    private void uploadProfilePiic() {

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.UPLOAD_PROFILE_PIC_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        uploadNewProfilePic.setVisibility(View.GONE);
                        //Showing toast message of the response
//                        Toast.makeText(AddProfile.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        volleyError.printStackTrace();

                        //Showing toast
//                        Toast.makeText(MyProfileDetails.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

//                        Log.i("Error 123", "Error in Volley " + volleyError.getMessage().toString().trim());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = userName.toString().trim();

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

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

//        marshMallowPermission = new MarshMallowPermission(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileDetails.this);
        builder.setTitle("Change Profile Pic!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(MyProfileDetails.this);

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

        profilePicFMIV.setImageBitmap(bitmap);
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

        profilePicFMIV.setImageBitmap(bitmap);
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
