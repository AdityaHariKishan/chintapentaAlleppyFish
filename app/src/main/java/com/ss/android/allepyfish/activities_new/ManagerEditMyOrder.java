package com.ss.android.allepyfish.activities_new;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.LoginActivity;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;
import com.ss.android.allepyfish.utils.NoDefaultSpinner;
import com.ss.android.allepyfish.utils.SessionManager;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class ManagerEditMyOrder extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ManagerEditMyOrder";

    EditText  delivery_date, inputQuantity, countPerKg;



    Button makeOrderBtn,cancelMuUpdate;

    private Calendar calendar;
    private int year, month, day;

    Intent intent;
    String delDate,order_ideStr,count_per_kgStr,qttStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_edit_my_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        intent = getIntent();


        delDate = intent.getStringExtra("date");
        order_ideStr = intent.getStringExtra("order_ide");
        count_per_kgStr = intent.getStringExtra("count_per_kg");
        qttStr = intent.getStringExtra("qty");


        delivery_date = (EditText) findViewById(R.id.inputUpdateDeliveryDate);
        delivery_date.setOnClickListener(this);
        delivery_date.setText(delDate);

        inputQuantity = (EditText) findViewById(R.id.inputUpdateQuantity);
        inputQuantity.setText(qttStr);

        countPerKg = (EditText) findViewById(R.id.inputUpdateCountPerKgEdt);
        countPerKg.setText(count_per_kgStr);


        makeOrderBtn = (Button) findViewById(R.id.updateOrderEdtBtn);
        makeOrderBtn.setOnClickListener(this);

        cancelMuUpdate = (Button) findViewById(R.id.cancelMuUpdate);
        cancelMuUpdate.setOnClickListener(this);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inputUpdateDeliveryDate:
                showDialog(999);
                break;

            case R.id.updateOrderEdtBtn:
                String userName = null, userContactNo = null, userProfilePicURL = null, mgnrStateStr, mgnrCityStr,inputCountPerKgStr;
//                String fishNameStr = fishNameEdt.getText().toString().trim();
                Random rn = new Random();
                String result = String.valueOf(rn.nextInt());
                System.out.println(result);

                ;
                String delDateStr = delivery_date.getText().toString().trim();
                String qtyStr = inputQuantity.getText().toString().trim();
                String countKGSTr = countPerKg.getText().toString().trim();



                makeOrderRequest(order_ideStr, delDateStr, qtyStr, countKGSTr);


                break;

            case R.id.cancelMuUpdate:
                finish();
                break;
        }

    }

    private void makeOrderRequest(final String order_ideStr, final String delDateStr, final String qtyStr, final  String countKGSTr) {

        String tag_string_req = "req_register";

//        pDialog.setMessage("Registering ...");
        final String is_working = "0";
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.UPDATE_MY_OLD_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        Toast.makeText(getApplicationContext(), "Product Successfully Approved", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ManagerEditMyOrder.this, ManagerMyUploads.class));

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

                params.put("order_ide", order_ideStr);
                params.put("del_date", delDateStr);
                params.put("qty", qtyStr);
                params.put("count_per_kg", countKGSTr);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate(int year, int i, int day) {
        delivery_date.setText(new StringBuilder().append(day).append("/")
                .append(month + 1).append("/").append(year));
    }





    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}