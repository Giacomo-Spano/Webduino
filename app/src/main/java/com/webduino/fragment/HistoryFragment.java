package com.webduino.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.WebduinoResponse;
import com.webduino.chart.HistoryChart;
import com.webduino.chart.HistoryData;
import com.webduino.elements.requestDataTask;

import java.util.List;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HistoryFragment extends Fragment {

    private HistoryChart hc;

    private LineChart chart;
    private List<HistoryData> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.fragment_history, container, false);


        chart = (LineChart) v.findViewById(R.id.chart);

        hc = new HistoryChart(getActivity(), chart);

        //refreshData();

        return v;
    }

    public void refreshData() {

       /* new requestDataTask(MainActivity.activity, new WebduinoResponse() {

            @Override
            public void processFinishObjectList(boolean response, int requestType, boolean error, String errorMessage) {

                //super.processFinishObjectList(response, requestType, error, errorMessage);

                //hc.drawChart(meteoDataList);


            }
        }, requestDataTask.REQUEST_DATALOG).execute();*/

        if (hc != null) {

          //  hc.drawChart(meteoDataList);
        }
    }

    public void update() {
        if (list != null)
            hc.drawChart(list);
    }

    public void setDataLogList(List<HistoryData> list) {
        this.list = list;
        update();
    }
}
