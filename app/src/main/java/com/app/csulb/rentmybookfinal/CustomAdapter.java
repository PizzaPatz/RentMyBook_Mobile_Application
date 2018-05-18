package com.app.csulb.rentmybookfinal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

// Contains relevant information of the rewards
public class CustomAdapter extends BaseAdapter {

    Context context;
    List<RowItem> items;

    public CustomAdapter(Context context, List<RowItem> list_items){
        this.context = context;
        this.items = list_items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.indexOf(getItem(i));
    }

    private class ViewHolder{
        ImageView item_image;
        TextView item_name;
        TextView item_point;

    }

    // Display the view of the reward
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder;

        LayoutInflater inflat = (LayoutInflater)context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null){
            view = inflat.inflate(R.layout.listview_layout, null);
            holder = new ViewHolder();

            holder.item_name = (TextView) view
                    .findViewById(R.id.item_name);

            holder.item_point = (TextView) view
                    .findViewById(R.id.item_point);

            holder.item_image = (ImageView) view
                    .findViewById(R.id.item_image);

            RowItem row_pos = items.get(i);

            holder.item_image.setImageResource(row_pos.getImage());
            holder.item_name.setText(row_pos.getName());
            holder.item_point.setText(row_pos.getPoint());
        }

        return view;
    }
}
