package com.ss.android.allepyfish.activities.office_boy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.AppConfig;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SingleItemViewOBAssign extends AppCompatActivity {

    String TAG = "SingleItemViewOBAssign";


    Intent intent;

    String product_name, created_by, quantity, rate_quoted, approved_status, approved_by, assigned_by, assigned_to, product_pic1, product_pic2, product_pic3, product_pic4, creator_contact_no, unique_id;

    String[] collectProfilePics;

    ImageView uploadedFishImages;

    Button getProfilePicsBtn;

    ImageButton nextImageOBAIB;

    int mIfCounter = 0;

    TextView fishNameTV_ob;

    String userName;
    String uuid;


    TextView mViewLabel, item_created_by_ob_assign, item_rate_ob_assign, item_approved_status_ob_assign, item_approved_by_ob_assign, item_assigned_by_ob_assign, item_assigned_to_ob_assign, tv_creator_contact_no_ob;

    SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_view_obassign);

        intent = getIntent();

        db = new SQLiteHandler(getApplicationContext());
//        session = new SessionManager(getApplicationContext());
        List<ContactInfo> contacts = db.getAllContacts();




        unique_id = intent.getStringExtra("unique_id");
        product_name = intent.getStringExtra("product_name");
        created_by = intent.getStringExtra("created_by");
        quantity = intent.getStringExtra("quantity");
        rate_quoted = intent.getStringExtra("rate_quoted");
        approved_status = intent.getStringExtra("approved_status");
        approved_by = intent.getStringExtra("approved_by");
        assigned_by = intent.getStringExtra("assigned_by");
        assigned_to = intent.getStringExtra("assigned_to");
        product_pic1 = intent.getStringExtra("product_pic1");
        product_pic2 = intent.getStringExtra("product_pic2");
        product_pic3 = intent.getStringExtra("product_pic3");
        product_pic4 = intent.getStringExtra("product_pic4");
        creator_contact_no = intent.getStringExtra("creator_contact_no");

        collectProfilePics = new String[]{product_pic1, product_pic2, product_pic3, product_pic4};

        fishNameTV_ob = (TextView) findViewById(R.id.fishNameTV);
        fishNameTV_ob.setText(product_name);

//        item_created_by_ob_assign = (TextView) findViewById(R.id.item_local_name);
//        item_created_by_ob_assign.setText(created_by);

        item_rate_ob_assign = (TextView) findViewById(R.id.item_req_state);
        item_rate_ob_assign.setText(rate_quoted);

        item_approved_status_ob_assign = (TextView) findViewById(R.id.item_in_city);
        item_approved_status_ob_assign.setText(approved_status);

        item_approved_by_ob_assign = (TextView) findViewById(R.id.item_district);
        item_approved_by_ob_assign.setText(approved_by);

        item_assigned_by_ob_assign = (TextView) findViewById(R.id.fishDelDateTV);
        item_assigned_by_ob_assign.setText(assigned_by);

        item_assigned_to_ob_assign = (TextView) findViewById(R.id.fishLocalNameTV);
        item_assigned_to_ob_assign.setText(assigned_to);

//        tv_creator_contact_no_ob = (TextView) findViewById(R.id.tv_creator_contact_no_ob);
//        tv_creator_contact_no_ob.setText(creator_contact_no);

        mViewLabel = (TextView) findViewById(R.id.ifTestLabel);

        uploadedFishImages = (ImageView) findViewById(R.id.uploadedFishImages);

        nextImageOBAIB = (ImageButton) findViewById(R.id.nextImageOBAIB);

        getProfilePicsBtn = (Button) findViewById(R.id.getProfilePicsBtn);
        getProfilePicsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateDeliveryStatus(unique_id);
            }
        });

        Picasso.with(SingleItemViewOBAssign.this).load(collectProfilePics[mIfCounter]).into(uploadedFishImages);

        uploadedFishImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewLabel.setText(collectProfilePics[mIfCounter]);
                Picasso.with(SingleItemViewOBAssign.this).load(collectProfilePics[mIfCounter]).into(uploadedFishImages);
                if (mIfCounter < collectProfilePics.length - 1) {
                    mIfCounter++;
                    Log.i("Getting Info", String.valueOf(mIfCounter));


                } else {
                    mIfCounter = 0;
                }
            }
        });

        getProfilePicsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateDeliveryStatus(unique_id);

                new registerUser().execute();
            }
        });

    }



    private class registerUser extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {


                URL url = new URL(AppConfig.UPDATE_OFFICE_BOYS_DELIVERY_STATUS);
                JSONObject postDataParams = new JSONObject();



                postDataParams.put("unique_id", unique_id);
                postDataParams.put("delivery_status", "Delivered");


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
}