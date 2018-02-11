package com.webduino.scenarios;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gs163400 on 10/02/2018.
 */

public class ScenarioCalendar {
    public List<ScenarioTimeInterval> timeintervals = new ArrayList<>();
    public int priority;


    public ScenarioTimeInterval getTimeIntervalFromId(int id) {
        for (ScenarioTimeInterval timeInterval:timeintervals) {
            if (timeInterval.id == id)
                return timeInterval;
        }
        return null;
    }
}
