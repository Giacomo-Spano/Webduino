package com.webduino.wizard;

//import android.app.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webduino.R;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HeaterWizardFragment_Summary extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.heaterwizardfragmentsummary, container, false);



        return v;
    }
}
