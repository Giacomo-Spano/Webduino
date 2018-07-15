package com.webduino;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.webduino.elements.requestDataTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PlayAlarmActivity extends AppCompatActivity implements AlarmFragment.OnAlarmListener {

    private static AlarmFragment lastActiveAlarmFragment = null;
    AlarmFragment mAlarmFragment;
    int actionid;
    int webduinosystemid;
    String param;
    String date;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SendLoagcatMail();
    }

    public void SendLoagcatMail() {


        DateFormat df = new SimpleDateFormat("ddMMyyyyHHmm");
        String date = df.format(Calendar.getInstance().getTime());

        // save logcat in file
        File outputFile = new File(Environment.getExternalStorageDirectory(),
                "logcat" /*-+ date*/ + ".txt");
        try {
            Runtime.getRuntime().exec(
                    "logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_play_alarm);

        // Create a new Fragment to be placed in the activity layout
        mAlarmFragment = new AlarmFragment();
        mAlarmFragment.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().add(R.id.content_frame, mAlarmFragment).commit();

        Bundle bundle = getIntent().getExtras();
        actionid = bundle.getInt("actionid");
        webduinosystemid = bundle.getInt("webduinosystemid");
        param = bundle.getString("param");
        date = bundle.getString("date");

        Bundle b = new Bundle();
        b.putInt("actionid", actionid);
        b.putInt("webduinosystemid", webduinosystemid);
        b.putString("param", param);
        b.putString("date", date);
        mAlarmFragment.setArguments(b);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (lastActiveAlarmFragment != null)
            lastActiveAlarmFragment.stopRingtone();
        mAlarmFragment.playAlarm();
        lastActiveAlarmFragment = mAlarmFragment;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set the window to keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mAlarmFragment.stopAlarm();
        stopAlarm(/*0*/);
    }

    private void stopAlarm(/*int snoozeMinutes*/) {

        /*Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.GO_DIRECTLY_TO_SPOT_DETAILS, true); //Optional parameters
        intent.putExtra("spotId", spotId); //Optional parameters
        intent.putExtra("alarmId", alarmId);
        intent.putExtra("snoozeMinutes", snoozeMinutes);
        startActivity(intent);*/

        finish();
    }


    @Override
    public void onStopAlarmClick() {
        stopAlarm(/*0*/);

        JSONObject json = new JSONObject();
        try {
            json.put("webduinosystemid", "" + webduinosystemid);
            json.put("command", "pause");

            new requestDataTask(MainActivity.activity, new WebduinoResponse() {
                @Override
                public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                    //heater.fromJson((JSONObject) result);
                    //refreshData();
                }
            }, requestDataTask.POST_COMMAND).execute(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSnoozeAlarmClick(int snoozeMinutes) {

        snoozeAlarm(snoozeMinutes, actionid);
        stopAlarm(/*snoozeMinutes*/);
    }

    @Override
    public void onStartPlayAlarm() {

        Date date = new Date();
        //updateAlarmRingDate(date);
        updateAlarmRingDate(date, actionid);
    }

    private void updateAlarmRingDate(final Date date, final int alarmid) {

        /*postalarmtask task = (postalarmtask) new postalarmtask(this, new AsyncPostProgramResponse() {
            @Override
            public void processFinish(Object obj, boolean error, String errorMessage) {
                // TODO
            }

        }, postalarmtask.POST_UPDATEALARMRINGDATE).execute(alarmid, date);*/
    }

    private void snoozeAlarm(final int snoozeMinutes, final int alarmid) {

        /*new postalarmtask(this, new AsyncPostProgramResponse() {
            @Override
            public void processFinish(Object obj, boolean error, String errorMessage) {
                // TODO
                //finish();
            }

        }, postalarmtask.POST_SNOOZEALARM).execute(alarmid, snoozeMinutes);*/

    }

}
