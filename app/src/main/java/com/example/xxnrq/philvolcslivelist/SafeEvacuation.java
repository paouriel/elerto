package com.example.xxnrq.philvolcslivelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SafeEvacuation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView emptyHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_evacuatio);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            emptyHeader.setVisibility(View.VISIBLE);
            Log.d("mapfragment-async", "error loading");
        }

        emptyHeader = (TextView) findViewById(R.id.emptyHeader);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(14.553886, 121.050232);
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Nearest Safe Zone").snippet(""));
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(sydney, 17)));
            mMap.setMinZoomPreference(17);
        } catch (Exception e) {
            emptyHeader.setVisibility(View.VISIBLE);
            Log.d("mapfragment-ready", "googlemap not loading...");
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
