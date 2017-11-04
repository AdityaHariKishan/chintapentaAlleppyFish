package com.ss.android.allepyfish.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.ManagerLandingScreen;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 5/17/2017.
 */

public class ManagerItemCountAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    static ImageView imageView;

    public ManagerItemCountAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
        imageView=(ImageView) itemView.findViewById(R.id.imageView_ls);
        // Locate the ImageView in listview_item.xml

        // Capture position and set results to the TextViews
        rank.setText("Fish Name : "+resultp.get("product_name"));
        country.setText("Available Count : "+resultp.get("product_count"));
        if(resultp.get("product_name").equals("Anchovies"))
        {
            Picasso.with(context).load("http://192.168.1.5/alleppyfish/fish_images/Anchovies.jpg").into(imageView);
        }
        if(resultp.get("product_name").equals("Bombay Duck"))
        {
            Picasso.with(context).load("http://192.168.1.5/alleppyfish/fish_images/bombay_duck.jpg").into(imageView);
        }
        if(resultp.get("product_name").equals("Butterfish"))
        {
            Picasso.with(context).load("http://192.168.1.5/alleppyfish/fish_images/butterfish.jpg").into(imageView);
        }
        if(resultp.get("product_name").equals("Lobsters"))
        {
            Picasso.with(context).load("http://192.168.1.5/alleppyfish/fish_images/lobsters.jpg").into(imageView);
        }
        if(resultp.get("product_name").equals("Bluefin Travelly"))
        {
            Picasso.with(context).load("http://192.168.1.5/alleppyfish/fish_images/bluefin_travelly.jpg").into(imageView);
        }

//                .load("http://192.168.0.6//alleppyfish//uploads//aditya.jpg")


        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        // Capture ListView item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                Intent intent = new Intent(context, ManagerLandingScreen.class);
                intent.putExtra("product_name", resultp.get("product_name"));

                // Start SingleItemView Class

                context.startActivity(intent);

            }
        });
        return itemView;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}