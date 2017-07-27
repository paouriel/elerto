package com.example.xxnrq.philvolcslivelist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import github.chenupt.springindicator.viewpager.ScrollerViewPager;
import me.relex.circleindicator.CircleIndicator;

public class FeatureSliderAcitivty extends Activity {
    Button next,skip;
    ScrollerViewPager viewPager;
    CircleIndicator indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        viewPager = (ScrollerViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.setBackgroundColor(Color.BLACK);

    }

    public void next(View v){
        if(viewPager.getCurrentItem() != 11){
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
        }else{
            //do action
            startActivity(new Intent(this, MainMenu.class));
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
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