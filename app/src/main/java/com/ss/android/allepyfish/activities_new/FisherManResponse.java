package com.ss.android.allepyfish.activities_new;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;
import com.ss.android.allepyfish.utils.NoDefaultSpinner;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class FisherManResponse extends AppCompatActivity {
    
    Intent intent;
    String qtyUnits = "Units";
    ArrayAdapter<CharSequence> adapterProductUnits;
    String userName,userContactNo,fmPP;
    SQLiteHandler db;

    String productUUid,productName,createdBy,quantity,state,district, city,delivery_date, deal_status, creater_pp,contact_no, totalQTY;
    
    String fm_delivery_dateStr,qty_providing_fmStr, product_mkt_priceStr,product_offer_priceStr; 

    String TAG = FisherManResponse.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisher_man_response);

        intent = getIntent();

        db = new SQLiteHandler(this);
        List<ContactInfo> contactInfoList =  db.getAllContacts();
        for(ContactInfo contactInfo: contactInfoList){
            userName = contactInfo.getName();
            userContactNo = contactInfo.getPhone_no();
            fmPP = contactInfo.getprofile_pic_url();
        }

        productUUid = intent.getStringExtra("unique_id");
        productName = intent.getStringExtra("product_name");
         createdBy = intent.getStringExtra("created_by");
        quantity = intent.getStringExtra("quantity");
        state = intent.getStringExtra("state");
        district = intent.getStringExtra("district");
        city = intent.getStringExtra("city");
        delivery_date = intent.getStringExtra("delivery_date");
        deal_status = intent.getStringExtra("deal_status");
        creater_pp = intent.getStringExtra("creater_pp");
        contact_no = intent.getStringExtra("contact_no");

        final Spinner qty_spinner = (Spinner)findViewById(R.id.qty_spinner);

        adapterProductUnits = ArrayAdapter.createFromResource(this, R.array.select_units, android.R.layout.simple_spinner_item);
        adapterProductUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qty_spinner.setPrompt(qtyUnits);

        qty_spinner.setAdapter((new NoDefaultSpinner(adapterProductUnits, R.layout.select_units_custom_spinner, this)));

        final EditText fm_delivery_date = (EditText)findViewById(R.id.fm_delivery_date);
        final EditText qty_providing_fm = (EditText)findViewById(R.id.qty_providing_fm);
        final EditText product_mkt_price = (EditText)findViewById(R.id.product_mkt_price);
        final EditText product_offer_price = (EditText)findViewById(R.id.product_offer_price);

        Button upload_fm_info_button = (Button)findViewById(R.id.upload_fm_info_button);
        upload_fm_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fm_delivery_dateStr = fm_delivery_date.getText().toString().trim();
                 qty_providing_fmStr = qty_providing_fm.getText().toString().trim();
                product_mkt_priceStr = product_mkt_price.getText().toString().trim();
                 product_offer_priceStr = product_offer_price.getText().toString().trim();

                String unitsQTy = qty_spinner.getSelectedItem().toString().trim();

                totalQTY = qty_providing_fmStr+" "+unitsQTy;



//                updateFMInfo(userName, userContactNo, fmPP, productUUid, productName, createdBy, quantity, state, district, city, delivery_date, deal_status, creater_pp, contact_no,
//                        fm_delivery_dateStr, qty_providing_fmStr, product_mkt_priceStr, product_offer_priceStr);


                new updateFMData().execute();

            }
        });
    }

    private class updateFMData extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {


                URL url = new URL(AppConfig.POST_ORDER_RES_FM);
                JSONObject postDataParams = new JSONObject();

                postDataParams.put("unique_id", productUUid);
                postDataParams.put("product_name",productName );
                postDataParams.put("district", district);
                postDataParams.put("city", city);
                postDataParams.put("state", state);
                postDataParams.put("delivery_date", delivery_date);
                postDataParams.put("quantity", quantity);
//                params.put("contact_no", contact_no);
                postDataParams.put("manager_name", createdBy);
                postDataParams.put("fm_name", userName);
                postDataParams.put("fm_pp", fmPP);
                postDataParams.put("fm_contact_no", userContactNo);
                postDataParams.put("delivery_date_by_mngr", delivery_date);
                postDataParams.put("delivery_date_by_fm", fm_delivery_dateStr);
                postDataParams.put("quantity_providing", qty_providing_fmStr);
                postDataParams.put("product_mkt_price", product_mkt_priceStr);
                postDataParams.put("product_offer_price", product_offer_priceStr);
                postDataParams.put("deal_offer_status", "Open");

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
//            startActivity(new Intent(AddProfile.this, LandingActivity.class));
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




    private void updateFMInfo(final String userName, final String userContactNo, final String fmPP, final String productUUid, final String productName, final String createdBy, final String quantity, final String state, final String district, final String city, final String delivery_date, String deal_status, String creater_pp, final String contact_no, final String fm_delivery_dateStr, final String qty_providing_fmStr, final String product_mkt_priceStr, final String product_offer_priceStr) {

        String tag_string_req = "req_register";

//        pDialog.setMessage("Registering ...");
        final String is_working = "0";
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.POST_ORDER_RES_FM, new Response.Listener<String>() {

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

//                        Intent intentUID = new Intent(ManagerOrderRequest.this,BoysInfoList.class);
//                        intentUID.putExtra("unique_id",uniqueIdStr);
//                        intentUID.putExtra("userName",userName);
//                        startActivity(intentUID);

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
                //String , String , String createdBy, String quantity, String state, String ,
                // String city, String delivery_date, String deal_status, String creater_pp, String contact_no,
                // String fm_delivery_dateStr, String , String , String
                params.put("unique_id", productUUid);
                params.put("product_name",productName );
                params.put("district", district);
                params.put("city", city);
                params.put("state", state);
                params.put("delivery_date", delivery_date);
                params.put("quantity", quantity);
//                params.put("contact_no", contact_no);
                params.put("manager_name", createdBy);
                params.put("fm_name", userName);
                params.put("fm_pp", fmPP);
                params.put("fm_contact_no", userContactNo);
                params.put("delivery_date_by_mngr", delivery_date);
                params.put("delivery_date_by_fm", fm_delivery_dateStr);
                params.put("quantity_providing", qty_providing_fmStr);
                params.put("product_mkt_price", product_mkt_priceStr);
                params.put("product_offer_price", product_offer_priceStr);
                params.put("deal_status", "Open");

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
