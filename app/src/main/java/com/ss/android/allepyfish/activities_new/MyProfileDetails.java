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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.ss.android.allepyfish.fragments.MyProfile;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MyProfileDetails extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_profile);

        profilePicFMIV = (ImageView) findViewById(R.id.profilePicFMIV);
        uploadNewProfilePic = (ImageButton) findViewById(R.id.uploadNewProfilePic);

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

                        //Showing toast
                        Toast.makeText(MyProfileDetails.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                        Log.i("Error 123", "Error in Volley " + volleyError.getMessage().toString().trim());
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
}
