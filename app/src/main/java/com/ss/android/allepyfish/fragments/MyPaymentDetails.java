package com.ss.android.allepyfish.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.adapters.MyPaymentDetailsAdapter;
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

/**
 * Created by dell on 6/3/2017.
 */

public class MyPaymentDetails extends Fragment {


    private String TAG = ContactManagers.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://api.androidhive.info/contacts/";

    ArrayList<HashMap<String, String>> contactList;

    Intent passingIntent;

    public static String uniqueIdStr;
    public static String userNameStr;

    ListView office_boy_lv;

    SQLiteHandler db;

    String userName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_payment_deliveries, container, false);



        office_boy_lv = (ListView)rootView.findViewById(R.id.my_delivered_list);

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
            String jsonStr = sh.makeServiceCall(AppConfig.GET_OFFICE_BOYS_DELIVERED_LIST);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("office_boy_info");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String deliveryStatus = "Delivered";

                        if(userName.equals(c.getString("created_by"))) {

                            String id = c.getString("unique_id");
                            String name = c.getString("product_name");
//                            String created_by = c.getString("created_by");
//                            String quantity = c.getString("quantity");
//                            String rate_quoted = c.getString("rate_quoted");
                            String delivery_status = c.getString("delivery_status");
//                            String approved_status = c.getString("approved_status");
//                            String approved_by = c.getString("approved_by");
                            String assigned_by = c.getString("assigned_by");
//                            String assigned_to = c.getString("assigned_to");
                            String product_pic1 = c.getString("product_pic1");
//                            String product_pic2 = c.getString("product_pic2");
//                            String product_pic3 = c.getString("product_pic3");
//                            String product_pic4 = c.getString("product_pic4");
                            String creator_contact_no = c.getString("creator_contact_no");
                            //  String description = c.getString("description");
                            //String contentType = c.getString("content_type");

                            // Phone node is JSON Object

                            // tmp hash map for single contact
                            HashMap<String, String> contact = new HashMap<>();

                            // adding each child node to HashMap key => value
                            contact.put("unique_id", id);
                            contact.put("product_name", name);
//                            contact.put("created_by", created_by);
//                            contact.put("quantity", quantity);
//                            contact.put("rate_quoted", rate_quoted);
//                            contact.put("approved_status", approved_status);
                            contact.put("assigned_by", assigned_by);
//                            contact.put("approved_by", approved_by);
//                            contact.put("rate_quoted", rate_quoted);
//                            contact.put("assigned_to", assigned_to);
                            contact.put("delivery_status", delivery_status);
                            contact.put("product_pic1", product_pic1);
//                            contact.put("product_pic2", product_pic2);
//                            contact.put("product_pic3", product_pic3);
//                            contact.put("product_pic4", product_pic4);
                            contact.put("creator_contact_no", creator_contact_no);
//                        contact.put("description",description);
//                        contact.put("content_type",contentType);

                            // adding contact to contact list
                            contactList.add(contact);


                        }else {
                            Log.i("","Not adding the value D");
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

//            OfficeBoyAssigningAdapter sfa = new OfficeBoyAssigningAdapter(getContext(), contactList);
            MyPaymentDetailsAdapter sfa = new MyPaymentDetailsAdapter(getContext(), contactList);
            office_boy_lv.setAdapter(sfa);


        }

    }
}