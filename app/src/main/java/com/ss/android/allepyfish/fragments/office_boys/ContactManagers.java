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
import com.ss.android.allepyfish.activities_new.adapters.ContactManagerAdapter;
import com.ss.android.allepyfish.adapters.OfficeBoyAssigningAdapter;
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

/**
 * Created by dell on 5/27/2017.
 */

public class ContactManagers extends Fragment {

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_contact_manager, container, false);



        office_boy_lv = (ListView)rootView.findViewById(R.id.contact_mgnr_lv);

        contactList = new ArrayList<>();

        db = new SQLiteHandler(getContext());

        List<ContactInfo> contacts = db.getAllContacts();

        for (ContactInfo cn : contacts) {
            userName = cn.getName();
            // Writing Contacts to log
            Log.d("Name: userName :: ", userName);
        }


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
//            String jsonStr = sh.makeServiceCall(AppConfig.GET_OFFICE_BOYS_ASSIGNING_LIST);
            String jsonStr = sh.makeServiceCall(AppConfig.GET_MANAGERS_LIST);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("managers_info");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String deliveryStatus = "Delivered";

//                        if(userName.equals(c.getString("assigned_to"))) {

                                String id = c.getString("unique_id");
                                String name = c.getString("user_name");
                                String email = c.getString("email");
                                String contact_no = c.getString("contact_no");
                                String city = c.getString("city");
                                String address_1 = c.getString("address_1");
                                String address_2 = c.getString("address_2");
                                String profile_pic_url = c.getString("profile_pic_url");
                                String login_type = c.getString("login_type");

                                //  String description = c.getString("description");
                                //String contentType = c.getString("content_type");

                                // Phone node is JSON Object

                                // tmp hash map for single contact
                                HashMap<String, String> contact = new HashMap<>();

                                // adding each child node to HashMap key => value
                                contact.put("unique_id", id);
                                contact.put("user_name", name);
                                contact.put("email", email);
                                contact.put("contact_no", contact_no);
                                contact.put("address_1", address_1);
                                contact.put("city", city);
                                contact.put("address_2", address_2);
                                contact.put("profile_pic_url", profile_pic_url);
                                contact.put("login_type", login_type);
//                        contact.put("description",description);
//                        contact.put("content_type",contentType);

                                // adding contact to contact list
                                contactList.add(contact);


//                        }else {
//                            Log.i("","Not adding the value D");
//                        }
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
                        Toast.makeText(getContext(),
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

            ContactManagerAdapter sfa = new ContactManagerAdapter(getContext(), contactList);
            office_boy_lv.setAdapter(sfa);


        }

    }
}
