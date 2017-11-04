package com.ss.android.allepyfish.activities_new;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities_new.adapters.MyGoodsAdapter;
import com.ss.android.allepyfish.activities_new.adapters.MyOrderFMResponseAdapter;
import com.ss.android.allepyfish.fragments.office_boys.ContactManagers;
import com.ss.android.allepyfish.handlers.HttpHandler;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManagerResponseList extends AppCompatActivity {

    private String TAG = ManagerResponseList.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON

    ArrayList<HashMap<String, String>> contactList;

    Intent passingIntent;

    public static String uniqueIdStr;
    public static String userNameStr;

    ListView office_boy_lv;

    SQLiteHandler db;

    String userName;
    public String loginType;

    String productNameStr;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_response_list);

        intent = getIntent();

        productNameStr = intent.getStringExtra("product_name");

        Log.i("Product Name ","Product Name :: "+productNameStr);

//        office_boy_lv = (ListView) findViewById(R.id.mangerGoodsResponseList);
        office_boy_lv = (ListView) findViewById(R.id.fmRespondedList);

        contactList = new ArrayList<>();




        new GetContacts().execute();

        db = new SQLiteHandler(this);

        List<ContactInfo> contacts = db.getAllContacts();

        for (ContactInfo cn : contacts) {
            userName = cn.getName();
            loginType = cn.getLoginType();
            // Writing Contacts to log
            Log.d("Name: userName :: ", userName);
        }
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ManagerResponseList.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppConfig.GET_FM_ORDERS_RESPONSE);
//            String jsonStr = sh.makeServiceCall(AppConfig.GET_MANAGER_DISTINCT_RESPONSE);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("products");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String deliveryStatus = "Delivered";

                        if (userName.equals(c.getString("manager_name"))) {

                            if(productNameStr.equals(c.getString("product_name"))) {

                                String id = c.getString("unique_id");
                                String name = c.getString("product_name");
                                String product_local_name = c.getString("product_local_name");
                                String manager_name = c.getString("manager_name");
                                String fm_name = c.getString("fm_name");
                                String state = c.getString("state");
                                String district = c.getString("district");
                                String city = c.getString("city");
                                String delivery_date_by_mngr = c.getString("delivery_date_by_mngr");
                                String delivery_date_by_fm = c.getString("delivery_date_by_fm");
                                String quantity = c.getString("quantity");
                                String product_offer_price = c.getString("product_offer_price");
                                String fm_contact_no = c.getString("fm_contact_no");
                                String deal_status = c.getString("deal_status");
                                String fm_pp = c.getString("fm_pp");
                                String response_status = c.getString("response_status");
                                String count_perkg = c.getString("count_perkg");

                                // Phone node is JSON Object

                                // tmp hash map for single contact
                                HashMap<String, String> contact = new HashMap<>();

                                // adding each child node to HashMap key => value
                                contact.put("unique_id", id);
                                contact.put("product_name", name);
                                contact.put("product_local_name", product_local_name);
                                contact.put("city", city);
                                contact.put("manager_name", manager_name);
                                contact.put("fm_name", fm_name);
                                contact.put("district", district);
                                contact.put("state", state);
                                contact.put("delivery_date_by_mngr",delivery_date_by_mngr);
                                contact.put("delivery_date_by_fm",delivery_date_by_fm);
                                contact.put("quantity",quantity);
                                contact.put("product_offer_price",product_offer_price);
                                contact.put("fm_contact_no",fm_contact_no);
                                contact.put("deal_status",deal_status);
                                contact.put("fm_pp",fm_pp);
                                contact.put("response_status",response_status);
                                contact.put("count_perkg",count_perkg);

                                // adding contact to contact list
                                contactList.add(contact);

                            }

                        } else {
                            Log.i("", "Not adding the value D");
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());


                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();

            }

//            MyGoodsAdapter sfa = new MyGoodsAdapter(ManagerResponseList.this, contactList);
            MyOrderFMResponseAdapter sfa = new MyOrderFMResponseAdapter(ManagerResponseList.this, contactList);
            office_boy_lv.setAdapter(sfa);


        }

    }
}
