package com.webduino.elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;

import com.webduino.AsyncRequestDataResponse;
import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.scenarios.Scenario;
import com.webduino.SensorFactory;
import com.webduino.ZoneFactory;
import com.webduino.chart.HistoryData;
import com.webduino.zones.Zone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by giacomo on 01/07/2015.
 */
public class requestDataTask extends
        AsyncTask<Object, Long, requestDataTask.Result> {

    public static final int REQUEST_REGISTERDEVICE = 1;
    public static final int REQUEST_SENSORS = 3;
    //public static final int REQUEST_ACTUATORS = 4;
    public static final int REQUEST_SHIELD = 5;
    public static final int REQUEST_PROGRAMS = 6;
    public static final int REQUEST_NEXTPROGRAMS = 7;
    public static final int POST_ACTUATOR_COMMAND = 8;
    public static final int POST_PROGRAM = 9;
    public static final int POST_DELETEPROGRAM = 10;
    public static final int REQUEST_SENSORDATALOG = 11;
    public static final int REQUEST_ZONES = 12;
    public static final int REQUEST_SCENARIOS = 13;
    public static final int REQUEST_ACTUATORPROGRAMTIMERANGEACTITONS = 14;
    public static final int REQUEST_COMMANDDATALOG = 15;
    private final Activity activity;

    public AsyncRequestDataResponse delegate = null;//Call back interface
    ProgressDialog ringProgressDialog;
    private boolean error = false;
    private String errorMessage = "";
    int requestType;
    int requestId;
    int contentSize;


    protected class Result {
        public long shieldId;
        public List<Sensor> sensors;
        public List<Actuator> actuators;
        public List<Shield> shields;
        public List<Object> programs;
        public List<Object> objectList;
        public List<Zone> zones;
        public List<Scenario> scenarios;
        public Actuator actuator;
        public String resultString;
        public boolean response;
        public Object resultObject;
    }

    public requestDataTask(Activity activity, AsyncRequestDataResponse asyncResponse, int type) {
        this.activity = activity;
        //dialog = new ProgressDialog(activity);
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
        requestType = type;

        if (requestType != REQUEST_REGISTERDEVICE)
            ringProgressDialog = new ProgressDialog(/*MainActivity.activity*/activity);
    }

    protected requestDataTask.Result doInBackground(Object... params) {

        requestDataTask.Result result = null;

        if (requestType == REQUEST_REGISTERDEVICE || requestType == REQUEST_SENSORS
                || requestType == REQUEST_PROGRAMS || requestType == REQUEST_NEXTPROGRAMS
                || requestType == REQUEST_ZONES || requestType == REQUEST_SCENARIOS
                || requestType == REQUEST_SENSORDATALOG
                || requestType == REQUEST_ACTUATORPROGRAMTIMERANGEACTITONS || requestType == REQUEST_COMMANDDATALOG) {
            result = performGetRequest(params);
            return result;
        } else if (requestType == POST_ACTUATOR_COMMAND || requestType == POST_PROGRAM || requestType == POST_DELETEPROGRAM) {
            try {
                result = performPostCall(params);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                result = new Result();
                result.response = false;
                result.resultString = e.toString();
                return null;
            }
        }

        return result;
    }

    @NonNull
    private Result performGetRequest(Object[] params) {

        URL url;

        try {
            String path = "";
            if (requestType == REQUEST_REGISTERDEVICE) {
                path = "/register?";
                String tokenId = (String) params[0];
                String model = (String) params[1];//Build.MODEL;

                path += "tokenid=" + tokenId;
                path += "&name=" + model;

            } else if (requestType == REQUEST_SENSORS) {

                path = "/sensor?";

            } else if (requestType == REQUEST_ZONES) {

                path = "/system?requestcommand=zones";

            } else if (requestType == REQUEST_SCENARIOS) {

                path = "/system?requestcommand=scenarios";

            } else if (requestType == REQUEST_PROGRAMS) {

                path = "/program?";

            } else if (requestType == REQUEST_NEXTPROGRAMS) {

                path = "/program?next=true";

            } else if (requestType == REQUEST_SENSORDATALOG) {
                path = "/datalog";
                int actuatorId = (int) params[0];
                String type = (String) params[1];

                Date date = new Date();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                path += "?enddate=" + df.format(date);

                path += "&elapsed=360";
                path += "&id=" + actuatorId;
                path += "&type=" + type;
            } else if (requestType == REQUEST_ACTUATORPROGRAMTIMERANGEACTITONS) {
                path = "/system?requestcommand=nextprograms";
                int actuatorId = (int) params[0];
                path += "&id=" + actuatorId;
            } else if (requestType == REQUEST_COMMANDDATALOG) {
                path = "/system?requestcommand=commandlog";
                int actuatorId = (int) params[0];
                path += "&id=" + actuatorId;

            }

            String serverUrl = getServerUrl();

            url = new URL(serverUrl + path);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000); //set timeout to 5 seconds
            conn.setAllowUserInteraction(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            try {
                BufferedInputStream in = new BufferedInputStream(
                        conn.getInputStream());
                // Convert the stream to a String
                // There are various approaches, I'll leave it up to you
                contentSize = 1000;//Integer.valueOf(conn.getHeaderField("Length"));//conn.getContentLength();
                String json = convertStreamToString(in);

                Result result = new Result();
                if (requestType == REQUEST_REGISTERDEVICE) {
                    // TODO add favorites list
                } else if (requestType == REQUEST_SENSORS) {

                    List<Sensor> list = new ArrayList<Sensor>();
                    JSONArray jArray = new JSONArray(json);
                    SensorFactory factory = new SensorFactory();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Sensor sensor = factory.createSensor(jObject);
                        if (sensor != null)
                            list.add(sensor);
                    }
                    result.sensors = list;
                } else if (requestType == REQUEST_ZONES) {

                    List<Zone> list = new ArrayList<Zone>();
                    JSONArray jArray = new JSONArray(json);
                    ZoneFactory factory = new ZoneFactory();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Zone zone = factory.createZone(jObject);
                        if (zone != null)
                            list.add(zone);
                    }
                    result.zones = list;
                } else if (requestType == REQUEST_PROGRAMS) {

                    List<Object> list = new ArrayList<Object>();
                    JSONArray jArray = new JSONArray(json);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Object program = new Program();
                        ((Program) program).fromJson(jObject);
                        list.add(program);
                    }
                    result.programs = list;

                } else if (requestType == REQUEST_SCENARIOS) {

                    List<Object> list = new ArrayList<Object>();
                    JSONArray jArray = new JSONArray(json);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Object data = new Scenario();
                        ((Scenario) data).fromJson(jObject);
                        list.add(data);
                    }
                    result.objectList = list;
                } else if (requestType == REQUEST_ACTUATORPROGRAMTIMERANGEACTITONS) {

                    List<Object> list = new ArrayList<Object>();
                    JSONArray jArray = new JSONArray(json);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Object nextaction = new NextProgramTimeRangeAction();
                        try {
                            ((NextProgramTimeRangeAction) nextaction).fromJson(jObject);
                            list.add(nextaction);
                        } catch (Exception e) {

                        }
                    }
                    result.resultObject = list;
                } else if (requestType == REQUEST_SENSORDATALOG) {

                    List<Object> list = new ArrayList<Object>();
                    JSONArray jArray = new JSONArray(json);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Object data = new HistoryData();
                        ((HistoryData) data).fromJson(jObject);
                        list.add(data);
                    }
                    result.resultObject = list;
                } else if (requestType == REQUEST_COMMANDDATALOG) {

                    List<Object> list = new ArrayList<Object>();
                    JSONArray jArray = new JSONArray(json);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        Object data = new CommandDataLog();
                        try {
                            ((CommandDataLog) data).fromJson(jObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        list.add(data);
                    }
                    result.resultObject = list;
                }


                if (conn != null)
                    conn.disconnect();

                return result;

            } catch (JSONException e) {
                error = true;
                e.printStackTrace();
                errorMessage = e.toString();
            }
        } catch (MalformedURLException e) {
            error = true;
            e.printStackTrace();
            errorMessage = e.toString();
            return null;
        } catch (java.net.SocketTimeoutException e) {
            error = true;
            e.printStackTrace();
            errorMessage = e.toString();
            return null;
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
            errorMessage = e.toString();
            return null;
        }
        return null;
    }

    @NonNull
    private String getServerUrl() {
        Context context = MainActivity.activity;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String serverUrl = "";
        if (prefs != null) {

            String key = context.getString(R.string.server_url_preference_key);
            serverUrl = prefs.getString(key, context.getString(R.string.server_url_default));
        }
        return serverUrl;
    }


    protected void onPreExecute() {

        //progressBar.setProgress(10);
        String title = "Attendere prego";
        String message = "";
        if (requestType == REQUEST_REGISTERDEVICE)
            //message = "Richiesta registrazione inviata";
            return;
        else if (requestType == REQUEST_SENSORS)
            message = "Aggiornamnento";
        else if (requestType == REQUEST_ZONES)
            message = "Aggiornamnento";
        else if (requestType == REQUEST_SCENARIOS)
            message = "Aggiornamnento";
        else if (requestType == REQUEST_PROGRAMS || requestType == REQUEST_NEXTPROGRAMS)
            message = "Aggiornamnento";
        else if (requestType == REQUEST_SHIELD)
            message = "Aggiornamnento";
        else if (requestType == POST_PROGRAM)
            message = "Salvataggio programma";
        else if (requestType == POST_ACTUATOR_COMMAND || requestType == POST_DELETEPROGRAM)
            message = "Comando inviato";
        else if (requestType == REQUEST_SENSORDATALOG)
            message = "Aggiornamnento";

        ringProgressDialog.setMessage(message);
        ringProgressDialog.setTitle(title);
        ringProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        //progressBar.setProgress(values[0].intValue());
    }

    protected void onPostExecute(Result result) {

        if (ringProgressDialog != null && ringProgressDialog.isShowing())
            ringProgressDialog.dismiss();

        if (result == null || error) {
            error = true;
            //errorMessage = "errore";
            result = new Result();

            final Result finalResult = result;
            new AlertDialog.Builder(activity)
                    .setTitle("Errore")
                    .setMessage(errorMessage)
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                            processFinish(finalResult);
                        }
                    }).show();

        } else {

            processFinish(result);
        }
    }

    private void processFinish(Result result) {


        if (ringProgressDialog != null && ringProgressDialog.isShowing() && requestType != REQUEST_REGISTERDEVICE)
            ringProgressDialog.dismiss();


        if (requestType == REQUEST_REGISTERDEVICE) {
            delegate.processFinishRegister(result.shieldId, error, errorMessage);
        } else if (requestType == REQUEST_SENSORS) {
            delegate.processFinishSensors(result.sensors, error, errorMessage);
        } else if (requestType == REQUEST_ZONES) {
            delegate.processFinishZones(result.zones, error, errorMessage);
        } else if (requestType == REQUEST_PROGRAMS || requestType == REQUEST_NEXTPROGRAMS) {
            delegate.processFinishPrograms(result.programs, requestType, error, errorMessage);
        } else if (requestType == POST_ACTUATOR_COMMAND) {
            delegate.processFinishSendCommand(result.resultString, error, errorMessage);
        } else if (requestType == POST_PROGRAM) {
            delegate.processFinishPostProgram(result.response, POST_PROGRAM, error, errorMessage);
        } else if (requestType == POST_DELETEPROGRAM) {
            delegate.processFinishPostProgram(result.response, POST_DELETEPROGRAM, error, errorMessage);
        } else if (requestType == REQUEST_SENSORDATALOG || requestType == REQUEST_SCENARIOS) {
            delegate.processFinishObjectList(result.objectList, requestType, error, errorMessage);
        } else {
            delegate.processFinish(result.resultObject, requestType, error, errorMessage);
        }

    }

    private String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

                long l = 100 * sb.length() / contentSize;

                publishProgress(l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    protected Result performPostCall(Object[] params) throws Exception {

        String path = "";
        if (requestType == POST_ACTUATOR_COMMAND) {
            path = "/shield?";
            String serverUrl = getServerUrl();
            String url = serverUrl + path;

            int shieldId = (int) params[0];
            int actuatorId = (int) params[1];
            String command = (String) params[2];
            int duration = (int) params[3] * 60;
            Date endtime = (Date) params[4];
            ;
            boolean nexttimerange = (boolean) params[5];
            ;
            double target = (double) params[6];
            int zoneId = (int) params[7];
            //boolean remote = (boolean) params[5];
            JSONObject json = new JSONObject();
            try {
                json.put("shieldid", "" + shieldId);
                json.put("actuatorid", "" + actuatorId);
                json.put("command", command);

                if (nexttimerange) {
                    json.put("nexttimerange", nexttimerange);
                } else if (endtime != null && !endtime.equals("")) {
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    json.put("endtime", df.format(endtime));
                } else {
                    json.put("duration", "" + duration);
                }


                json.put("target", "" + target);
                json.put("zone", "" + zoneId);
                //json.put("remote", "" + remote);
                Result result = new Result();
                result.resultString = postCall(url, json.toString());
                return result;

            } catch (JSONException e) {
                e.printStackTrace();
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

        }

        throw new Exception("invalid command");
    }

    public Result getResult(String response) {
        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                if (json.has("answer")) {
                    Result res = new Result();
                    String answer = json.getString("answer");
                    if (answer.equals("success")) {
                        res.response = true;
                    } else if (answer.equals("deleted") && requestType == POST_DELETEPROGRAM) {
                        res.response = true;
                    }
                    return res;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public String postCall(String requestURL, String json) throws Exception {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            Context context = MainActivity.activity;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000); // 7 sec
            conn.setReadTimeout(15000); // 12 sec
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            byte[] outputBytes = json.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(outputBytes);

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                return response;
            } else {

                errorMessage = conn.getResponseMessage() + " responseCode: " + responseCode;
                throw new Exception(errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
