package com.ss.android.allepyfish.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.model.RowItem;

import java.util.List;

/**
 * Created by dell on 4/30/2017.
 */

public class CustomListViewBoysAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomListViewBoysAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        CheckBox txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CustomListViewBoysAdapter.ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_boys, null);
            holder = new CustomListViewBoysAdapter.ViewHolder();
            holder.txtDesc = (CheckBox) convertView.findViewById(R.id.desc_boys_select);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title_boys_info);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon_boys_info);
            convertView.setTag(holder);
        } else
            holder = (CustomListViewBoysAdapter.ViewHolder) convertView.getTag();

//        holder.txtDesc.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());

        return convertView;
    }
}