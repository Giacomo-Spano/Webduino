package com.webduino;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class TutorialActivity extends FragmentActivity {

    public String Prova = "prova";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
    }
}