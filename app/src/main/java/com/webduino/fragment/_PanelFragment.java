package com.webduino.fragment;


//import android.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.webduino.R;


public class _PanelFragment extends Fragment {

    private boolean viewCreated = false;
    OnListener mCallback;
    private LinearLayout mcontainer;
    private MeteoDataList meteoDataList = new MeteoDataList();

    private class MeteoDataList {

    }

    // Container Activity must implement this interface
    public interface OnListener {
        void onSpotClick(long spotId);
        void onEnablePanelRefreshButtonRequest();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.fragment_controlpanel, container, false);


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}