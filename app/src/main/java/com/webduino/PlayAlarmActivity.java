package com.webduino;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PlayAlarmActivity extends AppCompatActivity implements AlarmFragment.OnAlarmListener {

    private static AlarmFragment lastActiveAlarmFragment = null;
    AlarmFragment mAlarmFragment;
    int spotId;
    String spotName;
    int alarmId;
    double curspeed;
    double curavspeed;
    String curDate;

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

        //send file using email
        /*Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // Set type to "email"
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"giaggi70@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, outputFile.getAbsolutePath());
        // the mail subject



        String str = outputFile.getAbsolutePath();

        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject " + str);
        startActivity(Intent.createChooser(emailIntent , "Send email..."));*/
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
        spotId = 1;//Integer.valueOf(bundle.getString("spotid"));
        spotName = "spotName";//bundle.getString("spotName");
        alarmId = 1;//Integer.valueOf(bundle.getString("alarmid"));
        curspeed = 1.0;//Double.valueOf(bundle.getString("curspeed"));
        curavspeed = 1.0;//Double.valueOf(bundle.getString("curavspeed"));
        curDate = "curDate";//bundle.getString("curdate");

        Bundle b = new Bundle();
        b.putInt("spotid", spotId);
        b.putString("spotName", spotName);
        b.putInt("alarmid", alarmId);
        b.putDouble("curavspeed", curavspeed);
        b.putDouble("curspeed", curspeed);
        b.putString("curdate", curDate);
        mAlarmFragment.setArguments(b);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (lastActiveAlarmFragment != null)
            lastActiveAlarmFragment.stopRingtone();
        mAlarmFragment.playAlarm(spotId);
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
    }

    @Override
    public void onSnoozeAlarmClick(int snoozeMinutes) {

        snoozeAlarm(snoozeMinutes, alarmId);
        stopAlarm(/*snoozeMinutes*/);
    }

    @Override
    public void onStartPlayAlarm() {

        Date date = new Date();
        //updateAlarmRingDate(date);
        updateAlarmRingDate(date, alarmId);
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
