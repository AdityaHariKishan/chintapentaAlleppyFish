package com.ss.android.allepyfish.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.ManagerLandingScreenItemCount;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ss.android.allepyfish.activities.BoysInfoList.uniqueIdStr;
import static com.ss.android.allepyfish.activities.BoysInfoList.userNameStr;

/**
 * Created by dell on 5/25/2017.
 */

public class BoysInfoAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    static ImageView imageView;

    public BoysInfoAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView rank;
        TextView country;
        TextView population;



        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.office_boys_items, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        rank = (TextView) itemView.findViewById(R.id.name_ob);
        country = (TextView) itemView.findViewById(R.id.email_ob);
        population = (TextView) itemView.findViewById(R.id.mobile_ob);
        imageView=(ImageView) itemView.findViewById(R.id.imageView_ob);
        // Locate the ImageView in listview_item.xml



        // Capture position and set results to the TextViews
        rank.setText(resultp.get("name"));
        country.setText(resultp.get("phone_no"));
        population.setText(resultp.get("email"));
        String imageURL1 = resultp.get("profile_pic_url").toString().trim();
        Picasso.with(context).load(imageURL1).into(imageView);


        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        // Capture ListView item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);

                Toast.makeText(context,resultp.get("name"),Toast.LENGTH_LONG).show();

                updateAssigneeName(resultp.get("name"));

//                Intent intent = new Intent(context, SingleItemView.class);
//                intent.putExtra("feed_title", resultp.get("feed_title"));
//                intent.putExtra("feed_type", resultp.get("feed_type"));
//                intent.putExtra("usefull_for",resultp.get("usefull_for"));
//                intent.putExtra("description",resultp.get("description"));
//                intent.putExtra("content_type",resultp.get("content_type"));
//                // Start SingleItemView Class
//
//                context.startActivity(intent);

            }
        });
        return itemView;
    }

    private void updateAssigneeName(final String name) {
//    private void registerUser() {
        // Tag used to cancel the request
        String tag_string_req = "req_register";




        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ASSIGN_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("", "Assignee Response: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String phone_no = user.getString("phone_no");
                        String is_Working = user.getString("city");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
//                        db.addUser(name, phone_no, uid, is_Working, created_at);

                        Toast.makeText(context, "Item successfully assigned.", Toast.LENGTH_LONG).show();


                        // Launch login activity
                        Intent intent = new Intent(context,ManagerLandingScreenItemCount.class);
                        context.startActivity(intent);


                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("", "Registration Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            //            $name, $email, $phone_no, $password, $city, $address_1, $address_2, $profile_pic_url
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("assigned_to", name);
                params.put("unique_id", uniqueIdStr);
                params.put("assigned_by", userNameStr);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
