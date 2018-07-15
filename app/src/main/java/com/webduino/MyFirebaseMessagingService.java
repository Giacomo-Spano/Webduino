package com.webduino;

/**
 * Created by Giacomo Spanò on 20/11/2016.
 */

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webduino.MainActivity;
import com.webduino.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Notification Message Body x: " + remoteMessage.getNotification().getBody());

        JSONObject data = new JSONObject(remoteMessage.getData());
        //String type = remoteMessage.getData().get("type");
        String type = null;
        try {
            type = data.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (type != null && type.equals("alarm")) {

            Log.i(TAG, "Alarm received");
            playAlarm(getApplicationContext(), data);
            return;
        } else {


            String strId = remoteMessage.getData().get("id");
            int id;
            if (strId != null) {
                id = Integer.valueOf(strId);
            } else {
                id = 0;
            }
            //Calling method to generate notification
            //sendNotification(remoteMessage.getNotification().getBody());
            sendMultilineNotification(remoteMessage.getNotification().getBody(), type, id);
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Push Notification")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

    private void sendMultilineNotification(String messageBody, String type, int id) {

        int notificationId = 0;

        // crea l'intent da passare alla notifica
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // crea un id notifica diverso a seconda del tipo di notifica
        if (type != null) {
            if (type.equals(MainActivity.notification_statuschange)) {

                notificationId = MainActivity.notificationId_ChangeStatus;

            } else if (type.equals(MainActivity.notification_relestatuschange)) {

                notificationId = MainActivity.notificationId_ReleStatus;

            } else {

                notificationId = 0;//MainActivity.notificationId_Error;
            }
        } else {
            notificationId = -1;//MainActivity.notificationId_ChangeStatus;;
        }
        intent.putExtra("notificationId", notificationId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);
        /*messageBody += "\nprova";
        messageBody += "\nprova";
        messageBody += "\nprova";
        messageBody += "\nprova";*/

        // crea la notifica
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(type/*"Firebase Push Notification"*/)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        //.addAction(action);

        // aggiungi eventual action button
        if (type != null)
            if (type.equals(MainActivity.notification_statuschange)) {

                //Aggiungi action "manual off nella notifica
                Intent actionIntent = new Intent(this, MainActivity.class);
                actionIntent.putExtra("action", "manual_off");
                //actionIntent.putExtra("notificationId",MainActivity.notification_statuschange);
                actionIntent.putExtra("notificationId", notificationId);
                actionIntent.putExtra("actuatorId", id);
                actionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 99, actionIntent, PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Action action = new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_call, "Manual Off", actionPendingIntent).build();
                notificationBuilder.addAction(action);

                // richiedi aggiornamento stato attuatore nell'activity
                requestActuatorStatusUpdate(id);
            }

        //invia la notifica
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void requestActuatorStatusUpdate(int actuatorId) {

        Intent intent = new Intent("com.webduino.USER_ACTION");
        Bundle bundle = new Bundle();
        bundle.putString("command", "statusupdate");
        bundle.putInt("command", actuatorId);
        intent.putExtras(bundle);
        sendBroadcast(intent);

        /*if (MainActivity.activity != null) {
            MainActivity mActivity = (MainActivity) MainActivity.activity;
            mActivity.getSensorData();
            mActivity.getActuatorData();
        }*/

       /*Intent intent = new Intent(this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_UPDATE_CURRENT);

        //Intent Intent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

        resultIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        getApplication().startActivity(resultIntent);*/


    }

    private void playAlarm(Context context, JSONObject alarmData) {
        Intent resultIntent = new Intent(context, PlayAlarmActivity.class);

        try {
            int actionid = Integer.parseInt(alarmData.getString("actionid"));
            int webduinosystemid = Integer.parseInt(alarmData.getString("webduinosystemid"));
            String param = alarmData.getString("param");
            String date = alarmData.getString("date");
            Bundle b = new Bundle();
            b.putInt("actionid", actionid);
            b.putInt("webduinosystemid", webduinosystemid);
            b.putString("param", param);
            b.putString("date", date);
            resultIntent.putExtras(b); //Put your id to your next Intent
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(resultIntent);


    }
}