package com.ss.android.allepyfish.activities_new.adapters;

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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.ManagerResponseListDetails;
import com.ss.android.allepyfish.activities_new.ManagerResponseList;
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
 * Created by dell on 7/11/2017.
 */

public class MyOrderFMResponseAdapter extends BaseAdapter {


    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    static SquareImageView imageView;

    public MyOrderFMResponseAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
        TextView tvCountry, tvDelDateMgnr, tvFMDelDate;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.my_order_response_fm, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        rank = (TextView) itemView.findViewById(R.id.tvName);
        country = (TextView) itemView.findViewById(R.id.tvDateOfBirth);
        population = (TextView) itemView.findViewById(R.id.tvHeight);
        tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
        tvDelDateMgnr = (TextView) itemView.findViewById(R.id.tvDelDateMgnr);
        tvFMDelDate = (TextView) itemView.findViewById(R.id.tvFMDelDate);
        imageView = (SquareImageView) itemView.findViewById(R.id.ivImage);
        // Locate the ImageView in listview_item.xml


//delivery_date_by_mngr

        // Capture position and set results to the TextViews
        rank.setText("Fisherman : " + resultp.get("fm_name"));
        country.setText("\nDistrict: " + resultp.get("district") + "\nState : " + resultp.get("state"));
        population.setText("Product : " + resultp.get("product_name") + "\nQuantity : " + resultp.get("quantity"));
        tvCountry.setText("Price Rs: " + resultp.get("product_offer_price") + "\nStatus : " + resultp.get("response_status"));
//        tvDelDateMgnr.setText("Manager Delivery Date Request " + resultp.get("delivery_date_by_mngr"));
//        tvFMDelDate.setText("Fisherman Delivery Date Response " + resultp.get("delivery_date_by_fm"));
        String imageURL1 = resultp.get("fm_pp");
        Picasso.with(context).load(imageURL1).memoryPolicy(MemoryPolicy.NO_STORE).into(imageView);


        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        // Capture ListView item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);


                Intent intent = new Intent(context, ManagerResponseListDetails.class);
                intent.putExtra("product_name", resultp.get("product_name"));
                intent.putExtra("unique_id", resultp.get("unique_id"));
                intent.putExtra("fm_contact_no", resultp.get("fm_contact_no"));
                intent.putExtra("product_local_name", resultp.get("product_local_name"));
                intent.putExtra("fm_name", resultp.get("fm_name"));
                intent.putExtra("city", resultp.get("city"));
                intent.putExtra("state", resultp.get("state"));
                intent.putExtra("district", resultp.get("district"));
                intent.putExtra("product_name", resultp.get("product_name"));
                intent.putExtra("quantity", resultp.get("quantity"));
                intent.putExtra("product_offer_price", resultp.get("product_offer_price"));
                intent.putExtra("delivery_date_by_mngr", resultp.get("delivery_date_by_mngr"));
                intent.putExtra("delivery_date_by_fm", resultp.get("delivery_date_by_fm"));
                intent.putExtra("fm_pp", resultp.get("fm_pp"));
                intent.putExtra("district", resultp.get("district"));
                intent.putExtra("deal_status", resultp.get("deal_status"));
                intent.putExtra("response_status", resultp.get("response_status"));
                context.startActivity(intent);

            }
        });
        return itemView;
    }


}


