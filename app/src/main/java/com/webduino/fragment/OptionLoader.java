package com.webduino.fragment;

import com.webduino.ActionCommand;
import com.webduino.Services;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.elements.Trigger;
import com.webduino.elements.Triggers;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.DecimalOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.IntegerOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.ListOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.webduinosystems.WebduinoSystem;
import com.webduino.webduinosystems.WebduinoSystemActuator;
import com.webduino.webduinosystems.WebduinoSystemService;
import com.webduino.webduinosystems.services.Service;
import com.webduino.zones.Zone;
import com.webduino.zones.ZoneSensor;
import com.webduino.zones.Zones;

import java.util.ArrayList;
import java.util.List;

public class OptionLoader {

    // action types
    public static final String ACTION_ACTUATOR = "actuator";
    public static final String ACTION_SERVICE = "service";
    public static final String ACTION_TRIGGER = "trigger";
    // condition types
    public static final String CONDITION_ZONESENSORVALUE = "zonesensorvalue";
    public static final String CONDITION_ZONESENSORSTATUS = "zonesensorstatus";
    public static final String CONDITION_TRIGGERSTATUS = "triggerstatus";


    public static final String ACTION_ACTUATOR_DESCRIPTION = "Attuatore";
    public static final String ACTION_SERVICE_DESCRIPTION = "Servizio";
    public static final String ACTION_TRIGGER_DESCRIPTION = "Trigger";
    public static final String ACTION_ZONESENSOR = "zonesensor";
    public static final String ACTION_ZONESENSOR_DESCRIPTION = "Sensore";

    public static final String CONDITION_ZONESENSORVALUE_DESCRIPTION = "Valore sensore zona";
    public static final String CONDITION_ZONESENSORSTATUS_DESCRIPTION = "Stato sensore zona";
    public static final String CONDITION_TRIGGERSTATUS_DESCRIPTION = "Stato trigger";

    public static final String COMPARE_OPERATOR_EQUALS = "equals";
    public static final String COMPARE_OPERATOR_GREATER = "greater";
    public static final String COMPARE_OPERATOR_LOWER = "lower";

    public static final String COMPARE_OPERATOR_EQUALS_DESCRIPTION = "=";
    public static final String COMPARE_OPERATOR_GREATER_DESCRIPTION = ">";
    public static final String COMPARE_OPERATOR_LOWER_DESCRIPTION = "<";

    public void loadActionType(OptionCardInfo optionCard, String type) {
        CharSequence[] cs = {ACTION_ACTUATOR_DESCRIPTION, ACTION_SERVICE_DESCRIPTION, ACTION_TRIGGER_DESCRIPTION};
        String[] csvalue = {ACTION_ACTUATOR, ACTION_SERVICE, ACTION_TRIGGER};
        int value = 0;
        for (int i = 0; i < csvalue.length; i++) {
            if (csvalue[i].equals(type)) {
                value = i;
                break;
            }
        }
        optionCard.value = new ListOptionCardValue("Tipo azione", value, cs, csvalue);
    }

    public void loadConditionType(OptionCardInfo optionCard, String type) {
        CharSequence[] cs = {CONDITION_ZONESENSORSTATUS_DESCRIPTION, CONDITION_ZONESENSORVALUE_DESCRIPTION, CONDITION_TRIGGERSTATUS_DESCRIPTION};
        String[] csvalue = {CONDITION_ZONESENSORSTATUS, CONDITION_ZONESENSORVALUE, CONDITION_TRIGGERSTATUS};
        int value = 0;
        for (int i = 0; i < csvalue.length; i++) {
            if (csvalue[i].equals(type)) {
                value = i;
                break;
            }
        }
        optionCard.value = new ListOptionCardValue("Tipo di condizione", value, cs, csvalue);
    }

    public void loadSensorStatus(OptionCardInfo optionCard, int sensorid, String sensortatus) {
        CharSequence[] items;
        String[] itemStringValues;
        Sensor sensor = Sensors.getFromId(sensorid);
        if (sensor != null && sensor.statuslist.size() > 0) {
            items = new CharSequence[sensor.statuslist.size()];
            itemStringValues = new String[sensor.statuslist.size()];
            int i = 0;
            int value = 0;
            for (String status: sensor.statuslist) {
                items[i] = status;
                itemStringValues[i] = status;
                if (itemStringValues[i].equals(sensortatus))
                    value = i;
                i++;
            }
            optionCard.value = new ListOptionCardValue("Stato sensore", value, items, itemStringValues);
        } else {
            optionCard.value = null;
        }
    }

    public void loadTriggerStatus(OptionCardInfo optionCard, int triggerid, String triggerstatus) {
        CharSequence[] items;
        String[] itemStringValues;
        Trigger trigger = Triggers.getFromId(triggerid);
        if (trigger != null && trigger.statuslist.size() > 0) {
            items = new CharSequence[trigger.statuslist.size()];
            itemStringValues = new String[trigger.statuslist.size()];
            int i = 0;
            int value = 0;
            for (String status: trigger.statuslist) {
                items[i] = status;
                itemStringValues[i] = status;
                if (itemStringValues[i].equals(triggerstatus))
                    value = i;
                i++;
            }
            optionCard.value = new ListOptionCardValue("Stato trigger", value, items, itemStringValues);
        } else {
            optionCard.value = null;
        }
    }

    public void loadCompareOperator(OptionCardInfo optionCard, String compareoperator) {
        CharSequence[] cs = {COMPARE_OPERATOR_EQUALS, COMPARE_OPERATOR_GREATER, COMPARE_OPERATOR_LOWER};
        String[] csvalue = {COMPARE_OPERATOR_EQUALS_DESCRIPTION, COMPARE_OPERATOR_GREATER_DESCRIPTION, COMPARE_OPERATOR_LOWER_DESCRIPTION};
        int value = 0;
        for (int i = 0; i < csvalue.length; i++) {
            if (csvalue[i].equals(compareoperator)) {
                value = i;
                break;
            }
        }
        optionCard.value = new ListOptionCardValue("Tipo di condizione", value, cs, csvalue);
    }

    public void loadTriggerCommand(String text, OptionCardInfo optionCard, int triggerid, String command) {
        CharSequence[] cs = {"Attiva", "Disattiva"};
        String[] csvalue = {"enabled", "disabled"};
        int current = 0;
        int value = 0;
        for (int i = 0; i < csvalue.length; i++) {
            if (csvalue[i].equals(command)) {
                value = i;
                break;
            }
            i++;
        }
        optionCard.value = new ListOptionCardValue(text, value, cs, csvalue);
    }

    public void loadWebduinoSystemServiceId(OptionCardInfo optionCard, int serviceid, WebduinoSystem webduinoSystem) {

        if (webduinoSystem.services.size() > 0) {
            CharSequence[] items;
            int[] itemValues;
            int i;
            int value = 0;
            items = new CharSequence[webduinoSystem.services.size()];
            itemValues = new int[webduinoSystem.services.size()];
            i = 0;

            for (WebduinoSystemService webduinoSystemService : webduinoSystem.services) {
                Service service = Services.getFromId(webduinoSystemService.serviceid);
                if (service == null) continue;
                items[i] = service.name;
                itemValues[i] = service.id;
                if (serviceid == service.id)
                    value = i;
                i++;
            }
            if (i == 0)
                optionCard.value = null;
            optionCard.value = new ListOptionCardValue("Servizio", value, items, itemValues);
        } else {
            optionCard.value = null;
        }
    }

    public void loadActuatorId(OptionCardInfo optionCard, int actuatorid, WebduinoSystem webduinoSystem) {

        if (webduinoSystem.actuators.size() > 0) {
            CharSequence[] items = new CharSequence[webduinoSystem.actuators.size()];
            int[] itemValues = new int[webduinoSystem.actuators.size()];
            String[] itemStringValues = new String[webduinoSystem.actuators.size()];
            int i = 0;
            int value = 0;
            for (WebduinoSystemActuator webduinoSystemActuator : webduinoSystem.actuators) {
                Sensor actuator = Sensors.getFromId(webduinoSystemActuator.actuatorid);
                if (actuator == null)
                    continue;
                items[i] = actuator.name;
                itemValues[i] = actuator.id;
                if (actuatorid == actuator.id)
                    value = i;
                i++;
            }
            if (i == 0)
                optionCard.value = null;
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
            if (i == 0)
                optionCard.value = null;
            else
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

    public void loadZoneSensorId(OptionCardInfo optionCard, int zoneid, int zonesensorid, String type) {

        Zone zone = Zones.getFromId(zoneid);
        if (zone != null && zone.zoneSensors.size() > 0) {

            List<ZoneSensor> list = new ArrayList<>();
            for (ZoneSensor zoneSensor : zone.zoneSensors) {
                Sensor sensor = Sensors.getFromId(zoneSensor.sensorId);
                if (sensor != null) {
                    if (type == null || sensor.getType().equals(type) )
                    list.add(zoneSensor);
                }
            }

            CharSequence[] items = new CharSequence[list.size()];
            int[] itemValues = new int[list.size()];
            int i = 0;
            int value = 0;
            for (ZoneSensor zoneSensor : list) {
                items[i] = zoneSensor.name;
                itemValues[i] = zoneSensor.id;
                if (zonesensorid == zone.id)
                    value = i;
                i++;
            }
            if (i == 0) // non c'è nessun sensore di tipo adeguato
                optionCard.value = null;
            else
                optionCard.value = new ListOptionCardValue("Sensore zona", value, items, itemValues);
        } else {
            optionCard.value = null;
        }
    }

    public boolean loadActuatorCommand(OptionCardInfo optionCard, int actuatorid, String actuatorcommand) {
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
                items[i] = actionCommand.name;
                itemStringValues[i] = actionCommand.command;
                if (actionCommand.command.equals(actuatorcommand))
                    value = i;
                i++;
            }
            optionCard.value = new ListOptionCardValue("Comando attuatore", value, items, itemStringValues);
            return true;
        } else {
            optionCard.value = null;
            return false;
        }
    }

    public boolean loadServiceCommand(OptionCardInfo optionCard, int serviceid, String servicecommand) {

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
            return true;
        } else {
            optionCard.value = null;
            return false;
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
