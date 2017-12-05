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
import com.ss.android.allepyfish.fragments.office_boys.ContactManagers;
import com.ss.android.allepyfish.fragments.office_boys.MyGoodsSuppliedFragment;
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

public class ManagerUploadsResponse extends AppCompatActivity {

    private String TAG = ContactManagers.class.getSimpleName();

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
    String loginType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_uploads_response);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        office_boy_lv = (ListView) findViewById(R.id.mangerGoodsResponseList);

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
            pDialog = new ProgressDialog(ManagerUploadsResponse.this);
//            pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
//            pDialog = null;

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(AppConfig.GET_MANAGER_DISTINCT_RESPONSE);

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

                        String id = c.getString("id");
                        String name = c.getString("product_name");
                        String manager_name = c.getString("manager_name");
                        String fm_name = c.getString("fm_name");
                        String product_name_response = c.getString("product_name_response");

                        // Phone node is JSON Object

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("unique_id", id);
                        contact.put("product_name", name);
                        contact.put("manager_name", manager_name);
                        contact.put("fm_name",fm_name);
                        contact.put("product_name_response",product_name_response);
//                        contact.put("content_type",contentType);

                        // adding contact to contact list
                        contactList.add(contact);


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
//                pDialog = null;
            }

            MyGoodsAdapter sfa = new MyGoodsAdapter(ManagerUploadsResponse.this, contactList, loginType);
            office_boy_lv.setAdapter(sfa);


        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        finish();
        return true;
    }
}