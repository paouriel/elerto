package com.example.xxnrq.philvolcslivelist;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        try {
            addHeatMap();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //new AddressFinder(this).execute("https://maps.googleapis.com/maps/api/geocode/json?address=016+km+N+63%C2%B0+E+of+General+Luna+(Surigao+Del+Norte)&key=AIzaSyCM5geofTT_vYQVyIPsx5DD4UCzO8HY-Q0");
        //getLatLngFromAddress();
    }

    public void getLatLngFromAddress() {
        Geocoder coder = new Geocoder(this);
        double latitude = 0, longitude = 0;
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName("016 km N 63° E of General Luna (Surigao Del Norte)", 50);
            for (Address add : adresses) {
                //if (statement) {//Controls to ensure it is right address such as country etc.
                longitude = add.getLongitude();
                latitude = add.getLatitude();
                Toast.makeText(this, "long: " + longitude + "\nlat: " + latitude, Toast.LENGTH_SHORT).show();
                //}
            }
        } catch (Exception e) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        if (mMap != null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(09.85, 126.29), 10));
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    }

    HeatmapTileProvider mProvider;
    TileOverlay mOverlay;

    private LatLng computeCentroid(List<LatLng> points) {
        double latitude = 0;
        double longitude = 0;
        int n = points.size();

        for (LatLng point : points) {
            latitude += point.latitude;
            longitude += point.longitude;
        }

        return new LatLng(latitude/n, longitude/n);
    }

    private void addHeatMap() throws JSONException {
        List<LatLng> list = null;

        list = readItems(new JSONWriter().getData(MapsActivity.this));
        ArrayList<WeightedLatLng> weightedlist = new ArrayList<>();
        String display="";
        for(int x=0; x<list.size(); x++) {
            weightedlist.add(new WeightedLatLng(list.get(x)));
            display+="lat:"+list.get(x).latitude+",lng:"+list.get(x).longitude;
        }

        Toast.makeText(this, display, Toast.LENGTH_SHORT).show();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(computeCentroid(list), 5));

        // Create the gradient.
//        int[] colors = {
////                Color.rgb(102, 225, 0), // green
//                Color.rgb(0, 0, 0),
//                Color.rgb(255, 0, 0)    // red
//        };
//
//        float[] startPoints = {
//                0.2f, 1f
//        };
//
//        Gradient gradient = new Gradient(colors, startPoints);


        // Create the tile provider.
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(weightedlist)
                .radius(50)
                .opacity(1)
//                .gradient(gradient)
                .build();

        // Add the tile overlay to the map.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<LatLng> readItems(String jsonreq) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
//        InputStream inputStream = getResources().openRawResource(resource);
//        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(jsonreq);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }
}
