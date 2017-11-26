package com.ss.android.allepyfish.activities_new;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.ForgotPasswordActivity;
import com.ss.android.allepyfish.activities.MainActivity;
import com.ss.android.allepyfish.activities.ManagerLandingScreen;
import com.ss.android.allepyfish.activities.ManagerResponseListDetails;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class ManagerUploadsDetails extends AppCompatActivity {

    String TAG = ManagerUploadsDetails.class.getSimpleName();

    String unique_id;
    String product_name;
    String state;
    String district;
    String city;
    String delivery_date;
    String quantity;
    String created_by;
    String fish_pp;
    String contact_no;
    String product_local_name;
    String deal_status;
    String order_ide;
    String count_per_kg;


    Intent intent;

    TextView fishNameTV;
    TextView fishNameLocal;
    TextView fishDelDateTV;
    TextView tv_order_created_by_name;
    TextView item_req_state;
    TextView item_district;
    TextView item_in_city;
    TextView orderIdTV;
    TextView orderedQtyTV;
    TextView orderedQtyPerKgTV;
    Button closeOrderBtn;

    ImageView uploadedFishImagesMU;

    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_uploads_details);

        intent = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        /*

        intent.putExtra("quantity",resultp.get("quantity"));

        intent.putExtra("deal_status",resultp.get("deal_status"));
        intent.putExtra("order_ide",resultp.get("order_ide"));*/

        unique_id = intent.getStringExtra("unique_id");
        product_name = intent.getStringExtra("product_name");
        state = intent.getStringExtra("state");
        district = intent.getStringExtra("district");
        city = intent.getStringExtra("city");
        delivery_date = intent.getStringExtra("delivery_date");
        quantity = intent.getStringExtra("quantity");
        created_by = intent.getStringExtra("created_by");
        fish_pp = intent.getStringExtra("fish_pp");
        Log.i(TAG, "The Fish Link " + fish_pp);
        contact_no = intent.getStringExtra("contact_no");
        product_local_name = intent.getStringExtra("product_local_name");
        deal_status = intent.getStringExtra("deal_status");
        order_ide = intent.getStringExtra("order_ide");
        count_per_kg = intent.getStringExtra("count_per_kg");

        uploadedFishImagesMU = (ImageView) findViewById(R.id.uploadedFishImagesMU);


        orderIdTV = (TextView) findViewById(R.id.orderIdTV);
        orderIdTV.setText(order_ide);

        orderedQtyTV = (TextView) findViewById(R.id.orderedQtyTV);
        orderedQtyTV.setText(quantity + " Kg's");

        fishNameLocal = (TextView) findViewById(R.id.fishLocalNameTV);
        fishNameLocal.setText(product_local_name);

        fishNameTV = (TextView) findViewById(R.id.fishNameTV);
        fishNameTV.setText(product_name);

        fishDelDateTV = (TextView) findViewById(R.id.fishDelDateTV);
        fishDelDateTV.setText(delivery_date);

        orderedQtyPerKgTV = (TextView)findViewById(R.id.orderedQtyPerKgTV);
        orderedQtyPerKgTV.setText(count_per_kg);

//        fishNameTV = (TextView) findViewById(R.id.fishNameTV);
//        fishNameTV.setText(product_name);

        tv_order_created_by_name = (TextView) findViewById(R.id.tv_order_created_by_name);
        tv_order_created_by_name.setText(created_by);

        item_req_state = (TextView) findViewById(R.id.item_req_state);
        item_req_state.setText(state);

        item_district = (TextView) findViewById(R.id.item_district);
        item_district.setText(district);

        item_in_city = (TextView) findViewById(R.id.item_in_city);
        item_in_city.setText(city);


        Picasso.with(this).load(fish_pp).into(uploadedFishImagesMU);


        closeOrderBtn = (Button) findViewById(R.id.closeOrderBtn);

        if (deal_status.equals("Close")) {
            closeOrderBtn.setVisibility(View.GONE);
//            menuItem.setEnabled(false);
//            menuItem.setVisible(false);

        } else {
            closeOrderBtn.setVisibility(View.VISIBLE);
//            menuItem.setEnabled(false);
//            menuItem.setVisible(true);
        }
        closeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new closeMgnrRequest().execute();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class closeMgnrRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {


                URL url = new URL(AppConfig.POST_UPDATE_DEAL);
                JSONObject postDataParams = new JSONObject();


                postDataParams.put("unique_id", unique_id);
                postDataParams.put("deal_offer_status", "Close");

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
//            Toast.makeText(getApplicationContext(), result,
//                    Toast.LENGTH_LONG).show();

            Log.i(TAG, "Getting the Value :: "+result.toString());

//            startActivity(new Intent(AddProfile.this, MainActivity.class));
            startActivity(new Intent(ManagerUploadsDetails.this, ManagerMyUploads.class));
            finish();
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.edit_my_order_menu, menu);
        menuItem = menu.findItem(R.id.edit_my_order);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.edit_my_order:

                Intent i = new Intent(ManagerUploadsDetails.this, ManagerEditMyOrder.class);
//            i.putExtra("product_name",product_name);
                i.putExtra("order_ide", order_ide);
                i.putExtra("date", delivery_date);
                i.putExtra("count_per_kg", count_per_kg);
                i.putExtra("qty", quantity);
                startActivity(i);
                finish();
                break;

        }

        //noinspection SimplifiableIfStatement

       /* if (id == R.id.edit_my_order) {
            Intent i = new Intent(ManagerUploadsDetails.this,ManagerEditMyOrder.class);
//            i.putExtra("product_name",product_name);
            i.putExtra("order_ide",order_ide);
            i.putExtra("date",delivery_date);
            i.putExtra("count_per_kg",count_per_kg);
            i.putExtra("qty",quantity);
            startActivity(i);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}