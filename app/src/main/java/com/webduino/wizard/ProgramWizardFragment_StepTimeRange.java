package com.webduino.wizard;

//import android.app.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webduino.fragment.DatePickerFragment;
import com.webduino.R;
import com.webduino.elements.TimeRange;
import com.webduino.fragment.adapters.TimeRangeAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.TimeRangeCardInfo;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class ProgramWizardFragment_StepTimeRange extends Fragment implements TimeRangeAdapter.OnListener, DatePickerFragment.DataPickerListener {

    public static final int HEATERWIZARD_REQUEST = 1;  // The request code

    private List<CardInfo> list;
    private TimeRangeAdapter timeRangeAdapter;
    private List<TimeRange> timeRanges = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.wizard_fragment_program_timerange, container, false);

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        recList.setLayoutManager(gridLayoutManager);


        timeRangeAdapter = new TimeRangeAdapter(createTimeRangeList());
        recList.setAdapter(timeRangeAdapter);

        timeRangeAdapter.setListener(this);

        return v;
    }

    public void init(List<TimeRange> list) {
        timeRanges = list;
    }

    public void update() {

        list = createTimeRangeList();
        timeRangeAdapter.swap(list);
    }

    public List<CardInfo> createTimeRangeList() {

        List<CardInfo> result = new ArrayList<CardInfo>();

        for (TimeRange timerange : timeRanges) {

            try {
                TimeRangeCardInfo ci = timeRangeCardInfoFromTimeRange( timerange);
                result.add(ci);
            } catch (Exception e) {

            }
        }
        return result;
    }

    @NonNull
    private TimeRangeCardInfo timeRangeCardInfoFromTimeRange(TimeRange timeRange) {
        TimeRangeCardInfo ci = new TimeRangeCardInfo();
        //ci.timeRange = timeRange;
        return ci;
    }

    @Override
    public void setDate(Date date) {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void onTimeRangeClick(int id) {
    }

    @Override
    public void onAddTimeRangeBelow(int id) {
        int index = 0;
        for (TimeRange tr : timeRanges) {
            if (tr.ID == id) {
                TimeRange newTimeRange = new TimeRange();
                newTimeRange.starTime = tr.endTime;
                newTimeRange.endTime = newTimeRange.starTime;
                newTimeRange.temperature = 15.0;
                newTimeRange.name = "nome";
                newTimeRange.ID = getMaxId() + 1;
                timeRanges.add(index+1,newTimeRange);

                TimeRange lastTimeRange = timeRanges.get(timeRanges.size()-1);
                lastTimeRange.endTime = Time.valueOf("23:59:00");
                update();
                break;
            }
            index++;
        }
    }

    @Override
    public void onChangeTimeRange(TimeRange timeRange) {
        int index = 0;
        for (TimeRange tr : timeRanges) {
            if (tr.ID == timeRange.ID) {

                if (index >= timeRanges.size()-1) {
                    // se è lultimo non fare nulla
                } else {

                    TimeRange next = timeRanges.get(index + 1);
                    next.starTime = timeRange.endTime;

                    /*for (int i = index + 1; i < timeRanges.size()-1; i++) {
                        TimeRange next = timeRanges.get(i);
                        if (next.starTime.getTime() < timeRange.endTime.getTime())
                            next.starTime = timeRange.endTime;

                        if (next.endTime.getTime() < timeRange.endTime.getTime())
                            next.starTime = timeRange.endTime;
                    }*/
                }
                update();
                return;
            }
            index++;
        }
    }

    public int getMaxId() {
        int id = 0;
        for (TimeRange tr : timeRanges) {
            if (tr.ID > id) {
                id = tr.ID;
            }
        }
        return id;
    }

    @Override
    public void onDeleteTimeRange(int id) {
        int index = 0;
        for (TimeRange tr : timeRanges) {
            if (tr.ID == id) {

                if (timeRanges.size() < 2) {
                    return;
                }

                if (index > timeRanges.size()-1) {
                    timeRanges.get(index+1).starTime = timeRanges.get(index).starTime;
                    return;
                }

                timeRanges.remove(index);
                update();
                break;
            }
            index++;
        }
    }

    public List<TimeRange> getTimeRanges() {
        return timeRanges;
    }
}
