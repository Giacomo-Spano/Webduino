package com.webduino.elements;

import android.app.Fragment;
import android.content.Context;

import com.webduino.ActionCommand;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.SensorCardInfo;
import com.webduino.fragment.cardinfo.TemperatureSensorCardInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */
public class Sensor {

    public int id;
    protected int shieldid;
    protected boolean online;
    protected String subaddress;
    public String name;
    protected Date lastUpdate;
    protected String type;

    public List<Sensor> childsensors = new ArrayList<>();

    public List<String> statuslist = new ArrayList<>();
    public List<ActionCommand> actioncommandlist = new ArrayList<>();


    public Sensor(JSONObject json) {
        fromJson(json);
    }

    public CardInfo getCardInfo(Fragment context) {
        return getCardInfo(context, null);
    }

    public CardInfo getCardInfo(Fragment context, CardInfo cardInfo) {

        if (cardInfo == null)
            cardInfo = new SensorCardInfo();
        cardInfo.id = getId();
        cardInfo.shieldid = getShieldId();
        cardInfo.name = getName();
        cardInfo.online = getOnLine();
        cardInfo.setEnabled(true);

        return cardInfo;
    }

    public int getId() {
        return id;
    }

    public int getShieldId() {
        return shieldid;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean getOnLine() {
        return online;
    }

    void fromJson(JSONObject json) {

        try {

            if (json.has("id"))
                id = json.getInt("id");
            if (json.has("shieldid"))
                shieldid = json.getInt("shieldid");
            if (json.has("online"))
                online = json.getBoolean("online");
            if (json.has("subaddress"))
                subaddress = json.getString("subaddress");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("type"))
                type = json.getString("type");
            if (json.has("lastupdate")) {
                String strDate = json.getString("lastupdate");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (strDate != null)
                    lastUpdate = df.parse(strDate);
            }

            if (json.has("statuslist")) {
                JSONArray jsonArray = json.getJSONArray("statuslist");
                if (jsonArray !=  null) {
                    for (int i = 0; i < jsonArray.length();i++) {
                        statuslist.add((String)jsonArray.get(i));
                    }
                }
            }

            if (json.has("actioncommandlist")) {
                JSONArray jsonArray = json.getJSONArray("actioncommandlist");
                if (jsonArray !=  null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ActionCommand actioncommand = new ActionCommand(jsonObject);
                        actioncommandlist.add(actioncommand);
                    }
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ActionCommand getActionCommand(String command) {
        if (actioncommandlist == null || actioncommandlist.size() == 0)
            return null;
        for(ActionCommand actioncommand:actioncommandlist) {
            if (actioncommand.command.equals(command))
                return actioncommand;
        }
        return actioncommandlist.get(0);
    }
}
