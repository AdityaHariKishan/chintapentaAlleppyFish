package com.ss.android.allepyfish.activities.admin;

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
import com.ss.android.allepyfish.activities.admin.adapter.RegisteredMembersAdapter;
import com.ss.android.allepyfish.activities_new.FisherManScreen;
import com.ss.android.allepyfish.activities_new.MyProfileDetails;
import com.ss.android.allepyfish.adapters.ManagerProductsAdapter;
import com.ss.android.allepyfish.handlers.HttpHandler;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisteredMembers extends AppCompatActivity {

    SQLiteHandler db;

    SessionManager session;

    SwipyRefreshLayout mSwipyRefreshLayout;

    SwipeMenuListView list_mgnr;

    Button assign_button, cancel_assigne_btn;

    private String TAG = ManagerLandingScreen.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON

    ArrayList<HashMap<String, String>> contactList;



    public static String uniqueIdStr;
    public static String userNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_members);



        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        contactList = new ArrayList<>();

        list_mgnr = (SwipeMenuListView)findViewById(R.id.registeredMembers);

        new GetRegisteredMembers().execute();

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(RegisteredMembers.this, LoginActivity.class);
        startActivity(intent);
        finish();
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

        return super.onOptionsItemSelected(item);
    }

    private class GetRegisteredMembers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegisteredMembers.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppConfig.GET_ALL_USERS_LIST);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("admin_info");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);


                            String id = c.getString("unique_id");
                            String user_name = c.getString("user_name");
                            String email = c.getString("email");
                            String contact_no = c.getString("contact_no");
                            String city = c.getString("city");
                            String address_1 = c.getString("address_1");
                            String address_2 = c.getString("address_2");
                            String profile_pic_url = c.getString("profile_pic_url");
                            String login_type = c.getString("login_type");


                            // Phone node is JSON Object

                            // tmp hash map for single contact
                            HashMap<String, String> contact = new HashMap<>();

                            // adding each child node to HashMap key => value
                            contact.put("unique_id", id);
                            contact.put("user_name", user_name);
                            contact.put("email", email);
                            contact.put("contact_no", contact_no);
                            contact.put("city", city);
                            contact.put("address_1", address_1);
                            contact.put("address_2", address_2);
                            contact.put("profile_pic_url", profile_pic_url);
//                            contact.put("uploaded_by", uploaded_by);
                            contact.put("login_type", login_type);


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

            RegisteredMembersAdapter registeredMembersAdapter = new RegisteredMembersAdapter(RegisteredMembers.this, contactList);
            list_mgnr.setAdapter(registeredMembersAdapter);


        }

    }
}

