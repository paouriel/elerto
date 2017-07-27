package com.example.xxnrq.philvolcslivelist;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends ListActivity implements SensorEventListener {
    MediaPlayer mediaPlayer;
    SensorManager senSensorManager;
    Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 50;
    private int ix = 0, iy = 0, iz = 0;
    private float j = 0;
    private float x, y, z;
    private float magnitude;

    private int time = 0;
    private int getId;

    GraphView graph;
    LineGraphSeries<DataPoint> series1, series2, series3;

    List<Float> xaxis, yaxis, zaxis;
    Vibrator vib;

    TextView magnitudeLevel;
    TableLayout tableLayout;
    TableRow tr1, tr2, tr3, tr4, tr5, tr6, tr7, tr8, tr9, tr10;
    List<Integer> id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xaxis = new ArrayList<>();
        yaxis = new ArrayList<>();
        zaxis = new ArrayList<>();
        id = new ArrayList<>();

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        try {
            TwitterConfig config = new TwitterConfig.Builder(this)
                    .logger(new DefaultLogger(Log.DEBUG))
                    .twitterAuthConfig(new TwitterAuthConfig("mCeEg179kedrcBVp15tqgrf9q", "ugMhOdwGPl9ZBOgr5lSodzpBgCleZCk3NkbilVSPUxZ1mbcMvm\n"))
                    .debug(true)
                    .build();
            Twitter.initialize(config);


        } catch (Exception e) {
            Log.d("twitter", "timeline/adapter error");
        }
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("phivolcs_dost")
                .userId((long) 802064401)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);
        graph = (GraphView) findViewById(R.id.graph);

        series1 = new LineGraphSeries<DataPoint>();
        series2 = new LineGraphSeries<DataPoint>();
        series3 = new LineGraphSeries<DataPoint>();

        series1.setColor(Color.RED);
        series2.setColor(Color.BLUE);
        series3.setColor(Color.GREEN);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        magnitudeLevel = (TextView) findViewById(R.id.magnitudeLevel);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        tr1 = (TableRow) findViewById(R.id.row1);
        tr6 = (TableRow) findViewById(R.id.row6);
        tr2 = (TableRow) findViewById(R.id.row2);
        tr7 = (TableRow) findViewById(R.id.row7);
        tr3 = (TableRow) findViewById(R.id.row3);
        tr8 = (TableRow) findViewById(R.id.row8);
        tr4 = (TableRow) findViewById(R.id.row4);
        tr9 = (TableRow) findViewById(R.id.row9);
        tr5 = (TableRow) findViewById(R.id.row5);
        tr10 = (TableRow) findViewById(R.id.row10);

        thread();


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


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = (Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000);
                magnitude = (float) Math.sqrt(Math.abs(Math.pow(x, 2)) + Math.abs(Math.pow(y, 2)));
                magnitudeLevel.setText(String.format("%.2f", (magnitude)));

                if (speed > SHAKE_THRESHOLD) {

                    if (xaxis.size() <= 10) {
                        xaxis.add(Math.abs(x));

                        for (int v = 0; v < 5; v++) {
                            if (xaxis.size() - 1 >= 3) {
                                try {
                                    float getX = xaxis.get(v);
                                    float getXb = xaxis.get(v - 1);
                                    float getXa = xaxis.get(v + 1);
                                    if (Math.round(getX) == Math.round(Math.max(getXa, getXb))
                                            && Math.round(getX) == Math.round(Math.min(getXa, getXb))) {

                                        if (ix >= 3) {
                                            detected();
                                            vib.vibrate(1000);
                                            ix = 0;
                                            iy = 0;
                                            iz = 0;
                                            xaxis.clear();
                                            mediaPlayer.prepare();
                                            mediaPlayer.start();
                                            dialog();
                                        } else {
                                            ix++;
                                        }

                                    } else {
                                        ix = 0;
                                        iy = 0;
                                        iz = 0;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        for (int v = xaxis.size(); v >= 10; v--) {
                            xaxis.remove(0);
                        }
                    }

                    if (yaxis.size() <= 10) {
                        yaxis.add(Math.abs(y));

                        for (int v = 0; v < 5; v++) {
                            if (yaxis.size() - 1 >= 3) {
                                try {
                                    float getY = yaxis.get(v);
                                    float getYb = yaxis.get(v - 1);
                                    float getYa = yaxis.get(v + 1);
                                    if (Math.floor(getY) == Math.floor(Math.max(getYa, getYb))
                                            && Math.ceil(getY) == Math.ceil(Math.min(getYa, getYb))) {
                                        if (iy >= 3) {
                                            vib.vibrate(1000);
                                            detected();
                                            ix = 0;
                                            iy = 0;
                                            iz = 0;
                                            yaxis.clear();
                                            mediaPlayer.start();
                                            dialog();
                                        } else {
                                            iy++;
                                        }
                                    } else {
                                        ix = 0;
                                        iy = 0;
                                        iz = 0;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        for (int v = yaxis.size(); v >= 10; v--) {
                            yaxis.remove(0);
                        }
                    }

                    if (zaxis.size() <= 10) {
                        zaxis.add(Math.abs(z));

                        for (int v = 0; v < 5; v++) {
                            if (zaxis.size() - 1 >= 3) {
                                try {
                                    float getZ = zaxis.get(v);
                                    float getZb = zaxis.get(v - 1);
                                    float getZa = zaxis.get(v + 1);
                                    if (Math.floor(getZ) == Math.floor(Math.max(getZa, getZb))
                                            && Math.ceil(getZ) == Math.ceil(Math.min(getZa, getZb))) {
                                        if (iz >= 3) {
                                            vib.vibrate(1000);
                                            detected();
                                            ix = 0;
                                            iy = 0;
                                            iz = 0;
                                            zaxis.clear();
                                            mediaPlayer.start();
                                            dialog();
                                        } else {
                                            iz++;
                                        }
                                    } else {
                                        ix = 0;
                                        iy = 0;
                                        iz = 0;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        for (int v = zaxis.size(); v >= 10; v--) {
                            zaxis.remove(0);
                        }
                    }

                    last_x = x;
                    last_y = y;
                    last_z = z;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void thread() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                for (; ; ) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            j += 0.1;
                            graph.willNotCacheDrawing();
                            graph.destroyDrawingCache();
                            graph.removeAllSeries();

                            series1.appendData(new DataPoint(j, x), true, 50);
                            series2.appendData(new DataPoint(j, y), true, 50);
                            series3.appendData(new DataPoint(j, z), true, 50);

                            graph.addSeries(series1);
                            graph.addSeries(series2);
                            graph.addSeries(series3);
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();

    }

    public void timer() {
        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    time = 0;
                    do {
                        sleep(0);
                        time += 20;
                        if (time >= 120) {
                            ix = 0;
                            iy = 0;
                            iz = 0;
                        }
                    } while (time < 560);
                    ix = 0;
                    iy = 0;
                    iz = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        logoTimer.start();
    }

    public void detected() {

        if (magnitude <= 1.9) {
            if (id.size() == 1) {
                TableRow table = (TableRow) findViewById(id.get(0));
                table.setBackgroundColor(0);
                id.clear();
            }
            tr1.setBackgroundColor(Color.LTGRAY);
            getId = tr1.getId();
            id.add(getId);

        } else if (magnitude >= 2 && magnitude <= 2.9) {
            if (id.size() == 1) {
                TableRow table = (TableRow) findViewById(id.get(0));
                table.setBackgroundColor(0);
                id.clear();
            }
            tr2.setBackgroundColor(Color.LTGRAY);
            getId = tr2.getId();
            id.add(getId);

        } else if (magnitude >= 3 && magnitude <= 3.9) {
            if (id.size() == 1) {
                TableRow table = (TableRow) findViewById(id.get(0));
                table.setBackgroundColor(0);
                id.clear();
            }
            tr3.setBackgroundColor(Color.LTGRAY);
            getId = tr3.getId();
            id.add(getId);

        } else if (magnitude >= 4 && magnitude <= 4.9) {
            if (id.size() == 1) {
                TableRow table = (TableRow) findViewById(id.get(0));
                table.setBackgroundColor(0);
                id.clear();
            }
            tr4.setBackgroundColor(Color.LTGRAY);
            getId = tr4.getId();
            id.add(getId);

        } else if (magnitude >= 5 && magnitude <= 5.9) {
            if (id.size() == 1) {
                TableRow table = (TableRow) findViewById(id.get(0));
                table.setBackgroundColor(0);
                id.clear();
            }
            tr5.setBackgroundColor(Color.LTGRAY);
            getId = tr5.getId();
            id.add(getId);

        } else if (magnitude >= 6 && magnitude <= 6.9) {
            if (id.size() == 1) {
                TableRow table = (TableRow) findViewById(id.get(0));
                table.setBackgroundColor(0);
                id.clear();
            }
            tr6.setBackgroundColor(Color.LTGRAY);
            getId = tr6.getId();
            id.add(getId);

        } else if (magnitude >= 7 && magnitude <= 7.9) {
            if (id.size() == 1) {
                TableRow table = (TableRow) findViewById(id.get(0));
                table.setBackgroundColor(0);
                id.clear();
            }
            tr7.setBackgroundColor(Color.LTGRAY);
            getId = tr7.getId();
            id.add(getId);

        } else if (magnitude >= 8 && magnitude <= 8.9) {
            if (id.size() == 1) {
                TableRow table = (TableRow) findViewById(id.get(0));
                table.setBackgroundColor(0);
                id.clear();
            }
            tr8.setBackgroundColor(Color.LTGRAY);
            getId = tr8.getId();
            id.add(getId);

        } else if (magnitude >= 9 && magnitude <= 9.9) {
            if (id.size() == 1) {
                TableRow table = (TableRow) findViewById(id.get(0));
                table.setBackgroundColor(0);
                id.clear();
            }
            tr9.setBackgroundColor(Color.LTGRAY);
            getId = tr9.getId();
            id.add(getId);

        } else {
            if (id.size() == 1) {
                TableRow table = (TableRow) findViewById(id.get(0));
                table.setBackgroundColor(0);
                id.clear();
            }
            tr10.setBackgroundColor(Color.LTGRAY);
            getId = tr10.getId();
            id.add(getId);

        }
    }

    public void dialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Alert");
        dialog.setMessage("Earthquake detected\n DUCK, COVER AND HOLD NOW!");
        dialog.setPositiveButton("Snooze", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                action();
                //do action
            }
        });

        AlertDialog alert1 = dialog.create();
        alert1.show();
    }

    public void action() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirmation");
        dialog.setMessage("Do you need help?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //do action
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainMenu.this, SafeEvacuation.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                //do action
            }
        });

        AlertDialog alert1 = dialog.create();
        alert1.show();
    }
}
