package com.webduino;

import com.webduino.webduinosystems.services.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class Devices {

    public static List<Device> list = new ArrayList<>();

    public static void add(Device device) {
        list.add(device);
    }

    public static void get(int index) {
        list.get(index);
    }

    public static Device getFromId(int id) {
        for(Device device : list) {
            if (device.id == id) {
                return device;
            }
        }
        return null;
    }
}
