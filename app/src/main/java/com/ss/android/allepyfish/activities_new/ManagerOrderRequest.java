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

import static com.ss.android.allepyfish.R.mipmap.quantity;

public class ManagerOrderRequest extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ManagerOrderRequest";

    EditText fishNameEdt, inputDealCity, delivery_date, inputQuantity, inputCountPerKgEdt;

    Spinner selectStateSpinner, selectDistrictSpinner, selectQtyUnits, selectFishSpinner;

    ArrayAdapter<CharSequence> adapterProductUnits, selectStateAdapter;

    String qtyUnits = "Ubnits";
    Button makeOrderBtn;
    String defaultTextForStateSpinner = "Select State";

    SQLiteHandler db;
    SessionManager session;

    String selectedFishLocalItem;
    String englishFishNameSelectedStr;

    private DatePickerDialog pickUpDateDialog;

    SimpleDateFormat dateFormatter;

    private Calendar calendar;
    private int year, month, day;
    String stateCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_order_request);

        db = new SQLiteHandler(this);
        session = new SessionManager(getApplicationContext());

        dateFormatter = new SimpleDateFormat("MMM dd,yyyy");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        fishNameEdt = (EditText) findViewById(R.id.fishNameEdt);
        inputDealCity = (EditText) findViewById(R.id.inputDealCity);
        delivery_date = (EditText) findViewById(R.id.inputDeliveryDate);
        delivery_date.setOnClickListener(this);
        inputQuantity = (EditText) findViewById(R.id.inputQuantity);
        inputQuantity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});

        inputCountPerKgEdt = (EditText) findViewById(R.id.inputCountPerKgEdt);
        inputCountPerKgEdt.setFilters(new InputFilter[]{new InputFilterMinMax("1", "1000")});

        selectStateSpinner = (Spinner) findViewById(R.id.selectStateSpinner);
        selectDistrictSpinner = (Spinner) findViewById(R.id.selectDistrictSpinner);
        selectQtyUnits = (Spinner) findViewById(R.id.selectQtyUnits);
        selectFishSpinner = (Spinner) findViewById(R.id.selectFishSpinner);

        makeOrderBtn = (Button) findViewById(R.id.makeOrderBtn);
        makeOrderBtn.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

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

        adapterProductUnits = ArrayAdapter.createFromResource(this, R.array.select_units, android.R.layout.simple_spinner_item);
        adapterProductUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectQtyUnits.setPrompt(qtyUnits);

        selectQtyUnits.setAdapter((new NoDefaultSpinner(adapterProductUnits, R.layout.select_units_custom_spinner, this)));

        selectStateAdapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        adapterProductUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectStateSpinner.setPrompt(defaultTextForStateSpinner);

        selectStateSpinner.setAdapter(new NoDefaultSpinner(selectStateAdapter, R.layout.select_state, this));
        selectStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectFishSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFishLocalItem = selectFishSpinner.getSelectedItem().toString();

                Log.i("Selected Value in Spinner 2", " Item Fish Selected Value Fish :: " + selectedFishLocalItem);

                if (position == 0) {
                    englishFishNameSelectedStr = "Anchovies";
                }
                if (position == 1) {
                    englishFishNameSelectedStr = "Barracuda";
                }
                if (position == 2) {
                    englishFishNameSelectedStr = "Bluefin Travelly";
                }
                if (position == 3) {
                    englishFishNameSelectedStr = "Bombay Duck";
                }
                if (position == 4) {
                    englishFishNameSelectedStr = "Butterfish";
                }
                if (position == 5) {
                    englishFishNameSelectedStr = "Catfish";
                }

                Log.i("Selected Value in Spinner 2", " Item Selected Value :: " + englishFishNameSelectedStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selectStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {

                    Resources res = getResources();
                    String[] biharStateValues = res.getStringArray(R.array.ap);
                    String[] selectAPFish = res.getStringArray(R.array.andhra_fish);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    selectDistrictSpinner.setAdapter(dataAdapter);

                    ArrayAdapter<String> apFishAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, selectAPFish);
                    apFishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    apFishAdapter.notifyDataSetChanged();
                    selectFishSpinner.setAdapter(apFishAdapter);
                    stateCode = "AP";

                }
                if (position == 2) {

                    Resources res = getResources();
                    String[] biharStateValues = res.getStringArray(R.array.Karnataka);
                    String[] karnatakaFish = res.getStringArray(R.array.kannada_fish);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    selectDistrictSpinner.setAdapter(dataAdapter);

                    ArrayAdapter<String> kaFishAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, karnatakaFish);
                    kaFishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kaFishAdapter.notifyDataSetChanged();
                    selectFishSpinner.setAdapter(kaFishAdapter);
                    stateCode = "KA";
                }
                if (position == 3) {

                    Resources res = getResources();
                    String[] biharStateValues = res.getStringArray(R.array.Kerala);
                    String[] klFish = res.getStringArray(R.array.kerala_fish);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    selectDistrictSpinner.setAdapter(dataAdapter);

                    ArrayAdapter<String> kaFishAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, klFish);
                    kaFishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kaFishAdapter.notifyDataSetChanged();
                    selectFishSpinner.setAdapter(kaFishAdapter);
                    stateCode = "KL";
                }
                if (position == 4) {

                    Resources res = getResources();

                    String[] biharStateValues = res.getStringArray(R.array.Lakshadweep);
                    String[] ldValues = res.getStringArray(R.array.kerala_fish);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    selectDistrictSpinner.setAdapter(dataAdapter);

                    ArrayAdapter<String> kaFishAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, ldValues);
                    kaFishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kaFishAdapter.notifyDataSetChanged();
                    selectFishSpinner.setAdapter(kaFishAdapter);
                    stateCode = "LK";
                }
                if (position == 5) {

                    Resources res = getResources();

                    String[] biharStateValues = res.getStringArray(R.array.Puducherry);
                    String[] pyFish = res.getStringArray(R.array.tamil_fish);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    selectDistrictSpinner.setAdapter(dataAdapter);

                    ArrayAdapter<String> kaFishAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, pyFish);
                    kaFishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kaFishAdapter.notifyDataSetChanged();
                    selectFishSpinner.setAdapter(kaFishAdapter);
                    stateCode = "PY";
                }
                if (position == 6) {

                    Resources res = getResources();

                    String[] biharStateValues = res.getStringArray(R.array.TamilNadu);
                    String[] tnFish = res.getStringArray(R.array.tamil_fish);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    selectDistrictSpinner.setAdapter(dataAdapter);

                    ArrayAdapter<String> kaFishAdapter = new ArrayAdapter<String>(ManagerOrderRequest.this,
                            android.R.layout.simple_spinner_item, tnFish);
                    kaFishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kaFishAdapter.notifyDataSetChanged();
                    selectFishSpinner.setAdapter(kaFishAdapter);
                    stateCode = "TN";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inputDeliveryDate:
                pickUpDateDialog.show();
                break;

            case R.id.makeOrderBtn:
                String userName = null, userContactNo = null, userProfilePicURL = null, mgnrStateStr, mgnrCityStr, inputCountPerKgStr;
//                String fishNameStr = fishNameEdt.getText().toString().trim();
                Random rn = new Random();
                String result = String.valueOf(rn.nextInt());
                System.out.println(result);

                String orderId = "AF-" + stateCode + "-" + result;

                String fishNameStr = null;
                String selectStateStr = null;
                String selectDistrictStr = null;

                try {
                    selectStateStr = selectStateSpinner.getSelectedItem().toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Select State", Toast.LENGTH_SHORT).show();
                }

                try {
                    fishNameStr = selectFishSpinner.getSelectedItem().toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    selectDistrictStr = selectDistrictSpinner.getSelectedItem().toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String dealCityStr = inputDealCity.getText().toString().trim();
                String delDateStr = delivery_date.getText().toString().trim();
                String qtyStr = inputQuantity.getText().toString().trim();

                inputCountPerKgStr = inputCountPerKgEdt.getText().toString().trim();

//                String selectUnitsStr = selectQtyUnits.getSelectedItem().toString().trim();


                UUID uuid = UUID.randomUUID();
                String uuIdStr = uuid.toString().trim();

//                    if (!(fishNameStr.length() == 0)) {

//                        if (!(inputCountPerKgStr.length() == 0)) {

                if (!(dealCityStr.length() == 0)) {
                    if ((qtyStr.length() == 0)) {
                        inputQuantity.setError("Enter No Of Kg's");
                    } else {
                        makeOrderRequest(uuIdStr, englishFishNameSelectedStr, fishNameStr, dealCityStr, delDateStr, selectStateStr, selectDistrictStr, inputCountPerKgStr, orderId, qtyStr);
                    }
                } else {
                    inputDealCity.setError("Please Enter City");
                }
                       /* } else {
                            inputCountPerKgEdt.setError("Enter Count Per Kg");
                        }*/


//                startActivity(new Intent(ManagerOrderRequest.this, ManagerMyUploads.class));


//                new MakeOrderRequest(uuIdStr, englishFishNameSelectedStr, fishNameStr, dealCityStr, delDateStr,
//                        selectStateStr, selectDistrictStr, inputCountPerKgStr, orderId, qtyStr, userName, userContactNo, userProfilePicURL).execute();

                break;
        }

    }

    private void makeOrderRequest(final String uuIdStr, final String englishFishNameSelectedStr, final String fishNameStr, final String dealCityStr, final String delDateStr, final String selectStateStr, final String selectDistrictStr, final String inputCountPerKgStr, final String orderId, final String qtyStr) {
        Log.i("Getting Unique Id ", "UUID Value is ......" + uuIdStr + " fishName " + fishNameStr);

        String userName = null;
        String userContactNo = null;
        String userProfilePicURL = null;
        String mgnrStateStr;
        String mgnrCityStr;
        List<ContactInfo> contacts = db.getAllContacts();

        for (ContactInfo cn : contacts) {
            userName = cn.getName();
            userContactNo = cn.getPhone_no();
            userProfilePicURL = cn.getprofile_pic_url();
            mgnrStateStr = cn.getState();
            mgnrCityStr = cn.getCity();
            // Writing Contacts to log
            Log.d("Name: userName :: ", userName + " userProfilePicURL " + userProfilePicURL + " mgnrStateStr " + mgnrStateStr + " mgnrCityStr " + mgnrCityStr);
        }

        String tag_string_req = "req_register";

//        pDialog.setMessage("Registering ...");
        final String is_working = "0";
//        showDialog();

        final String finalUserName = userName;
        final String finalUserContactNo = userContactNo;
        final String finalUserProfilePicURL = userProfilePicURL;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.POST_ORDER_BY_MANAGER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
//                hideDialog();
                String resVal = null;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    resVal = jsonObject.getString("message");
                    Log.d(TAG, "Upload Response: Value " + resVal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (resVal.equals("Product successfully created.")) {
                    startActivity(new Intent(ManagerOrderRequest.this, ManagerMyUploads.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), resVal, Toast.LENGTH_SHORT).show();
                }

               /* try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Product Successfully Approved", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(ManagerOrderRequest.this, ManagerMyUploads.class));
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
                }*/

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

                params.put("unique_id", uuIdStr);
                params.put("product_name", englishFishNameSelectedStr);
                params.put("product_local_name", fishNameStr);
                params.put("district", selectDistrictStr);
                params.put("city", dealCityStr);
                params.put("state", selectStateStr);
                params.put("delivery_date", delDateStr);
                params.put("quantity", qtyStr);
                params.put("count_per_kg", inputCountPerKgStr);
                params.put("order_ide", orderId);
                params.put("created_by", finalUserName);
                params.put("contact_no", finalUserContactNo);
                params.put("creater_pp", finalUserProfilePicURL);
                params.put("deal_status", "Open");

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }
        if (id == R.id.action_my_uploads) {
            startActivity(new Intent(ManagerOrderRequest.this, ManagerMyUploads.class));
            return true;
        }

        if (id == R.id.action_my_uploads_response) {
            startActivity(new Intent(ManagerOrderRequest.this, ManagerUploadsResponse.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void logoutUser() {

        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ManagerOrderRequest.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class MakeOrderRequest extends AsyncTask<String, Void, String> {
        String uuIdStr, englishFishNameSelectedStr, fishNameStr, dealCityStr, delDateStr, selectStateStr, selectDistrictStr, inputCountPerKgStr, orderId, qtyStr, userName, userContactNo, userProfilePicURL;

        public MakeOrderRequest(String uuIdStr, String englishFishNameSelectedStr, String fishNameStr, String dealCityStr, String delDateStr, String selectStateStr,
                                String selectDistrictStr, String inputCountPerKgStr, String orderId, String qtyStr, String userName, String userContactNo, String userProfilePicURL) {
            this.uuIdStr = uuIdStr;
            this.englishFishNameSelectedStr = englishFishNameSelectedStr;
            this.fishNameStr = fishNameStr;
            this.dealCityStr = dealCityStr;
            this.delDateStr = delDateStr;
            this.selectStateStr = selectStateStr;
            this.selectDistrictStr = selectDistrictStr;
            this.inputCountPerKgStr = inputCountPerKgStr;
            this.orderId = orderId;
            this.qtyStr = qtyStr;
            this.userName = userName;
            this.userContactNo = userContactNo;
            this.userProfilePicURL = userProfilePicURL;
        }

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {


                URL url = new URL(AppConfig.POST_FM_ORDERS_RESPONSE);
                JSONObject params = new JSONObject();

                /*params.put("unique_id", uuIdStr);
                params.put("order_ide", orderId);
                params.put("product_name", englishFishNameSelectedStr);
                params.put("product_local_name", fishNameStr);
                params.put("district", selectDistrictStr);
                params.put("city", dealCityStr);
                params.put("state", selectStateStr);
                params.put("delivery_date", delDateStr);
                params.put("quantity", qtyStr);
                params.put("count_per_kg", inputCountPerKgStr);
                params.put("contact_no", userContactNo);
                params.put("created_by", userName);
                params.put("creater_pp", userProfilePicURL);
                params.put("deal_status", "Open");*/

                params.put("unique_id", "123455");
                params.put("order_ide", "APPY12343");
                params.put("product_name", "ButterFish");
                params.put("product_local_name", "Viral Meen");
                params.put("district", "Yanam");
                params.put("city", "Yanam");
                params.put("state", "Puducherry");
                params.put("delivery_date", "10/09/2017");
                params.put("quantity", "34 kgs");
                params.put("count_per_kg", "21");
                params.put("contact_no", "9032138731");
                params.put("created_by", "Aditya");
                params.put("creater_pp", "12345.com");
                params.put("deal_status", "Open");


                Log.e("postDataParams", params.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(params));

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
//            startActivity(new Intent(ManagerOrderRequest.this, FisherManScreen.class));
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