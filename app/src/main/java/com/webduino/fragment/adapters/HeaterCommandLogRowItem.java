package com.webduino.fragment.adapters;

import java.util.Date;

/**
 * Created by gs163400 on 24/08/2017.
 */

public class HeaterCommandLogRowItem extends ListItem {
    public Date date, enddate;
    public double targetvalue, temperature;
    public int duration;
    public boolean success;
    public String command;
}

