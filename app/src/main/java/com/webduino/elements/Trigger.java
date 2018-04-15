package com.webduino.elements;

import com.webduino.ActionCommand;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giaco on 17/02/2018.
 */

public class Trigger {

    public int id = 0;
    public String name = "";
    public boolean status = false;
    public java.util.Date date;

    public List<ActionCommand> actioncommandlist = new ArrayList<>();
    public List<String> statuslist = new ArrayList<>();

    public void fromJson(JSONObject json) throws Exception {

        if (json.has("id"))
            id = json.getInt("id");
        if (json.has("name"))
            name = json.getString("name");
        if (json.has("status"))
            status = json.getBoolean("status");

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
