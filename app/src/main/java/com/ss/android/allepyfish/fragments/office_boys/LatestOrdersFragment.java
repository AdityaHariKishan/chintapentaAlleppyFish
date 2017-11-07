package com.ss.android.allepyfish.fragments.office_boys;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.UploadNewFish;
import com.ss.android.allepyfish.activities_new.adapters.LatestOrdersAdapter;
import com.ss.android.allepyfish.adapters.OfficeBoyAssigningAdapter;
import com.ss.android.allepyfish.fragments.MyProductsUploadsFragment;
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

/**
 * Created by dell on 5/27/2017.
 */

public class LatestOrdersFragment extends Fragment {

    private String TAG = LatestOrdersFragment.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON

    ArrayList<HashMap<String, String>> contactList;

    SQLiteHandler db;
    SessionManager session;

    String userName, userContactNo, userDistrict, userState, userProfilePicURL;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_latest_orders, container, false);

        db = new SQLiteHandler(getContext());
        session = new SessionManager(getContext().getApplicationContext());

        List<ContactInfo> contacts = db.getAllContacts();

        for (ContactInfo cn : contacts) {
            userName = cn.getName();
            userContactNo = cn.getPhone_no();
            userProfilePicURL = cn.getprofile_pic_url();
            userDistrict = cn.getDistrict();
            userState = cn.getState();

            // Writing Contacts to log
            Log.d("Name: userName :: ", userName);
        }

        contactList = new ArrayList<>();

        lv = (ListView) rootView.findViewById(R.id.list_new_orders);

        new GetLatestOrderUpdates().execute();
        return rootView;
    }

    private class GetLatestOrderUpdates extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getContext());
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

                        if (userDistrict.equals(c.getString("district"))) {
                            String id = c.getString("id");
                            String unique_id = c.getString("unique_id");
                            String product_name = c.getString("product_name");
                            String product_local_name = c.getString("product_local_name");
                            String state = c.getString("state");
                            String district = c.getString("district");
                            String city = c.getString("city");
                            String delivery_date = c.getString("delivery_date");
                            String quantity = c.getString("quantity");
                            String count_per_kg = c.getString("count_per_kg");
                            String contactNo = c.getString("contact_no");
                            String created_by = c.getString("created_by");
                            String deal_status = c.getString("deal_status");
                            String creater_pp = c.getString("creater_pp");
                            String order_ide = c.getString("order_ide");
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
                            contact.put("count_per_kg", count_per_kg);
                            contact.put("contact_no", contactNo);
                            contact.put("created_by", created_by);
                            contact.put("deal_status", deal_status);
                            contact.put("creater_pp", creater_pp);
                            contact.put("order_ide", order_ide);

                            // adding contact to contact list
                            contactList.add(contact);
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext().getApplicationContext(),
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
            LatestOrdersAdapter sfa = new LatestOrdersAdapter(getContext(), contactList);
            lv.setAdapter(sfa);


        }
    }
}