package com.ss.android.allepyfish.activities_new.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities_new.RespondOrder;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.CapitalizedTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 6/9/2017.
 */
public class LatestOrdersAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    static ImageView imageView;

    public LatestOrdersAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
        TextView localNameTV;
        CapitalizedTextView orderAvailableTV;
        TextView orderQtyRequiredTV;
//        CapitalizedTextView orderCreatedByTv;

        ImageView imageViewStatus;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        View itemView = inflater.inflate(R.layout.my_uploads_items, parent, false);
        View itemView = inflater.inflate(R.layout.my_available_orders, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        localNameTV = (TextView) itemView.findViewById(R.id.fish_local_name);
        orderAvailableTV = (CapitalizedTextView) itemView.findViewById(R.id.orderAvailableTV);
        orderQtyRequiredTV = (TextView) itemView.findViewById(R.id.orderQtyRequiredTV);
//        orderCreatedByTv = (CapitalizedTextView) itemView.findViewById(R.id.orderCreatedByTv);
        imageView = (ImageView) itemView.findViewById(R.id.imageView_ls);
        imageViewStatus = (ImageView) itemView.findViewById(R.id.imageView_status);
        // Locate the ImageView in listview_item.xml


        // Capture position and set results to the TextViews
        localNameTV.setText(resultp.get("product_local_name"));
        orderAvailableTV.setText(resultp.get("city"));
        orderQtyRequiredTV.setText(resultp.get("quantity") + " Kg" + " & " + resultp.get("count_per_kg")+"/ Kg");
//        orderCreatedByTv.setText(resultp.get("created_by"));

     /*   if (resultp.get("deal_status").equals("Close")) {
            imageViewStatus.setImageResource(R.mipmap.ic_deal_open);
        }else {
            imageViewStatus.setImageResource(R.mipmap.ic_deal_close);
        }*/

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

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                if (resultp.get("deal_status").equals("Open")) {
                    Intent intent = new Intent(context, RespondOrder.class);
                    intent.putExtra("unique_id", resultp.get("unique_id"));
                    intent.putExtra("product_name", resultp.get("product_name"));
                    intent.putExtra("product_local_name", resultp.get("product_local_name"));
                    intent.putExtra("state", resultp.get("state"));
                    intent.putExtra("district", resultp.get("district"));
                    intent.putExtra("city", resultp.get("city"));
                    intent.putExtra("delivery_date", resultp.get("delivery_date"));
                    intent.putExtra("quantity", resultp.get("quantity"));
                    intent.putExtra("created_by", resultp.get("created_by"));
                    intent.putExtra("creater_pp", resultp.get("creater_pp"));
                    intent.putExtra("contact_no", resultp.get("contact_no"));
                    intent.putExtra("order_ide", resultp.get("order_ide"));
                    // Start SingleItemView Class

                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Deal Closed", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return itemView;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
