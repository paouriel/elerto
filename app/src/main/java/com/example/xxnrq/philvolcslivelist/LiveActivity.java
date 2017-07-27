package com.example.xxnrq.philvolcslivelist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LiveActivity extends Activity {

    TextView onlineData;
    ListView itemListView;
    ArrayList<ArrayList<String>> liveDataResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        itemListView = (ListView) findViewById(R.id.itemListView);
        itemListView.setAdapter(new ListAdapter(this, R.layout.list_live_results, liveDataResults));

        TextView emptyHeader = (TextView) findViewById(android.R.id.empty);
        itemListView.setEmptyView(emptyHeader);

        try {
            EarthquakeLiveFeed equakeFeed = new EarthquakeLiveFeed(onlineData, LiveActivity.this, itemListView);
            equakeFeed.execute();
        } catch (Exception e) {
            Log.d("philvolcs-livefeed", "live feed execution error.");
        }
    }

    public void goToHome(View v) {
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void goToUpdt(View v) {
        Intent intent = new Intent(getApplicationContext(), LiveActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void goToTips(View v) {
        Intent intent = new Intent(getApplicationContext(), FeatureSliderAcitivty.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void goToSafe(View v) {
        Intent intent = new Intent(getApplicationContext(), SafeEvacuation.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}


