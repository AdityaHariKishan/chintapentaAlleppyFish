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

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities_new.ManagerUploadsDetails;
import com.ss.android.allepyfish.activities_new.RespondOrder;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.SquareImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by dell on 6/30/2017.
 */

public class ManagerUploadsAdapter extends BaseAdapter {

    String TAG = ManagerUploadsAdapter.class.getSimpleName();

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    static ImageView imageView_status;
    SquareImageView squareImageView;

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
        TextView fishNameMgnr;
        TextView stateNameMgnr;
        TextView cityNameMgnr;
        TextView qtyOrderedMgnr;
        TextView calendarDateTV;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.my_uploads_items, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        fishNameMgnr = (TextView) itemView.findViewById(R.id.fishNameMgnr);
        stateNameMgnr = (TextView) itemView.findViewById(R.id.stateNameMgnr);
        cityNameMgnr = (TextView) itemView.findViewById(R.id.cityNameMgnr);
        qtyOrderedMgnr = (TextView) itemView.findViewById(R.id.qtyOrderedMgnr);
        calendarDateTV = (TextView) itemView.findViewById(R.id.calendarDateTV);
        squareImageView = (SquareImageView) itemView.findViewById(R.id.imageView_ls);
        imageView_status = (ImageView) itemView.findViewById(R.id.imageView_status);

        DateFormat targetFormat = new SimpleDateFormat("MMM  dd-yyyy", Locale.ENGLISH);
//        DateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
//        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat originalFormat = new SimpleDateFormat("MMM dd,yyyy");

        DateFormat targetFormat2 = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        Date date = null;
        Date date2 = null;
        Date appliedOnDate = null;

        try {

            date = originalFormat.parse(resultp.get("delivery_date"));
            appliedOnDate = originalFormat.parse(resultp.get("delivery_date"));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        final String formattedFromDate;

        String formattedAppliedDate = null;

        if (appliedOnDate != null) {
            formattedFromDate = targetFormat.format(date);  // 20120821
            formattedAppliedDate = targetFormat2.format(appliedOnDate);  // 20120821
        } else {
//            formattedFromDate = targetFormat.format(date);  // 20120821
        }

        // Capture position and set results to the TextViews
        fishNameMgnr.setText(resultp.get("product_name"));

//        country.setText(resultp.get("delivery_date"));
        stateNameMgnr.setText(resultp.get("state"));
        cityNameMgnr.setText(resultp.get("city"));
        calendarDateTV.setText(formattedAppliedDate);
        qtyOrderedMgnr.setText("Quantity : " + resultp.get("quantity") + " Kg's");
       /* if (!resultp.get("count_per_kg").equals("")) {
            countPerKg.setText("Count : "+resultp.get("count_per_kg")+ " / Kg");
        } else {
            countPerKg.setText("-");
        }*/
        if (resultp.get("product_name").equals("Anchovies")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "Anchovies.jpg").memoryPolicy(MemoryPolicy.NO_STORE).into(squareImageView);
            fish_pp = AppConfig.fish_images_url + "Anchovies.jpg";
        } else if (resultp.get("product_name").equals("Bombay Duck")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "bombay_duck.jpg").memoryPolicy(MemoryPolicy.NO_STORE).into(squareImageView);
            fish_pp = AppConfig.fish_images_url + "bombay_duck.jpg";
        } else if (resultp.get("product_name").equals("Butterfish")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "butterfish.jpg").memoryPolicy(MemoryPolicy.NO_STORE).into(squareImageView);
            fish_pp = AppConfig.fish_images_url + "butterfish.jpg";
        } else if (resultp.get("product_name").equals("Lobsters")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "lobsters.jpg").memoryPolicy(MemoryPolicy.NO_STORE).into(squareImageView);
            fish_pp = AppConfig.fish_images_url + "lobsters.jpg";
        } else if (resultp.get("product_name").equals("Bluefin Travelly")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "bluefin_travelly.jpg").memoryPolicy(MemoryPolicy.NO_STORE).into(squareImageView);
            fish_pp = AppConfig.fish_images_url + "bluefin_travelly.jpg";
        } else if (resultp.get("product_name").equals("Catfish")) {
            Picasso.with(context).load(AppConfig.fish_images_url + "cat_fish.jpg").memoryPolicy(MemoryPolicy.NO_STORE).into(squareImageView);
            fish_pp = AppConfig.fish_images_url + "cat_fish.jpg";
        } else {
            Picasso.with(context).load(AppConfig.fish_images_url + "only_fish.jpg").memoryPolicy(MemoryPolicy.NO_STORE).into(squareImageView);
            fish_pp = AppConfig.fish_images_url + "only_fish.jpg";
        }

        if (resultp.get("deal_status").equals("Close")) {
            imageView_status.setImageResource(R.drawable.ic_close);
        } else {
            imageView_status.setImageResource(R.drawable.ic_open);
        }

        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        // Capture ListView item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);

                String fish_pp_link;

                if (resultp.get("product_name").equals("Anchovies")) {

                    fish_pp_link = AppConfig.fish_images_url + "Anchovies.jpg";
                } else if (resultp.get("product_name").equals("Bombay Duck")) {
                    fish_pp_link = AppConfig.fish_images_url + "bombay_duck.jpg";
                } else if (resultp.get("product_name").equals("Butterfish")) {
                    fish_pp_link = AppConfig.fish_images_url + "butterfish.jpg";
                } else if (resultp.get("product_name").equals("Lobsters")) {
                    fish_pp_link = AppConfig.fish_images_url + "lobsters.jpg";
                } else if (resultp.get("product_name").equals("Bluefin Travelly")) {
                    fish_pp_link = AppConfig.fish_images_url + "bluefin_travelly.jpg";
                } else if (resultp.get("product_name").equals("Catfish")) {
                    fish_pp_link = AppConfig.fish_images_url + "cat_fish.jpg";
                } else {
                    Picasso.with(context).load(AppConfig.fish_images_url + "only_fish.jpg").memoryPolicy(MemoryPolicy.NO_STORE).into(squareImageView);
                    fish_pp_link = AppConfig.fish_images_url + "only_fish.jpg";
                }


                Log.i(TAG, "The Fish Link " + fish_pp_link);


                Intent intent = new Intent(context, ManagerUploadsDetails.class);
                intent.putExtra("unique_id", resultp.get("unique_id"));
                intent.putExtra("product_name", resultp.get("product_name"));
                intent.putExtra("product_local_name", resultp.get("product_local_name"));
                intent.putExtra("state", resultp.get("state"));
                intent.putExtra("district", resultp.get("district"));
                intent.putExtra("city", resultp.get("city"));
                intent.putExtra("delivery_date", resultp.get("delivery_date"));
                intent.putExtra("quantity", resultp.get("quantity"));
                intent.putExtra("created_by", resultp.get("created_by"));
                intent.putExtra("fish_pp", fish_pp_link);

                intent.putExtra("contact_no", resultp.get("contact_no"));
                intent.putExtra("deal_status", resultp.get("deal_status"));
                intent.putExtra("order_ide", resultp.get("order_ide"));
                intent.putExtra("count_per_kg", resultp.get("count_per_kg"));
//                // Start SingleItemView Class
                context.startActivity(intent);

            }
        });
        return itemView;
    }
}