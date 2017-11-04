package com.ss.android.allepyfish.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities_new.ManagerMyUploads;
import com.ss.android.allepyfish.activities_new.ManagerUploadsDetails;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ManagerResponseListDetails extends AppCompatActivity {


    String fish_pp;

    Intent intent;

    TextView fishNameTV;
    TextView fishNameLocal;
    TextView fishDelDateTV;
    TextView delivery_date_by_fm_txt;
    TextView item_req_state;
    TextView fishReqDistrict;
    TextView fishReqCity;
    TextView delivery_date_by_mngr_tv;
    TextView contact_no;
    ImageView uploadedFishImagesMU;
    TextView fishReqState;
    TextView quantitySupplying;

    Button acceptFMResponse,rejectFMResponse;

    String product_name;
    String fm_name;
    String city;
    String state;
    String quantity;
    String product_offer_price;
    String delivery_date_by_mngr;
    String delivery_date_by_fm;
    String fm_pp;
    String district;
    String unique_id;
    String fm_contact_no;
    String product_local_name;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_response_list_details);

        intent = getIntent();

        unique_id = intent.getStringExtra("unique_id");
        product_name = intent.getStringExtra("product_name");
        fm_name = intent.getStringExtra("fm_name");
        state = intent.getStringExtra("state");
        district = intent.getStringExtra("district");
        city = intent.getStringExtra("city");
        delivery_date_by_mngr = intent.getStringExtra("delivery_date_by_mngr");
        quantity = intent.getStringExtra("quantity");
        fm_contact_no = intent.getStringExtra("fm_contact_no");
        product_offer_price = intent.getStringExtra("product_offer_price");
        delivery_date_by_fm = intent.getStringExtra("delivery_date_by_fm");
        product_local_name = intent.getStringExtra("product_local_name");

//        fm_pp = intent.getStringExtra("fm_pp");


        uploadedFishImagesMU = (ImageView) findViewById(R.id.uploadedFishImagesFMResponse);

        fishNameTV = (TextView) findViewById(R.id.fishNameTVFMR);
        fishNameTV.setText(product_name);

        fishNameLocal = (TextView) findViewById(R.id.fishNameTVLocalFMR);
        fishNameTV.setText(product_local_name);

        contact_no = (TextView) findViewById(R.id.fmContactNoResponse);
        contact_no.setText(fm_contact_no);

        quantitySupplying = (TextView) findViewById(R.id.fmQTYSupplying);
        quantitySupplying.setText(quantity);



        fishDelDateTV = (TextView) findViewById(R.id.fmReqDate);
        fishDelDateTV.setText(delivery_date_by_fm);

        delivery_date_by_mngr_tv = (TextView) findViewById(R.id.mgnrReqDate);
        delivery_date_by_mngr_tv.setText(delivery_date_by_mngr);

        fishReqState = (TextView) findViewById(R.id.fishReqState);
        fishReqState.setText(state);



        fishReqDistrict = (TextView) findViewById(R.id.fishReqDistrict);
        fishReqDistrict.setText(district);

        fishReqCity = (TextView) findViewById(R.id.fishReqCity);
        fishReqCity.setText(city);


        if (product_name.equals("Anchovies")) {
            Picasso.with(this).load(AppConfig.fish_images_url+"Anchovies.jpg").into(uploadedFishImagesMU);
            fish_pp = AppConfig.fish_images_url+"Anchovies.jpg";
        }
        if (product_name.equals("Bombay Duck")) {
            Picasso.with(this).load(AppConfig.fish_images_url+"bombay_duck.jpg").into(uploadedFishImagesMU);
            fish_pp = AppConfig.fish_images_url+"bombay_duck.jpg";
        }
        if (product_name.equals("Butterfish")) {
            Picasso.with(this).load(AppConfig.fish_images_url+"butterfish.jpg").into(uploadedFishImagesMU);
            fish_pp = AppConfig.fish_images_url+"butterfish.jpg";
        }
        if (product_name.equals("Lobsters")) {
            Picasso.with(this).load(AppConfig.fish_images_url+"lobsters.jpg").into(uploadedFishImagesMU);
            fish_pp = AppConfig.fish_images_url+"lobsters.jpg";
        }
        if (product_name.equals("Bluefin Travelly")) {
            Picasso.with(this).load(AppConfig.fish_images_url+"bluefin_travelly.jpg").into(uploadedFishImagesMU);
            fish_pp = AppConfig.fish_images_url+"bluefin_travelly.jpg";
        }
        if (product_name.equals("Catfish")) {
            Picasso.with(this).load(AppConfig.fish_images_url+"cat_fish.jpg").into(uploadedFishImagesMU);
            fish_pp = AppConfig.fish_images_url+"cat_fish.jpg";
        }


        Picasso.with(this).load(fish_pp).into(uploadedFishImagesMU);


//        closeOrderBtn = (Button)findViewById(R.id.closeOrderBtn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        acceptFMResponse = (Button)findViewById(R.id.acceptFMResponse);
        acceptFMResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String statusInfo = "Accepted";
//                acceptRejectResponse(statusInfo,unique_id);
                new updateFMResponse(statusInfo).execute();
            }
        });
        rejectFMResponse = (Button)findViewById(R.id.rejectFMResponse);
        rejectFMResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String statusInfo = "Rejected";
                new updateFMResponse(statusInfo).execute();
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class updateFMResponse extends AsyncTask<String, Void, String> {

        private String data;

        public updateFMResponse(String passedData) {
            data = passedData;
        }

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {


                URL url = new URL(AppConfig.POST_UPDATE_FM_RESPONSE);
                JSONObject postDataParams = new JSONObject();

//                String urlPath = "http://www.skilluniverse.org/skill_mobile_app/uploads/"+firstNameStr+".png";



                postDataParams.put("unique_id", unique_id);
//                params.put("unique_id", "c1cffbb2-67a7-40cd-87ac");
                postDataParams.put("delivery_response", data);

                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());

            }

        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();

//            startActivity(new Intent(AddProfile.this, MainActivity.class));
            startActivity(new Intent(ManagerResponseListDetails.this, ManagerMyUploads.class));
        }
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
}
