package com.ss.android.allepyfish.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.office_boy.SingleItemViewOBAssign;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ss.android.allepyfish.R.id.uploadedFishImages;

public class FishUploadsDetails extends AppCompatActivity {

    private static final String TAG = "FishUploadsDetails";

    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    FloatingActionButton fab_assign_office_boy;

    Button accept_upload,reject_upload;

    String st,productNameStr,createdByStr,approvedStatusStr,uploadedByStr,contactNumberStr,rateQuotedStr,uploadedFromStr,product_pic_1_url_str,uniqueIdStr,product_pic4,product_pic3,product_pic2;

    Intent intent;

    TextView fishNameTV,uploadedByTV,contactNumberTV,rate_quoted,uploadedFrom;

    ImageView iv_detail;

    SQLiteHandler db;

    String userName;

    String[] collectProfilePics;

    int mIfCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_uploads_details);

        intent = getIntent();

        db = new SQLiteHandler(this);


        List<ContactInfo> contacts = db.getAllContacts();

        for (ContactInfo cn : contacts) {
            userName = cn.getName();
            // Writing Contacts to log
            Log.d("Name: userName :: ", userName);
        }



        fishNameTV = (TextView)findViewById(R.id.fishNameTV);
        uploadedByTV = (TextView)findViewById(R.id.uploadedByTV);
        contactNumberTV = (TextView)findViewById(R.id.contactNumberTV);
        rate_quoted = (TextView)findViewById(R.id.rate_quoted);
        uploadedFrom = (TextView)findViewById(R.id.uploadedFrom);

        iv_detail = (ImageView)findViewById(R.id.iv_detail);


        st = intent.getStringExtra("load");

        productNameStr = intent.getStringExtra("product_name");
        createdByStr = intent.getStringExtra("created_by");
        approvedStatusStr = intent.getStringExtra("approved_Status");
//        uploadedByStr = intent.getStringExtra("uploaded_by");
        contactNumberStr = intent.getStringExtra("contact_number");
        rateQuotedStr = intent.getStringExtra("rate_quoted");
        uploadedFromStr = intent.getStringExtra("uploadedFrom");
        product_pic_1_url_str = intent.getStringExtra("product_pic1");
        product_pic4 = intent.getStringExtra("product_pic4");
        product_pic2 = intent.getStringExtra("product_pic2");
        product_pic3 = intent.getStringExtra("product_pic3");
        uniqueIdStr = intent.getStringExtra("unique_id");

        collectProfilePics = new String[]{product_pic_1_url_str, product_pic2, product_pic3, product_pic4};



        Picasso.with(FishUploadsDetails.this).load(collectProfilePics[mIfCounter]).into(iv_detail);
        iv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//           mViewLabel.setText(collectProfilePics[mIfCounter]);
                Picasso.with(FishUploadsDetails.this).load(collectProfilePics[mIfCounter]).into(iv_detail);
                if (mIfCounter < collectProfilePics.length - 1) {
                    mIfCounter++;
                    Log.i("Getting Info", String.valueOf(mIfCounter));


                } else {
                    mIfCounter = 0;
                }
            }
        });

        fishNameTV.setText(productNameStr);
        uploadedByTV.setText(createdByStr);
        rate_quoted.setText(rateQuotedStr);
        uploadedFrom.setText(uploadedFromStr);

        accept_upload = (Button)findViewById(R.id.accept_upload);
        accept_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(FishUploadsDetails.this,ManagerLandingScreen.class));

                String productStatus = "Approved";
                approveProduct(uniqueIdStr,userName,productStatus);

            }
        });

//        ImageLoader.getInstance().displayImage(product_pic_1_url_str, iv_detail, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
////                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
////                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
////                progressBar.setVisibility(View.GONE);
//            }
//        });




        reject_upload = (Button)findViewById(R.id.reject_upload);
        reject_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productStatus = "Rejected";
                approveProduct(uniqueIdStr,userName,productStatus);
                startActivity(new Intent(FishUploadsDetails.this,ManagerLandingScreen.class));
            }
        });

        fab_assign_office_boy = (FloatingActionButton)findViewById(R.id.fab_assign_office_boy);
//        if(st.equals("1")){
//
//            fab_assign_office_boy.setVisibility(View.GONE);
//        }
//        if (st.equals("2")){
//            fab_assign_office_boy.setVisibility(View.VISIBLE);
//        }



        fab_assign_office_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Share Info",Toast.LENGTH_LONG).show();


            }
        });

    }

    private void approveProduct(final String uniqueIdStr, final String userName, final String productStatus) {
        Log.i("Getting Unique Id ","UUID Value is ......"+uniqueIdStr+" userName "+userName);



        String tag_string_req = "req_register";

//        pDialog.setMessage("Registering ...");
        final String is_working = "0";
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PRODUCTS_APROVE, new Response.Listener<String>() {

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

                        Toast.makeText(getApplicationContext(), "Product Successfully Approved", Toast.LENGTH_LONG).show();

                        Intent intentUID = new Intent(FishUploadsDetails.this,BoysInfoList.class);
                        intentUID.putExtra("unique_id",uniqueIdStr);
                        intentUID.putExtra("userName",userName);
                        startActivity(intentUID);

                        // Launch login activity
//                        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                        finish();
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
                params.put("unique_id", uniqueIdStr);
                params.put("approved_Status",productStatus );
                params.put("approved_by", userName);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}