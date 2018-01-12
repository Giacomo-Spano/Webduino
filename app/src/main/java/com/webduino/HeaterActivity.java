package com.webduino;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.webduino.fragment.HeaterTabsPagerAdapter;

/**
 * Created by giaco on 07/01/2018.
 */

public class HeaterActivity extends AppCompatActivity {

    private int actuatorId;
    private int shieldId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getIntent().getExtras();
        actuatorId = bundle.getInt("actuatorid");
        shieldId = bundle.getInt("shieldid");

        setContentView(R.layout.activity_heater);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        HeaterTabsPagerAdapter adapter = new HeaterTabsPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    public int getSensorId() {
        return actuatorId;
    }
}
