package com.example.xxnrq.philvolcslivelist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xxnrq on 22/07/2017.
 */

public class ListAdapter extends ArrayAdapter<ArrayList<String>> {

    Context context;


    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public ListAdapter(Context context, int resource, ArrayList<ArrayList<String>> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layout = LayoutInflater.from(getContext());
            view = layout.inflate(R.layout.list_live_results, null);
        }

        ArrayList<String> innerElements = getItem(position);

        if (innerElements != null) {
            ImageView mapImage = (ImageView) view.findViewById(R.id.mapImage);
            TextView datetime = (TextView) view.findViewById(R.id.datetime);
            TextView lat = (TextView) view.findViewById(R.id.lat);
            TextView lng = (TextView) view.findViewById(R.id.lng);
            TextView depth = (TextView) view.findViewById(R.id.depth);
            TextView mag = (TextView) view.findViewById(R.id.mag);
            TextView loc = (TextView) view.findViewById(R.id.loc);

            if (mapImage != null) {
                String staticImg = "http://maps.googleapis.com/maps/api/staticmap?&size=400x400&center=" + innerElements.get(1) + "," + innerElements.get(2) + "&zoom=6&" +
                        "maptype=roadmap&markers=size:large%7Ccolor:red%7C" + innerElements.get(1) + "," + innerElements.get(2) + "&key=" + context.getString(R.string.google_maps_key);
                try {
                    new DownloadImageTask(mapImage, context).execute(staticImg);
                } catch (Exception e) {
                    Log.d("philvolcs-livefeed", "image donwload error");
                }
            }

            if (datetime != null) {
                datetime.setText(innerElements.get(0));
            }

            if (lat != null) {
                lat.setText(innerElements.get(1) + "°");
            }

            if (lng != null) {
                lng.setText(innerElements.get(2) + "°");
            }

            if (depth != null) {
                depth.setText(innerElements.get(3));
            }

            if (mag != null) {
                mag.setText(innerElements.get(4));
            }

            if (loc != null) {
                loc.setText(innerElements.get(5));
            }

        }
        return view;
    }
}
