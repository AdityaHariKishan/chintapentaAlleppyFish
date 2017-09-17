package com.ss.android.allepyfish.activities.office_boy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.LoginActivity;
import com.ss.android.allepyfish.activities.ManagerLandingScreen;
import com.ss.android.allepyfish.activities.admin.RegisteredMembers;
import com.ss.android.allepyfish.activities.admin.adapter.AdminRequestsAdapter;
import com.ss.android.allepyfish.activities.admin.adapter.RegisteredMembersAdapter;
import com.ss.android.allepyfish.handlers.HttpHandler;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminRequests extends AppCompatActivity {

    SQLiteHandler db;

    SessionManager session;

    SwipyRefreshLayout mSwipyRefreshLayout;

    ListView adminRequestLV;

    Button assign_button, cancel_assigne_btn;

    private String TAG = ManagerLandingScreen.class.getSimpleName();

    private ProgressDialog pDialog;

    // URL to get contacts JSON

    ArrayList<HashMap<String, String>> contactList;



    public static String uniqueIdStr;
    public static String userNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_requests);


        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());


        contactList = new ArrayList<>();

        adminRequestLV = (ListView)findViewById(R.id.adminRequestLV);

        new GetAdminRequests().execute();

    }


    private class GetAdminRequests extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AdminRequests.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppConfig.GET_USER_REQUESTS);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("user_requests_info");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);


                        String id = c.getString("id");
                        String user_email = c.getString("user_email");
                        String request_subject = c.getString("request_subject");
                        String request_reason = c.getString("request_reason");
                        String request_status = c.getString("request_status");



                        // Phone node is JSON Object

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("user_email", user_email);
                        contact.put("request_subject", request_subject);
                        contact.put("request_reason", request_reason);
                        contact.put("request_status", request_status);


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
            if (pDialog.isShowing()) {
                pDialog.dismiss();

            }

            AdminRequestsAdapter registeredMembersAdapter = new AdminRequestsAdapter(AdminRequests.this, contactList);
            adminRequestLV.setAdapter(registeredMembersAdapter);


        }

    }
}

