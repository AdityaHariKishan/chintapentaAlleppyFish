package com.ss.android.allepyfish.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.adapters.BoysInfoAdapter;
import com.ss.android.allepyfish.adapters.ManagerProductsAdapter;
import com.ss.android.allepyfish.handlers.HttpHandler;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.MovieModel;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.SessionManager;
import com.ss.android.allepyfish.utils.SquareImageView;
import com.ss.android.allepyfish.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ManagerLandingScreen extends AppCompatActivity {

    Intent intent;

    String productNameStr;

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

    Intent passingIntent;

    public static String uniqueIdStr;
    public static String userNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_landing_screen);

        intent = getIntent();

        productNameStr = intent.getStringExtra("product_name");

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        contactList = new ArrayList<>();

        list_mgnr = (SwipeMenuListView)findViewById(R.id.list_mgnr);

        new GetContacts().execute();

    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ManagerLandingScreen.this, LoginActivity.class);
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

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ManagerLandingScreen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(AppConfig.URL_GET_PRODUCT_DETAILS);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("user_info");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        if (productNameStr.equals(c.getString("product_name"))) {
                            String id = c.getString("unique_id");
                            String name = c.getString("product_name");
                            String product_type = c.getString("product_type");
                            String quantity = c.getString("quantity");
                            String product_available_from = c.getString("product_available_from");
                            String comments = c.getString("comments");
                            String product_location = c.getString("product_location");
                            String market_rate = c.getString("market_rate");
                            String rate_quoted = c.getString("rate_quoted");
//                            String uploaded_by = c.getString("uploaded_by");
                            String pick_up_time = c.getString("pick_up_time");
                            String created_by = c.getString("created_by");
                            String contact_no = c.getString("contact_no");
                            String approved_Status = c.getString("approved_Status");
                            String approved_by = c.getString("approved_by");
                            String product_pic1 = c.getString("product_pic1");
                            String product_pic2 = c.getString("product_pic2");
                            String product_pic3 = c.getString("product_pic3");
                            String product_pic4 = c.getString("product_pic4");

                            // Phone node is JSON Object

                            // tmp hash map for single contact
                            HashMap<String, String> contact = new HashMap<>();

                            // adding each child node to HashMap key => value
                            contact.put("unique_id", id);
                            contact.put("product_name", name);
                            contact.put("product_type", product_type);
                            contact.put("quantity", quantity);
                            contact.put("product_available_from", product_available_from);
                            contact.put("comments", comments);
                            contact.put("product_location", product_location);
                            contact.put("rate_quoted", rate_quoted);
//                            contact.put("uploaded_by", uploaded_by);
                            contact.put("created_by", created_by);
                            contact.put("pick_up_time", pick_up_time);
                            contact.put("contact_no", contact_no);
                            contact.put("approved_Status", approved_Status);
                            contact.put("approved_by", approved_by);
                            contact.put("product_pic1", product_pic1);
                            contact.put("product_pic2", product_pic2);
                            contact.put("product_pic3", product_pic3);
                            contact.put("product_pic4", product_pic4);

                            // adding contact to contact list
                            contactList.add(contact);
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

            ManagerProductsAdapter sfa = new ManagerProductsAdapter(ManagerLandingScreen.this, contactList);
            list_mgnr.setAdapter(sfa);


        }

    }
}
