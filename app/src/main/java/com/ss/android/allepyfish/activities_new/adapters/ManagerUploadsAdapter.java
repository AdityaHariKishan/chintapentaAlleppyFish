package com.ss.android.allepyfish.activities_new.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
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

import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities_new.ManagerUploadsDetails;
import com.ss.android.allepyfish.activities_new.RespondOrder;
import com.ss.android.allepyfish.utils.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 6/30/2017.
 */

public class ManagerUploadsAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    static ImageView imageView;

    String fish_pp;

    public ManagerUploadsAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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

        View itemView = inflater.inflate(R.layout.my_uploads_items, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        rank = (TextView) itemView.findViewById(R.id.name_ls);
        country = (TextView) itemView.findViewById(R.id.email_ls);
        population = (TextView) itemView.findViewById(R.id.mobile_ls);
        imageView = (ImageView) itemView.findViewById(R.id.imageView_ls);

        // Capture position and set results to the TextViews
        rank.setText(resultp.get("product_name") + "\n State : " + resultp.get("state") + ", District : " + resultp.get("district") + ", City : " + resultp.get("city"));
        country.setText("Delivery Date : " + resultp.get("delivery_date")+"\nOrder Id : "+resultp.get("order_ide"));
        population.setText("Quantity : " + resultp.get("quantity")+"\nDeal Status : "+resultp.get("deal_status"));

        if (resultp.get("product_name").equals("Anchovies")) {
            Picasso.with(context).load(AppConfig.fish_images_url+"Anchovies.jpg").into(imageView);
            fish_pp = AppConfig.fish_images_url+"Anchovies.jpg";
        }
        if (resultp.get("product_name").equals("Bombay Duck")) {
            Picasso.with(context).load(AppConfig.fish_images_url+"bombay_duck.jpg").into(imageView);
            fish_pp = AppConfig.fish_images_url+"bombay_duck.jpg";
        }
        if (resultp.get("product_name").equals("Butterfish")) {
            Picasso.with(context).load(AppConfig.fish_images_url+"butterfish.jpg").into(imageView);
            fish_pp = AppConfig.fish_images_url+"butterfish.jpg";
        }
        if (resultp.get("product_name").equals("Lobsters")) {
            Picasso.with(context).load(AppConfig.fish_images_url+"lobsters.jpg").into(imageView);
            fish_pp = AppConfig.fish_images_url+"lobsters.jpg";
        }
        if (resultp.get("product_name").equals("Bluefin Travelly")) {
            Picasso.with(context).load(AppConfig.fish_images_url+"bluefin_travelly.jpg").into(imageView);
            fish_pp = AppConfig.fish_images_url+"bluefin_travelly.jpg";
        }
        if (resultp.get("product_name").equals("Catfish")) {
            Picasso.with(context).load(AppConfig.fish_images_url+"cat_fish.jpg").into(imageView);
            fish_pp = AppConfig.fish_images_url+"cat_fish.jpg";
        }


        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        // Capture ListView item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                Intent intent = new Intent(context, ManagerUploadsDetails.class);
                intent.putExtra("unique_id", resultp.get("unique_id"));
                intent.putExtra("product_name", resultp.get("product_name"));
                intent.putExtra("product_local_name", resultp.get("product_local_name"));
                intent.putExtra("state", resultp.get("state"));
                intent.putExtra("district",resultp.get("district"));
                intent.putExtra("city",resultp.get("city"));
                intent.putExtra("delivery_date",resultp.get("delivery_date"));
                intent.putExtra("quantity",resultp.get("quantity"));
                intent.putExtra("created_by",resultp.get("created_by"));
                intent.putExtra("fish_pp",fish_pp);
                intent.putExtra("contact_no",resultp.get("contact_no"));
                intent.putExtra("deal_status",resultp.get("deal_status"));
                intent.putExtra("order_ide",resultp.get("order_ide"));
                intent.putExtra("count_per_kg",resultp.get("count_per_kg"));
//                // Start SingleItemView Class
                context.startActivity(intent);

            }
        });
        return itemView;
    }
}