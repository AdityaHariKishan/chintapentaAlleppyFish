package com.ss.android.allepyfish.activities_new;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.DecimalDigitsInputFilter;
import com.ss.android.allepyfish.utils.InputFilterMinMax;
import com.ss.android.allepyfish.utils.NoDefaultSpinner;

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
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RespondOrder extends AppCompatActivity implements View.OnClickListener {

    String TAG = RespondOrder.class.getSimpleName();

    Intent intent;

    String uniquieIdStr, productNameStr, stateStr, districtStr, cityStr, deliveryDateStr, quantityStr, creater_ppStr, contactNoStr, productLocalNameStr;

    String dateAvailableStr, qtyAvailabilityStr, countPerKgStr, rateQuotedStr, unitsStr, totalQtyStr, createdByStr, orderIDStr;

    EditText dateAvailableEdt, qtyAvailabilityEdt, countPerKgEdt, rateQuotedEdt;

    Spinner selectFmQtyUnits;

    Button respondOrderBtn;

    ArrayAdapter<CharSequence> adapterProductUnits;

    String qtyUnits = "Units";

    SQLiteHandler db;

    String fmName, fmPHNo, fm_ppURL;

    private DatePickerDialog pickUpDateDialog;

    SimpleDateFormat dateFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("MMM dd,yyyy");

        db = new SQLiteHandler(this);
        List<ContactInfo> contactInfos = db.getAllContacts();
        for (ContactInfo contactInfo : contactInfos) {

            fmName = contactInfo.getName();
            fmPHNo = contactInfo.getPhone_no();
            fm_ppURL = contactInfo.getprofile_pic_url();
        }

        intent = getIntent();

        uniquieIdStr = intent.getStringExtra("unique_id");
        productNameStr = intent.getStringExtra("product_name");
        productLocalNameStr = intent.getStringExtra("product_local_name");
        stateStr = intent.getStringExtra("state");
        districtStr = intent.getStringExtra("district");
        cityStr = intent.getStringExtra("city");
        deliveryDateStr = intent.getStringExtra("delivery_date");
        quantityStr = intent.getStringExtra("quantity");
        creater_ppStr = intent.getStringExtra("creater_pp");
        contactNoStr = intent.getStringExtra("contact_no");
        createdByStr = intent.getStringExtra("created_by");
        orderIDStr = intent.getStringExtra("order_ide");

        dateAvailableEdt = (EditText) findViewById(R.id.dateAvailableEdt);
        dateAvailableEdt.setOnClickListener(this);
        dateAvailableEdt.setKeyListener(null);

        qtyAvailabilityEdt = (EditText) findViewById(R.id.qtyAvailabilityEdt);
        qtyAvailabilityEdt.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2, 2)});

        countPerKgEdt = (EditText) findViewById(R.id.countPerKgEdt);
        countPerKgEdt.setFilters(new InputFilter[]{new InputFilterMinMax("1", "1000")});

        rateQuotedEdt = (EditText) findViewById(R.id.rateQuotedEdt);
        rateQuotedEdt.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});

        selectFmQtyUnits = (Spinner) findViewById(R.id.selectFmQtyUnits);


        adapterProductUnits = ArrayAdapter.createFromResource(this, R.array.select_units, android.R.layout.simple_spinner_item);
        adapterProductUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectFmQtyUnits.setPrompt(qtyUnits);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());


        final String formattedDate = dateFormatter.format(c.getTime());
        dateAvailableEdt.setText(formattedDate);

        selectFmQtyUnits.setAdapter((new NoDefaultSpinner(adapterProductUnits, R.layout.select_units_custom_spinner, this)));

        Calendar newCalendar = Calendar.getInstance();
        pickUpDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateAvailableEdt.setText(dateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        pickUpDateDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());

        respondOrderBtn = (Button) findViewById(R.id.respondOrderBtn);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.respond_order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.done_order) {

            dateAvailableStr = dateAvailableEdt.getText().toString().trim();
            qtyAvailabilityStr = qtyAvailabilityEdt.getText().toString().trim();
            countPerKgStr = countPerKgEdt.getText().toString().trim();
            rateQuotedStr = rateQuotedEdt.getText().toString().trim();

            totalQtyStr = qtyAvailabilityStr;

            if (!(qtyAvailabilityStr.length() == 0)) {
                if (!(countPerKgStr.length() == 0)) {

                    if (!(rateQuotedStr.length() == 0)) {

                        new RespondOrderRequest().execute();

                    } else {
                        rateQuotedEdt.setError("Rate Should not be empty");

                    }

                } else {
                    countPerKgEdt.setError("Enter Count Per Kg");

                }
            } else {
                qtyAvailabilityEdt.setError("Qunatity Should not be empty");
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateAvailableEdt:

                pickUpDateDialog.show();
                break;
        }
    }

    private class RespondOrderRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {


                URL url = new URL(AppConfig.POST_FM_ORDERS_RESPONSE);
                JSONObject postDataParams = new JSONObject();

                postDataParams.put("unique_id", uniquieIdStr);
                postDataParams.put("product_name", productNameStr);
                postDataParams.put("product_local_name", productLocalNameStr);
                postDataParams.put("state", stateStr);
                postDataParams.put("district", districtStr);
                postDataParams.put("city", cityStr);
                postDataParams.put("manager_name", createdByStr);
                postDataParams.put("fm_name", fmName);
                postDataParams.put("fm_pp", fm_ppURL);
                postDataParams.put("delivery_date_by_mngr", deliveryDateStr);
                postDataParams.put("delivery_date_by_fm", dateAvailableStr);
                postDataParams.put("quantity_providing", totalQtyStr);
                postDataParams.put("count_perkg", countPerKgStr);
                postDataParams.put("product_offer_price", rateQuotedStr);
                postDataParams.put("deal_offer_status", "Open");
                postDataParams.put("fm_contact_no", fmPHNo);
                postDataParams.put("order_ide", orderIDStr);
                postDataParams.put("response_status", "Pending");


                Log.e("postDataParams", postDataParams.toString());

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
            startActivity(new Intent(RespondOrder.this, FisherManScreen.class));
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
    public void onBackPressed() {
        Intent intent = new Intent(RespondOrder.this, FisherManScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}