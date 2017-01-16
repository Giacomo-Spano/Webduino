package com.webduino.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import android.support.v7.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.webduino.WebduinoResponse;
import com.webduino.elements.Actuator;
import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.elements.Program;
import com.webduino.elements.Sensor;
import com.webduino.elements.requestDataTask;

import java.util.List;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference);

        android.preference.Preference pref = findPreference(getString(R.string.server_url_preference));

        Context hostActivity = getActivity();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(hostActivity);
        if (prefs != null) {

            String key = getString(R.string.server_url_preference_key);
            String serverurl = prefs.getString(key, "");
            pref.setSummary(serverurl);
        }

        pref.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.preference.Preference preference, Object newValue) {

                preference.setSummary((String) newValue);

                Context hostActivity = getActivity();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(hostActivity);
                String key = getString(R.string.server_url_preference_key);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key, newValue.toString());
                editor.commit();

                sendRegistrationToServer();

                return true;
                //return false;
            }
        });

    }

    private void sendRegistrationToServer() {
        // TODO: Implement this method to send token to your app server.

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //String tokenId = (String) params[0];
        String model = Build.MODEL;

        new requestDataTask(MainActivity.activity, new WebduinoResponse() {

            @Override
            public void processFinishRegister(long shieldId, boolean error, String errorMessage) {
                // queto viene chiamato in fase di registrazione device per notifiche google
            }

        }, requestDataTask.REQUEST_REGISTERDEVICE).execute(refreshedToken, model);

    }
}