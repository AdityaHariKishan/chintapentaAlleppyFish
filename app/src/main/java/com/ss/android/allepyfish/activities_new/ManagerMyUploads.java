package com.ss.android.allepyfish.activities_new;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.LoginActivity;
import com.ss.android.allepyfish.activities_new.adapters.LatestOrdersAdapter;
import com.ss.android.allepyfish.activities_new.adapters.ManagerUploadsAdapter;
import com.ss.android.allepyfish.fragments.office_boys.LatestOrdersFragment;
import com.ss.android.allepyfish.handlers.HttpHandler;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManagerMyUploads extends AppCompatActivity {


    private String TAG = ManagerMyUploads.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    SQLiteHandler db;
    SessionManager session;

    // URL to get contacts JSON

    ArrayList<HashMap<String, String>> contactList;

    String userName, userContactNo, userProfilePicURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_my_uploads);

        db = new SQLiteHandler(this);
        session = new SessionManager(getApplicationContext());

        List<ContactInfo> contacts = db.getAllContacts();

        for (ContactInfo cn : contacts) {
            userName = cn.getName();
            userContactNo = cn.getPhone_no();
            userProfilePicURL = cn.getprofile_pic_url();
            // Writing Contacts to log
            Log.d("Name: userName :: ", userName);
        }



        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list_mgnr_orders);
//        ImageButton makeNewOrderBtn = (ImageButton)findViewById(R.id.makeNewOrderBtn);
        FloatingActionButton makeNewOrderBtn = (FloatingActionButton)findViewById(R.id.makeNewOrderBtn);
        makeNewOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerMyUploads.this,ManagerOrderRequest.class));
            }
        });

        new GetLatestOrderUpdates().execute();
    }

    private class GetLatestOrderUpdates extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ManagerMyUploads.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppConfig.GET_LATEST_ORDERS);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("products");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        if(userName.equals(c.getString("created_by"))) {

//                            if((c.getString("created_by")).equals("Open")) {

                            String id = c.getString("id");
                            String unique_id = c.getString("unique_id");
                            String product_name = c.getString("product_name");
                            String product_local_name = c.getString("product_local_name");
                            String state = c.getString("state");
                            String district = c.getString("district");
                            String city = c.getString("city");
                            String delivery_date = c.getString("delivery_date");
                            String quantity = c.getString("quantity");
                            String contactNo = c.getString("contact_no");
                            String created_by = c.getString("created_by");
                            String deal_status = c.getString("deal_status");
                            String creater_pp = c.getString("creater_pp");
                            String order_ide = c.getString("order_ide");
                            String count_per_kg = c.getString("count_per_kg");
                            //  String description = c.getString("description");
                            //String contentType = c.getString("content_type");

                            // Phone node is JSON Object

                            // tmp hash map for single contact
                            HashMap<String, String> contact = new HashMap<>();

                            // adding each child node to HashMap key => value
                            contact.put("id", id);
                            contact.put("unique_id", unique_id);
                            contact.put("product_name", product_name);
                            contact.put("product_local_name", product_local_name);
                            contact.put("state", state);
                            contact.put("district", district);
                            contact.put("city", city);
                            contact.put("delivery_date", delivery_date);
                            contact.put("quantity", quantity);
                            contact.put("contact_no", contactNo);
                            contact.put("created_by", created_by);
                            contact.put("deal_status", deal_status);
                            contact.put("creater_pp", creater_pp);
                            contact.put("order_ide", order_ide);
                            contact.put("count_per_kg", count_per_kg);

                            // adding contact to contact list
                            contactList.add(contact);

//                        }
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

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

//            MyUploadsAdapter sfa = new MyUploadsAdapter(getContext(), contactList);
//            LatestOrdersAdapter sfa = new LatestOrdersAdapter(ManagerMyUploads.this, contactList);
            ManagerUploadsAdapter managerUploadsAdapter = new ManagerUploadsAdapter(ManagerMyUploads.this, contactList);
            lv.setAdapter(managerUploadsAdapter);


        }
    }


    @Override
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
            startActivity(new Intent(ManagerMyUploads.this,ManagerOrderRequest.class));
            return true;
        }

        if (id == R.id.action_my_uploads_response) {
            startActivity(new Intent(ManagerMyUploads.this,ManagerUploadsResponse.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {

        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ManagerMyUploads.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}