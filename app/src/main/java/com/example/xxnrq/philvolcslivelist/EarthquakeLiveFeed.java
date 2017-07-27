package com.example.xxnrq.philvolcslivelist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by xxnrq on 22/07/2017.
 */

public class EarthquakeLiveFeed extends AsyncTask<Void, Void, Void> {
    TextView dataResult;
    Context context;
    String result = "";
    ListView listView;
    ArrayList<ArrayList<String>> dataElements = new ArrayList<>();
    JSONArray company = new JSONArray();
    JSONObject obj2 = new JSONObject();
    ProgressDialog pd;

    public EarthquakeLiveFeed(TextView dataResult, Context context, ListView listView) {
        this.dataResult = dataResult;
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        try {
            pd = new ProgressDialog(context);
            pd.setCancelable(false);
            pd.setTitle("Retrieving data");
            pd.setMessage("Please wait...");
            pd.show();
        } catch(Exception e) {
            Log.d("earthquake-livefeed", "error loading progress dialog");
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        String html = "http://www.phivolcs.dost.gov.ph/html/update_SOEPD/EQLatest.html";
        try {
            Document doc = Jsoup.connect(html).get();
            Elements tableElements = doc.select("table");

            Elements tableHeaderEles = tableElements.select("thead tr th");
            //System.out.println("headers");
            for (int i = 0; i < tableHeaderEles.size(); i++) {
                Toast.makeText(context, tableHeaderEles.get(i).text(), Toast.LENGTH_SHORT).show();
                if (!tableHeaderEles.get(i).text().contentEquals("Seismological Observation and Earthquake Prediction Division")
                        || !tableHeaderEles.get(i).text().contentEquals("SEISMICITY MAPS")) {
                    result += tableHeaderEles.get(i).text() + "\n";         ///GETTING HEADERS
                } else {
                    result += "\n";
                }
            }

            Elements tableRowElements = tableElements.select(":not(thead) tr");

            for (int i = 0; i < tableRowElements.size(); i++) {         //OUTER
                if (i > 1 && i < 34) {
                    Element row = tableRowElements.get(i);
                    if (i == 2) {
                        result += "Headers: " + "\n";
                    } else if (i == 3) {
                        result += "Month: " + "\n";
                    } else {
                        result += "Row #" + (i) + "\n";
                        dataElements.add(new ArrayList<String>());
                    }

                    Elements rowItems = row.select("td");
                    for (int j = 0; j < rowItems.size(); j++) {                 //INNER ELEMENTS
                        if (!rowItems.get(j).text().equals(" ")) {
                            if (i > 3) {
                                switch (j) {
                                    case 0:
                                        result += "Date/Time: ";
                                        break;

                                    case 1:
                                        result += "Latitude (ºN): ";
                                        break;

                                    case 2:
                                        result += "Longitude (ºE): ";
                                        break;

                                    case 3:
                                        result += "Depth (km): ";
                                        break;

                                    case 4:
                                        result += "Magnitude: ";
                                        break;

                                    case 5:
                                        result += "Location: ";
                                        break;
                                }
                                Log.d("hehehe", "i - "+i+" j - "+j+"\n"+rowItems.get(j).text());
                                try {
                                    dataElements.get(i - 4).add(rowItems.get(j).text());
                                    if (j == 0){
                                        obj2 = new JSONObject();
                                    } else if(j == 1) {
                                        obj2.put("lat", rowItems.get(j).text());
                                    } else if(j == 2) {
                                        obj2.put("lng", rowItems.get(j).text());
                                    }
                                } catch(Exception e) {
                                    Log.d("hehe", e.toString());
                                }
                            }
                            result += rowItems.get(j).text() + "\n";    ///GETTING CELL VALUES
                        }
                    }
                    company.put(obj2);
                    result += "\n";
                }
            }

            new JSONWriter().saveData(context, company.toString());
        } catch (IOException e) {
            Log.d("asynctask-error", "error loading data");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        if(pd.isShowing()) {
            pd.dismiss();
        }
        listView.setAdapter(new ListAdapter(context, R.layout.list_live_results, dataElements));
    }
}
