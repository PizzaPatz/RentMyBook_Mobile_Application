package com.app.csulb.rentmybookfinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
    Show owner of the book
 */

public class OwnerAdapter extends BaseAdapter {

    Context context;
    List<OwnerList> owners;

    public OwnerAdapter(Context context, List<OwnerList> owners_list){
        this.context = context;
        this.owners = owners_list;
    }
    @Override
    public int getCount() {
        return owners.size();
    }

    @Override
    public Object getItem(int i) {
        return owners.get(i);
    }

    @Override
    public long getItemId(int i) {
        return owners.indexOf(getItem(i));
    }

    private class ViewHolder{
        TextView owner;
        TextView price;
        TextView condition;
        TextView available;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder;

        LayoutInflater inflat = (LayoutInflater)context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null){
            view = inflat.inflate(R.layout.owners_list, null);
            holder = new ViewHolder();

            holder.owner = (TextView) view
                    .findViewById(R.id.owner);

            holder.price = (TextView) view
                    .findViewById(R.id.price);

            holder.condition = (TextView) view
                    .findViewById(R.id.condition);

            holder.available = (TextView) view
                    .findViewById(R.id.available);

            final OwnerList position= owners.get(i);

            holder.owner.setText(position.getName());
            holder.price.setText(position.getPrice());
            holder.available.setText(position.getAvailable());
            holder.condition.setText(position.getCondition());
            if (holder.available.getText().toString().equalsIgnoreCase("Yes")) {
                holder.available.setTextColor(Color.BLUE);
                holder.available.setPaintFlags(holder.available.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                holder.available.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //System.out.println(holder.owner.getText().toString() + " was clicked.";
                        AlertDialog.Builder buildPay = new AlertDialog.Builder(context);
                        buildPay.setMessage("Do you wish to continue with PayPal?")
                                .setPositiveButton("PayPal", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent startPayment = new Intent(context, PaymentDetails.class);
                                        startPayment.putExtra("owner_name", holder.owner.getText().toString());
                                        startPayment.putExtra("book_price", holder.price.getText().toString());
                                        context.startActivity(startPayment);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        final AlertDialog build = buildPay.create();
                        build.setTitle("Payment");
                        build.setOnShowListener( new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                build.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                                build.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                            }
                        });
                        build.show();
                    }
                });
            }

        }

        return view;
    }
}