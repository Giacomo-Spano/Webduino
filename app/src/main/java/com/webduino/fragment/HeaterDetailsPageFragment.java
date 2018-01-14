package com.webduino.fragment;

import com.webduino.HeaterActivity;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.Sensors;
import com.webduino.fragment.adapters.HeaterDataHeaderItem;
import com.webduino.fragment.adapters.HeaterDataRowItem;
import com.webduino.fragment.adapters.HeaterListListener;
import com.webduino.fragment.adapters.ListItem;

import java.util.ArrayList;

/**
 * Created by giaco on 07/01/2018.
 */

public class HeaterDetailsPageFragment extends PageFragment {


    public static PageFragment newInstance() {
        PageFragment fragment = new HeaterDetailsPageFragment();
        return fragment;
    }

    @Override
    public void refreshData() {

        HeaterActivity a = (HeaterActivity) getActivity();
        int id = a.getSensorId();
        HeaterActuator heater = (HeaterActuator) Sensors.getFromId(id);

        if (heater == null)
            return;

        if (!adaptercreated)
            return;

        list = new ArrayList<>();
        int count = 0;

        // Heater
        HeaterDataHeaderItem hi = new HeaterDataHeaderItem();
        hi.type = ListItem.HeaterDataHeader;
        hi.description = "Stato";
        list.add(hi);
        // id
        HeaterDataRowItem mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Id";
        mi.value = "" + heater.getId();
        list.add(mi);
        // shieldid
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "ShieldId";
        mi.value = "" + heater.getShieldId();
        list.add(mi);
        // name
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Nome";
        mi.value = "" + heater.getName();
        list.add(mi);
        // stato
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Stato";
        mi.value = "" + heater.getStatus();
        list.add(mi);
        // relestatus
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Relè";
        if (heater.getReleStatus())
            mi.value = "ACCESO";
        else
            mi.value = "SPENTO";
        list.add(mi);
        // temperature
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Temperatura";
        mi.value = "" + heater.getRemoteTemperature() + "°C " + heater.getLastTemperatureUpdate();
        list.add(mi);
        // zone
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Zona";
        mi.value = "" + heater.getZoneId();
        list.add(mi);
        // target
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Target";
        mi.value = "" + heater.getTarget() + "°C";
        list.add(mi);
        // last command
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Ultimo comando";
        mi.value = heater.getLastCommandDate();
        list.add(mi);

        // Heater
        hi = new HeaterDataHeaderItem();
        hi.type = ListItem.HeaterDataHeader;
        hi.description = "Programma";
        list.add(hi);
        // action
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Action";
        mi.value = "" /*+ /*heater.get*/;
        list.add(mi);
        // action
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Fine programma";
        mi.value = heater.getEndDate();
        list.add(mi);
        // action
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Durata";
        mi.value = heater.getDuration();
        list.add(mi);
        // action
        mi = new HeaterDataRowItem();
        mi.type = ListItem.HeaterDataRow;
        mi.description = "Tempo rimanente";
        mi.value = heater.getRemainig();
        list.add(mi);

        HeaterListListener.HeaterListArrayAdapter adapter = new HeaterListListener.HeaterListArrayAdapter(getActivity(), list, listener);
        listView.setAdapter(adapter);
    }
}
