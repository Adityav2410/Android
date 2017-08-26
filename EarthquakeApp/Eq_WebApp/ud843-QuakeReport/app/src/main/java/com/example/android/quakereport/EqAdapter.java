package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by adityav on 8/21/17.
 */

public class EqAdapter extends ArrayAdapter<EqInfo>{

    DecimalFormat decimalFormat;
    public EqAdapter(@NonNull Context context, @NonNull List<EqInfo> objects) {
        super(context, 0, objects);
        decimalFormat  =   new DecimalFormat("0.0");
    }

    private int getBackgroundColor(Double magnitude){
        int magnitudeColorResourceId = R.color.magnitude1;
        switch( (int) Math.floor(magnitude) ){
            case 0: break;
            case 1: magnitudeColorResourceId=(R.color.magnitude1);   break;
            case 2: magnitudeColorResourceId=(R.color.magnitude2);   break;
            case 3: magnitudeColorResourceId=(R.color.magnitude3);   break;
            case 4: magnitudeColorResourceId=(R.color.magnitude4);   break;
            case 5: magnitudeColorResourceId=(R.color.magnitude5);   break;
            case 6: magnitudeColorResourceId=(R.color.magnitude6);   break;
            case 7: magnitudeColorResourceId=(R.color.magnitude7);   break;
            case 8: magnitudeColorResourceId=(R.color.magnitude8);   break;
            case 9: magnitudeColorResourceId=(R.color.magnitude9);   break;
            default: magnitudeColorResourceId=(R.color.magnitude10plus);
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent, false);

        EqInfo      currEqInfo          =       getItem(position);
        TextView    tv_magnitude        =       (TextView)  listItemView.findViewById(R.id.tv_magnitude);
        TextView    tv_citySecondary    =       (TextView)  listItemView.findViewById(R.id.tv_citySecondary);
        TextView    tv_cityPrimary      =       (TextView)  listItemView.findViewById(R.id.tv_cityPrimary);
        TextView    tv_date             =       (TextView)  listItemView.findViewById(R.id.tv_date);
        TextView    tv_time             =       (TextView)  listItemView.findViewById(R.id.tv_time);

        // Format magnitude in the desired form and set background color as per the magnitude value
        String      magnitudeString     =       decimalFormat.format(currEqInfo.getMagnitude());
        tv_magnitude.setText(magnitudeString);
        GradientDrawable    magnitudeBg =       (GradientDrawable) tv_magnitude.getBackground();
        magnitudeBg.setColor(getBackgroundColor(currEqInfo.getMagnitude()));

        tv_citySecondary.setText(currEqInfo.getmCitySecondary());
        tv_cityPrimary.setText(currEqInfo.getCityPrimary());
        tv_date.setText(String.valueOf(currEqInfo.getDate()));
        tv_time.setText(String.valueOf(currEqInfo.getTime()));
        return listItemView;
    }
}
