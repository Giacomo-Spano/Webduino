package com.webduino;

import com.webduino.webduinosystems.services.Service;
import com.webduino.zones.Zone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 18/12/2016.
 */

public class Services {

    public static List<Service> list = new ArrayList<>();

    public static void add(Service service) {
        list.add(service);
    }

    public static void get(int index) {
        list.get(index);
    }

    public static Service getFromId(int id) {
        for(Service service : list) {
            if (service.id == id) {
                return service;
            }
        }
        return null;
    }


}
