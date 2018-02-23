package com.webduino.fragment.cardinfo;

import com.webduino.elements.TimeRange;

import java.util.Date;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class TimeRangeCardInfo extends CardInfo {
    public Date startTime, endTime;

    public TimeRangeCardInfo() {
        type = TYPE_PROGRAMTIMERANGE;
    }
}