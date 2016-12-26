package com.webduino;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by giacomo on 01/07/2015.
 */
public class requestDataTask extends
        AsyncTask<Object, Long, requestDataTask.Result> {

    public static final int REQUEST_REGISTERDEVICE = 1;
    public static final int REQUEST_SENSORS = 3;
    public static final int REQUEST_ACTUATORS = 4;
    public static final int REQUEST_SHIELD = 5;
    public static final int POST_ACTUATOR_COMMAND = 6;

    public AsyncRequestDataResponse delegate = null;//Call back interface
    //private ProgressDialog dialog;
    //private Activity activity;
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
        public Actuator actuator;
    }

    public requestDataTask(/*Activity activity, */AsyncRequestDataResponse asyncResponse, int type) {
        //this.activity = activity;
        //dialog = new ProgressDialog(activity);
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
        requestType = type;
    }

    protected requestDataTask.Result doInBackground(Object... params) {

        requestDataTask.Result result = null;

        if (requestType == REQUEST_REGISTERDEVICE || requestType == REQUEST_SENSORS || requestType == REQUEST_ACTUATORS) {
            result = performGetRequest(params);
        } else {
            result = performPostCall(params);
        }

        return result;

    }

    @NonNull
    private Result performGetRequest(Object[] params) {
        Result result;
        result = new Result();
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

            } else if (requestType == REQUEST_ACTUATORS) {

                path = "/actuator?";

            }

            String serverUrl = getServerUrl();

            //String serverUrl = "http://192.168.1.3:8080/webduino";
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

                if (requestType == REQUEST_REGISTERDEVICE) {
                    // TODO add favorites list
                } else if (requestType == REQUEST_SENSORS) {

                    List<Sensor> list = new ArrayList<Sensor>();
                    JSONArray jArray = new JSONArray(json);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        if (jObject.has("type") && jObject.getString("type").equals("temperature")) {
                            TemperatureSensor sensor = new TemperatureSensor();
                            sensor.fromJson(jObject);
                            list.add(sensor);
                        }
                    }
                    result.sensors = list;
                } else if (requestType == REQUEST_ACTUATORS) {

                    List<Actuator> list = new ArrayList<Actuator>();
                    JSONArray jArray = new JSONArray(json);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        if (jObject.has("type") && jObject.getString("type").equals("heater")) {
                            HeaterActuator heater = new HeaterActuator();
                            heater.fromJson(jObject);
                            list.add(heater);
                        }
                    }
                    result.actuators = list;
                }

                if (conn != null)
                    conn.disconnect();
            } catch (JSONException e) {
                error = true;
                e.printStackTrace();
                errorMessage = e.toString();
            }
        } catch (MalformedURLException e) {
            error = true;
            e.printStackTrace();
            errorMessage = e.toString();
        } catch (java.net.SocketTimeoutException e) {
            error = true;
            e.printStackTrace();
            errorMessage = e.toString();
        } catch (IOException e) {
            error = true;
            e.printStackTrace();
            errorMessage = e.toString();
        }
        return result;
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
        String message = "attendere prego...";
        if (requestType == REQUEST_REGISTERDEVICE)
            message = "Richiesta lista spot...";
        else if (requestType == REQUEST_SENSORS)
            message = "Richiesta lista completa spot...";
        //else if (requestType == REQUEST_HISTORYMETEODATA)
        //message = "Richiesta dati storici...";
        /*else if (requestType == REQUEST_LASTMETEODATA)
            message = "Richiesta dati meteo...";
        else if (requestType == REQUEST_FORECAST) {
            message = "caricamento ...";
            //this.dialog.setMessage(message);
            //this.dialog.show();
        } else if (requestType == REQUEST_FORECASTLOCATIONS) {
            message = "caricamento...";
            this.dialog.setMessage(message);
            this.dialog.show();
        }*/

        //this.dialog.setMessage(message);
        //this.dialog.show();
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        //progressBar.setProgress(values[0].intValue());
    }

    protected void onPostExecute(Result result) {

        /*if (dialog.isShowing()) {
            dialog.dismiss();
        }*/
        if (result == null) {
            error = true;
            errorMessage = "errore";
            result = new Result();

            new AlertDialog.Builder(MainActivity.activity)
                    .setTitle("Your Alert")
                    .setMessage("Your Message")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                        }
                    }).show();
            //return;
        }

        if (requestType == REQUEST_REGISTERDEVICE) {
            delegate.processFinishRegister(result.shieldId, error, errorMessage);
        } else if (requestType == REQUEST_SENSORS) {
            delegate.processFinishSensors(result.sensors, error, errorMessage);
        } else if (requestType == REQUEST_ACTUATORS) {
            delegate.processFinishActuators(result.actuators, error, errorMessage);
        } else if (requestType == POST_ACTUATOR_COMMAND) {
            delegate.processFinishSendCommand(result.actuator, error, errorMessage);
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


    protected Result performPostCall(Object[] params) {
        boolean status = false;

        Result result;
        result = new Result();
        //URL url;

        String path = "";
        if (requestType == POST_ACTUATOR_COMMAND) {
            path = "/actuator?";
            int actuatorId = (int) params[0];
            String command = (String) params[1];
            int duration = (int) params[2];
            double target = (double) params[3];
            int sensorId = (int) params[4];
            boolean remote = (boolean) params[5];

            /*Actuator actuator = Actuators.getFromId(actuatorId);
            if (actuator == null)
                return null;
            HeaterActuator heater = (HeaterActuator) actuator;
*/
            String serverUrl = getServerUrl();
            String url = serverUrl + path;

            String response;
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", "" + actuatorId);
            map.put("command", command);
            map.put("duration", "" + 3000);
            map.put("target", "" + target);
            map.put("sensorid", "" + sensorId);
            map.put("remote", "" + remote);
            response = postCall(url, map);
            if (response != null) {
                try {
                    JSONObject json = new JSONObject(response);

                    if (json.has("answer") && json.getString("answer").equals("success")) {

                        JSONObject actuator = new JSONObject(json.getString("actuator"));
                        if (actuator != null) {
                            result.actuator = new HeaterActuator();
                            result.actuator.fromJson(actuator);
                            return result;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else {
            // other command
            return null;
        }
        return null;
    }

    public String postCall(String requestURL,
                           HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            Context context = MainActivity.activity;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000); // 7 sec
            conn.setReadTimeout(10000); // 12 sec
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json");

            /*
             * JSON
             */

            JSONObject root = new JSONObject();
            for (HashMap.Entry<String, String> entry : postDataParams.entrySet()) {
                root.put(entry.getKey(),  entry.getValue());
            }

            String str = root.toString();
            byte[] outputBytes = str.getBytes("UTF-8");
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
            } else {
                response = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return response;
    }
}
