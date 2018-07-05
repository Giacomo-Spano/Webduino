package com.webduino;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

public class AlarmFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    double mcurspeed, mcuravspeed;
    String mcurdate;
    int mspotid;
    String mspotName;

    private Ringtone ringtone;
    private TextView timerTextView;
    private TextView alarmTextView;

    OnAlarmListener mCallback;

    // Container Activity must implement this interface
    public interface OnAlarmListener {
        public void onStopAlarmClick();

        public void onSnoozeAlarmClick(int snoozeMinutes);

        public void onStartPlayAlarm();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnAlarmListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnAlarmListener");
        }
    }

    @SuppressWarnings("deprecation")
    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            try {
                mCallback = (OnAlarmListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnAlarmListener");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null)  {
            mcurspeed = getArguments().getDouble("curspeed");
            mcuravspeed = getArguments().getDouble("curavspeed");
            mcurdate = getArguments().getString("curdate");
            mspotid = getArguments().getInt("spotid");
            mspotName = getArguments().getString("spotName");
        }

        View v;
        v = inflater.inflate(R.layout.fragment_alarm, container, false);

        final Button stopButton = (Button) v.findViewById(R.id.stopAlarmButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopAlarm();
            }
        });

        final Button snoozeButton = (Button) v.findViewById(R.id.snoozeAlarmButton);
        snoozeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                snoozeAlarm(5);
            }
        });

        timerTextView = (TextView) v.findViewById(R.id.timerTextView);

        TextView tv = (TextView) v.findViewById(R.id.curspeedTextView);
        tv.setText("velocità: " + mcurspeed);
        tv = (TextView) v.findViewById(R.id.curavspeedTextView);
        tv.setText("velocità media: " + mcuravspeed);
        tv = (TextView) v.findViewById(R.id.curdateTextView);
        tv.setText("data: " + mcurdate.toString());
        tv = (TextView) v.findViewById(R.id.spotTextView);
        tv.setText("spot: " + mspotName);

        return v;
    }

    public void playAlarm(int spot) {

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }
        ringtone = RingtoneManager.getRingtone(this.getActivity(), alarmUri);
        ringtone.play();

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //snoozeAlarm(5);
                timerTextView.setText("done!");
                stopAlarm();
            }
        }.start();

        if (mCallback != null)
            mCallback.onStartPlayAlarm();

    }

    public void stopRingtone() {
        ringtone.stop();
        //fullscreenTextView.setText("Allarme disattivato");
    }

    public void stopAlarm() {
        stopRingtone();

        mCallback.onStopAlarmClick();
    }

    public void snoozeAlarm(int minutes) {
        stopRingtone();

        mCallback.onSnoozeAlarmClick(minutes);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    /*public void getMeteoData(final int spotID) {

        final AlarmFragment fragment = this;

        new requestMeteoDataTask(this.getActivity(), new AsyncRequestMeteoDataResponse() {

            @Override
            public void processFinishFavoritesLastMeteoData(List<Object> list, boolean error, String errorMessage) {

                if (error) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                    alertDialogBuilder.setTitle("Errore");
                    alertDialogBuilder
                            .setMessage(errorMessage)
                            .setCancelable(false);
                    alertDialogBuilder
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog;
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {

                    MeteoStationData md = null;
                    for (int i = 0; i < list.size(); i++) {
                        md = (MeteoStationData) list.get(i);
                        if (md.spotID == spotID)
                            break;
                    }
                    mCarditem.spotID = spotID;
                    mCarditem.mWecamUrl = md.webcamurl;
                    mCarditem.update(md);
                }
            }

            @Override
            public void processFinishHistory(List<Object> list, boolean error, String errorMessage) {

            }

            @Override
            public void processFinishSpotList(List<Object> list, boolean error, String errorMessage) {

            }
        }).execute(requestMeteoDataTask.REQUEST_LASTMETEODATA, "" + spotID);
    }

    @Override
    public void meteocardselected(long index) {


        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(MainActivity.GO_DIRECTLY_TO_SPOT_DETAILS, mCarditem.spotID); //Optional parameters
        intent.putExtra("spotID", mCarditem.spotID); //Optional parameters
        startActivity(intent);

    }*/
}