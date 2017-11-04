package com.ss.android.allepyfish.activities_new.adapters;

/**
 * Created by dell on 6/10/2017.
 */

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

import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities_new.RespondOrder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell on 6/9/2017.
 */
public class ContactManagerAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    static ImageView imageView;

    public ContactManagerAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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

        View itemView = inflater.inflate(R.layout.contact_manager_adapter, parent, false);
        // Get the position
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        rank = (TextView) itemView.findViewById(R.id.name__mgnr);
        country = (TextView) itemView.findViewById(R.id.email_mgnr);
        population = (TextView) itemView.findViewById(R.id.mobile_mgnr);
        imageView=(ImageView) itemView.findViewById(R.id.imageView_mgnr);

        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "Roboto-Italic.ttf");


        rank.setTypeface(custom_font);


        // Capture position and set results to the TextViews
        rank.setText("User Name : "+resultp.get("user_name")+"\n City : "+resultp.get("city"));
        country.setText("Contact No: "+resultp.get("contact_no"));
        String imageURL1 = resultp.get("profile_pic_url").toString().trim();
        Picasso.with(context).load(imageURL1).into(imageView);
//                .load("http://192.168.0.6//alleppyfish//uploads//aditya.jpg")


        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        // Capture ListView item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
//                Intent intent = new Intent(context, RespondOrder.class);
//                intent.putExtra("unique_id", resultp.get("unique_id"));
//                intent.putExtra("product_name", resultp.get("product_name"));
//                intent.putExtra("state", resultp.get("state"));
//                intent.putExtra("district",resultp.get("district"));
//                intent.putExtra("city",resultp.get("city"));
//                intent.putExtra("delivery_date",resultp.get("delivery_date"));
//                intent.putExtra("quantity",resultp.get("quantity"));
//                intent.putExtra("created_by",resultp.get("created_by"));
//                intent.putExtra("creater_pp",resultp.get("creater_pp"));
//                intent.putExtra("contact_no",resultp.get("contact_no"));
//                // Start SingleItemView Class
//
//                context.startActivity(intent);

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
