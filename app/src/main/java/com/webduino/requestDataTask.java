package com.webduino;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giacomo on 01/07/2015.
 */
public class requestDataTask extends
        AsyncTask<Object, Long, requestDataTask.Result> {

    public static final int REQUEST_REGISTERDEVICE = 1;
    public static final int REQUEST_SENSOR = 3;
    public static final int REQUEST_ACTUATOR = 4;
    public static final int REQUEST_SHIELD = 5;

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
    }

    public requestDataTask(/*Activity activity, */AsyncRequestDataResponse asyncResponse, int type) {
        //this.activity = activity;
        //dialog = new ProgressDialog(activity);
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
        requestType = type;
    }

    protected requestDataTask.Result doInBackground(Object... params) {

        requestDataTask.Result result = new requestDataTask.Result();
        URL url;


        try {
            String path = "/register?";
            if (requestType == REQUEST_REGISTERDEVICE) {
                String tokenId = (String) params[0];
                String model = (String) params[1];//Build.MODEL;

                    path += "tokenid="+tokenId;
                    path += "&name="+ model;

            } /*else if (requestType == REQUEST_LOGMETEODATA) {

                } else if (requestType == REQUEST_SPOTLIST) {

                } else if (requestType == REQUEST_FORECASTLOCATIONS) {

                } else if (requestType == REQUEST_FAVORITESLASTMETEODATA) {
                }*/

            String serverUrl = "http://192.168.1.3:8080/webduino";//AAlarmPreferences.getServerUrl(activity);
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
                        /*List<Spot> list = new ArrayList<Spot>();
                        JSONObject jObject = new JSONObject(json);
                        JSONArray jArray = jObject.getJSONArray("spotlist");
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jObject2 = jArray.getJSONObject(i);
                            Spot spt = new Spot(jObject2);
                            list.add(spt);
                        }
                        String favorites = jObject.getString("favorites");
                        result.spotList = list;
                        result.favorites = new ArrayList<Long>();
                        String[] split = favorites.split(",");
                        if (split != null) {
                            for (int i = 0; i < split.length; i++) {
                                if (!split[i].equals("")) {
                                    long id = Integer.valueOf(split[i]);
                                    if (id != 0)
                                        result.favorites.add(id);
                                }
                            }
                        }*/
                    // TODO add favorites list
                } else if (requestType == REQUEST_SENSOR) {
                    List<Sensor> list = new ArrayList<Sensor>();
                    JSONObject jObject = new JSONObject(json);
                    JSONArray jArray = jObject.getJSONArray("meteodata");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject2 = jArray.getJSONObject(i);
                        Sensor md = new Sensor();//:(jObject2);
                        list.add(md);
                    }
                    result.sensors = list;
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

    protected void onPreExecute() {

        //progressBar.setProgress(10);
        String message = "attendere prego...";
        if (requestType == REQUEST_REGISTERDEVICE)
            message = "Richiesta lista spot...";
        else if (requestType == REQUEST_SENSOR)
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

        if (requestType == REQUEST_REGISTERDEVICE) {
            delegate.processFinishRegister(result.shieldId, error, errorMessage);
        } /*else if (requestType == REQUEST_LOGMETEODATA) {
            List<MeteoStationData> data = new ArrayList<>();
            if (result.meteoList != null) {
                for (Object obj : result.meteoList) {

                    data.add(new MeteoStationData((MeteoStationData) obj));
                }
            }
            delegate.processFinishHistory(result.spotId,data, error, errorMessage);
        } else if (requestType == REQUEST_LASTMETEODATA || requestType == REQUEST_FAVORITESLASTMETEODATA) {
            delegate.processFinish(result.meteoList, error, errorMessage);
        } else if (requestType == REQUEST_ADDFAVORITES) {
            delegate.processFinishAddFavorite(result.spotId, error, errorMessage);
        } else if (requestType == REQUEST_REMOVEFAVORITE) {
            delegate.processFinishRemoveFavorite(result.spotId, error, errorMessage);
        } else if (requestType == REQUEST_FORECAST) {
            delegate.processFinishForecast(requestId, result.forecast, error, errorMessage);
        } else if (requestType == REQUEST_FORECASTLOCATIONS) {
            delegate.processFinishForecastLocation(result.locations, error, errorMessage);
        }*/


        //progressBar.setVisibility(View.GONE);
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
}
