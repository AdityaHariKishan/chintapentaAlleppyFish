package com.ss.android.allepyfish.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.activities.FishUploadsDetails;
import com.ss.android.allepyfish.model.RowItem;

import java.util.ArrayList;

/**
 * Created by dell on 4/30/2017.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<RowItem> android_versions;
    private Context context;

    public DataAdapter(Context context, ArrayList<RowItem> android_versions) {
        this.context = context;
        this.android_versions = android_versions;

    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.tv_android.setText(android_versions.get(i).getTitle());
        viewHolder.fishDescTV.setText(android_versions.get(i).getDesc());
        viewHolder.fishUploadedFromTV.setText(android_versions.get(i).getFishUploadedFromTV());
        Picasso.with(context).load(android_versions.get(i).getImageId()).resize(120, 60).into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return android_versions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_android, fishDescTV, fishUploadedFromTV;
        ImageView img_android;
        LinearLayout fishLayout;

        public ViewHolder(final View view) {
            super(view);

            fishLayout = (LinearLayout) view.findViewById(R.id.fishLayout);
            tv_android = (TextView) view.findViewById(R.id.tv_android);
            fishDescTV = (TextView) view.findViewById(R.id.fishDescTV);
            fishUploadedFromTV = (TextView) view.findViewById(R.id.fishUploadedFromTV);
            img_android = (ImageView) view.findViewById(R.id.img_android);

            fishLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(view.getContext(),FishUploadsDetails.class);
                    i.putExtra("load","2");
                    view.getContext().startActivity(i);
                }
            });
        }
    }
}
