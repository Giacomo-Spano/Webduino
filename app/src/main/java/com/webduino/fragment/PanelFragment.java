package com.webduino.fragment;

import android.app.Fragment;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.support.v4.app.Fragment;

import com.webduino.R;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class PanelFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.fragment_controlpanel, container, false);

        return v;
    }
}
