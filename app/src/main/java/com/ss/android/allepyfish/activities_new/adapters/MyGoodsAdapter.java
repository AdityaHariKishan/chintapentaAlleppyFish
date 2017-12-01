package com.ss.android.allepyfish.activities_new.adapters;

/**
 * Created by dell on 6/10/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.ss.android.allepyfish.activities_new.FisherManResponse;
import com.ss.android.allepyfish.activities_new.ManagerResponseList;
import com.ss.android.allepyfish.activities_new.ManagerUploadsResponse;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;
import com.ss.android.allepyfish.utils.SquareImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ss.android.allepyfish.activities.BoysInfoList.uniqueIdStr;
import static com.ss.android.allepyfish.activities.BoysInfoList.userNameStr;

/**
 * Created by dell on 5/27/2017.
 */

public class MyGoodsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    static SquareImageView imageView;
    String loginType;

    public MyGoodsAdapter(Context context, ArrayList<HashMap<String, String>> arraylist, String loginType) {
        this.context = context;
        data = arraylist;
        this.loginType = loginType;
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

        View itemView = inflater.inflate(R.layout.my_goods_adapter, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        rank = (TextView) itemView.findViewById(R.id.name_goods_adapter);
        country = (TextView) itemView.findViewById(R.id.email_goods_adapter);
        population = (TextView) itemView.findViewById(R.id.mobile_goods_adapter);
        imageView = (SquareImageView) itemView.findViewById(R.id.imageView_goods_adapter);
        // Locate the ImageView in listview_item.xml

        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "Roboto-Italic.ttf");


        rank.setTypeface(custom_font);


        // Capture position and set results to the TextViews
        rank.setText(resultp.get("product_name"));
//        country.setText("Created By : " + resultp.get("manager_name") + "\nSupplying Quantity : " + resultp.get("quantity") + "\nCount Per Kg : " + resultp.get("count_perkg") + "\nRate Quoted : " + resultp.get("product_offer_price") + "\nMy Product Status : " + resultp.get("response_status"));

        if (loginType.equals("Manager")) {
            country.setText("Created By : " + resultp.get("manager_name") + "\nResponded Count : " + resultp.get("product_name_response"));
        }else {
            country.setText("Created By : " + resultp.get("manager_name") + "\nSupplying Quantity : " + resultp.get("quantity") + "\nCount Per Kg : " + resultp.get("count_perkg") + "\nRate Quoted : " + resultp.get("product_offer_price") + "\nMy Product Status : " + resultp.get("response_status"));
        }
        /*if (loginType.equals("Fisher Man")) {
            country.setText("Created By : " + resultp.get("manager_name") + "\nSupplying Quantity : " + resultp.get("quantity") + "\nCount Per Kg : " + resultp.get("count_perkg") + "\nRate Quoted : " + resultp.get("product_offer_price") + "\nMy Product Status : " + resultp.get("response_status"));
        }*/
//        population.setText("State : "+resultp.get("state")+"\n District : "+resultp.get("district"));
//        String imageURL1 = resultp.get("creater_pp");
//        Picasso.with(context).load(imageURL1).into(imageView);
        if (resultp.get("product_name").equals("Anchovies")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "Anchovies.jpg").into(imageView);
        }
        if (resultp.get("product_name").equals("Bombay Duck")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "bombay_duck.jpg").into(imageView);
        }
        if (resultp.get("product_name").equals("Butterfish")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "butterfish.jpg").into(imageView);
        }
        if (resultp.get("product_name").equals("Lobsters")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "lobsters.jpg").into(imageView);
        }
        if (resultp.get("product_name").equals("Bluefin Travelly")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "bluefin_travelly.jpg").into(imageView);
        }
        if (resultp.get("product_name").equals("Barracuda")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "bluefin_travelly.jpg").into(imageView);
        }

        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        // Capture ListView item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);

                if (loginType.equals("Manager")) {
                    Intent intent = new Intent(context, ManagerResponseList.class);
                    intent.putExtra("product_name", resultp.get("product_name"));
                    context.startActivity(intent);
                } else {


                }

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
//                        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                        finish();
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

