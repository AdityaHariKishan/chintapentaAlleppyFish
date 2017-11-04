package com.ss.android.allepyfish.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.adapters.CustomListViewAdapter;
import com.ss.android.allepyfish.adapters.MyUploadsAdapter;
import com.ss.android.allepyfish.handlers.HttpHandler;
import com.ss.android.allepyfish.model.RowItem;
import com.ss.android.allepyfish.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 4/30/2017.
 */

public class MyProductsUploadsFragment extends Fragment {


    private String TAG = MyProductsUploadsFragment.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON

    ArrayList<HashMap<String, String>> contactList;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_my_uploads, container, false);

        contactList = new ArrayList<>();

        lv = (ListView) rootView.findViewById(R.id.list);

        new GetContacts().execute();
        return rootView;
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

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
            String jsonStr = sh.makeServiceCall(AppConfig.URL_GET_PRODUCT_DETAILS);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

//                    Toast.makeText(getActivity(),"Welcome ",Toast.LENGTH_LONG).show();

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("user_info");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("product_name");
                        String name = c.getString("product_available_from");
                        String email = c.getString("product_location");
                        String productURL = c.getString("product_pic1");
                        String approvedStatus = c.getString("approved_Status");
                        String contactNo = c.getString("contact_no");
                        //  String description = c.getString("description");
                        //String contentType = c.getString("content_type");

                        // Phone node is JSON Object

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("product_name", id);
                        contact.put("product_available_from", name);
                        contact.put("product_location", email);
                        contact.put("product_pic1", productURL);
                        contact.put("approved_Status", approvedStatus);
                        contact.put("contact_no", contactNo);
//                        contact.put("description",description);
//                        contact.put("content_type",contentType);

                        // adding contact to contact list
                        contactList.add(contact);
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

                MyUploadsAdapter sfa = new MyUploadsAdapter(getContext(), contactList);
                lv.setAdapter(sfa);
            }
        }

    }
}