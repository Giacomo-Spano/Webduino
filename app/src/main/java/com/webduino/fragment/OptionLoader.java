package com.webduino.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.webduino.ActionCommand;
import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.Services;
import com.webduino.WebduinoResponse;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.elements.Trigger;
import com.webduino.elements.Triggers;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.DecimalOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.IntegerOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.ListOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.scenarios.Action;
import com.webduino.webduinosystems.services.Service;
import com.webduino.zones.Zone;
import com.webduino.zones.ZoneSensor;
import com.webduino.zones.Zones;

import java.util.ArrayList;
import java.util.List;

public class OptionLoader {

    public void loadCommandType(OptionCardInfo optionCard, String type) {
        CharSequence[] cs = {"Attuatore", "Servizio", "Trigger"};
        String[] csvalue = {"actuator", "service", "trigger"};
        int value = 0;
        for (int i = 0; i < csvalue.length; i++) {
            if (csvalue[i].equals(type)) {
                value = i;
                break;
            }
        }
        optionCard.value = new ListOptionCardValue("Tipo", value, cs, csvalue);
    }

    public void loadTriggerCommand(String text, OptionCardInfo optionCard, boolean enabled) {
        CharSequence[] cs = {"Attiva", "Disattiva"};
        String[] csvalue = {"enabled", "disabled"};
        int current = 0;
        if (enabled)
            current = 0;
        else
            current = 1;
        optionCard.value = new ListOptionCardValue(text, current, cs, csvalue);
    }

    public void loadServiceId(OptionCardInfo optionCard, int serviceid) {

        if (Services.list.size() > 0) {
            CharSequence[] items;
            int[] itemValues;
            int i;
            int value = 0;
            items = new CharSequence[Services.list.size()];
            itemValues = new int[Services.list.size()];
            i = 0;
            for (Service service : Services.list) {
                items[i] = service.name;
                itemValues[i] = service.id;
                if (serviceid == service.id)
                    value = i;
                i++;
            }
            optionCard.value = new ListOptionCardValue("Servizio", value, items, itemValues);
        } else {
            optionCard.value = null;
        }
    }

    public void loadActuatorId(OptionCardInfo optionCard, int actuatorid) {


        if (Sensors.list.size() > 0) {
            CharSequence[] items = new CharSequence[Sensors.list.size()];
            int[] itemValues = new int[Sensors.list.size()];
            String[] itemStringValues = new String[Sensors.list.size()];
            int i = 0;
            int value = 0;
            for (Sensor sensor : Sensors.list) {
                items[i] = sensor.name;
                itemValues[i] = sensor.id;
                if (actuatorid == sensor.id)
                    value = i;
                i++;
            }
            optionCard.value = new ListOptionCardValue("Attuatore", value, items, itemValues);
        } else {
            optionCard.value = null;
        }
    }

    public void loadTriggerId(OptionCardInfo optionCard, int triggerid) {

        if (Triggers.list.size() > 0) {
            CharSequence[] items = new CharSequence[Triggers.list.size()];
            int[] itemValues = new int[Triggers.list.size()];
            int i = 0;
            int value = 0;
            for (Trigger trigger : Triggers.list) {
                items[i] = trigger.name;
                itemValues[i] = trigger.id;
                if (triggerid == trigger.id)
                    value = i;
                i++;
            }
            optionCard.value = new ListOptionCardValue("Trigger", value, items, itemValues);
        } else {
            optionCard.value = null;
        }
    }

    public void loadZoneId(OptionCardInfo optionCard, int zoneid) {

        if (Zones.list.size() > 0) {
            CharSequence[] items = new CharSequence[Zones.list.size()];
            int[] itemValues = new int[Zones.list.size()];
            int i = 0;
            int value = 0;
            for (Zone zone : Zones.list) {
                items[i] = zone.name;
                itemValues[i] = zone.id;
                if (zoneid == zone.id)
                    value = i;
                i++;
            }
            optionCard.value = new ListOptionCardValue("Zona", value, items, itemValues);
        } else {
            optionCard.value = null;
        }
    }

    public void loadZoneSensorId(OptionCardInfo optionCard, int zoneid, int zonesensorid) {

        Zone zone = Zones.getFromId(zoneid);
        if (zone != null && zone.zoneSensors.size() > 0) {
            CharSequence[] items = new CharSequence[zone.zoneSensors.size()];
            int[] itemValues = new int[zone.zoneSensors.size()];
            int i = 0;
            int value = 0;
            for (ZoneSensor zoneSensor : zone.zoneSensors) {
                items[i] = zoneSensor.name;
                itemValues[i] = zoneSensor.id;
                if (zonesensorid == zone.id)
                    value = i;
                i++;
            }
            optionCard.value = new ListOptionCardValue("Sensore zona", value, items, itemValues);
        } else {
            optionCard.value = null;
        }
    }

    public void loadActuatorCommand(OptionCardInfo optionCard, int actuatorid, String actuatorcommand) {
        CharSequence[] items;
        String[] itemStringValues;
        int i;

        Sensor actuator = Sensors.getFromId(actuatorid);
        if (actuator != null && actuator.actioncommandlist.size() > 0) {
            items = new CharSequence[actuator.actioncommandlist.size()];
            itemStringValues = new String[actuator.actioncommandlist.size()];
            i = 0;
            int value = 0;
            for (ActionCommand actionCommand : actuator.actioncommandlist) {
                items[i] = actionCommand.command;
                itemStringValues[i] = actionCommand.name;
                if (itemStringValues[i].equals(actuatorcommand))
                    value = i;
                i++;
            }
            optionCard.value = new ListOptionCardValue("Comando attuatore", value, items, itemStringValues);
        } else {
            optionCard.value = null;
        }
    }

    public void loadServiceCommand(OptionCardInfo optionCard, int serviceid, String servicecommand) {

        CharSequence[] items;
        String[] itemStringValues;
        Service service = Services.getFromId(serviceid);
        if (service != null && service.actioncommandlist.size() > 0) {
            items = new CharSequence[service.actioncommandlist.size()];
            itemStringValues = new String[service.actioncommandlist.size()];
            int i = 0;
            int value = 0;
            for (ActionCommand actionCommand : service.actioncommandlist) {
                items[i] = actionCommand.command;
                itemStringValues[i] = actionCommand.name;
                if (itemStringValues[i].equals(servicecommand))
                    value = i;
                i++;
            }
            optionCard.value = new ListOptionCardValue("Comando servizio", value, items, itemStringValues);
        } else {
            optionCard.value = null;
        }
    }

    public void loadDecimalValue(String text, OptionCardInfo optionCard, double value) {

        optionCard.value = new DecimalOptionCardValue(text, value);
    }

    public void loadIntValue(String text, OptionCardInfo optionCard, int value) {

        optionCard.value = new IntegerOptionCardValue(text, value);
    }

    public void loadStringValue(String text, OptionCardInfo optionCard, String value) {

        optionCard.value = new StringOptionCardValue(text, value);
    }
}
