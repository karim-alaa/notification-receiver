package com.example.mohamed.anticheating;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Mohamed on 26/02/2017.
 */
public class ManualNotificationAdapter extends ArrayAdapter<ManualNotification> {
    Context context;

    private static LayoutInflater inflater=null;

    public ManualNotificationAdapter(Context c, ArrayList<ManualNotification> notifications) {
        super(c, 0 , notifications);
        this.context=c;
    }

    private class ViewHolder {

        ImageView imgeVw;
        TextView txtVwHeader;
        TextView txtVwContent;
     //   Button btnignor;
       // Button btnConfirm;
    }



    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;

        ManualNotification rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.raw, null);
            holder = new ViewHolder();

            holder.imgeVw = (ImageView) convertView.findViewById(R.id.imgVwVideo);
            holder.txtVwHeader = (TextView) convertView.findViewById(R.id.txtVwHeader);
            holder.txtVwContent = (TextView) convertView.findViewById(R.id.txtVwContent);
          //  holder.btnConfirm = (Button) convertView.findViewById(R.id.btnSeeVideo);
           // holder.btnignor = (Button) convertView.findViewById(R.id.btnIgnore);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            convertView.setTag(holder);
        }


            holder.txtVwHeader.setText(rowItem.getHeader());
            holder.txtVwContent.setText(rowItem.getContent());
            if (rowItem.isSeen()) {
                convertView.setBackgroundColor(Color.parseColor("#66F44336"));
            }

        Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.imgeVw);

        return convertView;
    }



}
