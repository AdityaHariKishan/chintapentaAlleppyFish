package com.ss.android.allepyfish.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.adapters.BoysInfoAdapter;
import com.ss.android.allepyfish.adapters.CustomListViewAdapter;
import com.ss.android.allepyfish.adapters.CustomListViewBoysAdapter;
import com.ss.android.allepyfish.adapters.MyUploadsAdapter;
import com.ss.android.allepyfish.fragments.MyProductsUploadsFragment;
import com.ss.android.allepyfish.handlers.HttpHandler;
import com.ss.android.allepyfish.model.RowItem;
import com.ss.android.allepyfish.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoysInfoList extends AppCompatActivity {


    Button assign_button, cancel_assigne_btn;

    private String TAG = BoysInfoList.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://api.androidhive.info/contacts/";

    ArrayList<HashMap<String, String>> contactList;

    Intent passingIntent;

    public static String uniqueIdStr;
    public static String userNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boys_info_list);

        passingIntent = getIntent();

        uniqueIdStr = passingIntent.getStringExtra("unique_id");
        userNameStr = passingIntent.getStringExtra("userName");


        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list_boys_info);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
//            pDialog = new ProgressDialog(getContext());
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppConfig.GET_OFFICE_BOYS_LIST);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("office_boy_info");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String phoneNo = c.getString("phone_no");
                        String city = c.getString("city");
                        String contactNo = c.getString("profile_pic_url");
                        //  String description = c.getString("description");
                        //String contentType = c.getString("content_type");

                        // Phone node is JSON Object

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("phone_no", phoneNo);
                        contact.put("city", city);
                        contact.put("profile_pic_url", contactNo);
//                        contact.put("description",description);
//                        contact.put("content_type",contentType);

                        // adding contact to contact list
                        contactList.add(contact);
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
//            if (pDialog.isShowing()) {
//                pDialog.dismiss();
//
//            }

            BoysInfoAdapter sfa = new BoysInfoAdapter(BoysInfoList.this, contactList);
            lv.setAdapter(sfa);


        }

    }
}
