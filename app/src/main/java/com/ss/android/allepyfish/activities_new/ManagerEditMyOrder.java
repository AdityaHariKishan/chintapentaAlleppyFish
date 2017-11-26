package com.ss.android.allepyfish.activities_new;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
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
import com.ss.android.allepyfish.utils.DecimalDigitsInputFilter;
import com.ss.android.allepyfish.utils.InputFilterMinMax;
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
import java.text.SimpleDateFormat;
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

    SimpleDateFormat dateFormatter;

    Button makeOrderBtn,cancelMuUpdate;

    private DatePickerDialog pickUpDateDialog;

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

        dateFormatter = new SimpleDateFormat("MMM dd,yyyy");

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        delivery_date = (EditText) findViewById(R.id.inputUpdateDeliveryDate);
        delivery_date.setOnClickListener(this);
        delivery_date.setText(delDate);

        final String formattedDate = dateFormatter.format(c.getTime());
        delivery_date.setText(formattedDate);

        Calendar newCalendar = Calendar.getInstance();
        pickUpDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                delivery_date.setText(dateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        pickUpDateDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());

        inputQuantity = (EditText) findViewById(R.id.inputUpdateQuantity);
        inputQuantity.setText(qttStr);
        inputQuantity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});

        countPerKg = (EditText) findViewById(R.id.inputUpdateCountPerKgEdt);
        countPerKg.setText(count_per_kgStr);
        countPerKg.setFilters(new InputFilter[]{new InputFilterMinMax("1", "1000")});


        makeOrderBtn = (Button) findViewById(R.id.updateOrderEdtBtn);
        makeOrderBtn.setOnClickListener(this);

        cancelMuUpdate = (Button) findViewById(R.id.cancelMuUpdate);
        cancelMuUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inputUpdateDeliveryDate:
                pickUpDateDialog.show();
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


                if(!(qtyStr.length() == 0)) {
//                    if(!(countKGSTr.length() == 0)) {
                        makeOrderRequest(order_ideStr, delDateStr, qtyStr, countKGSTr);
                    /*}else {
                        countPerKg.setError("Enter Count Per Kg");
                    }*/
                }else {
                    inputQuantity.setError("Enter Quantity");
                }


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
                        Toast.makeText(getApplicationContext(), "Product Successfully Updated", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ManagerEditMyOrder.this, ManagerMyUploads.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();

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








    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}