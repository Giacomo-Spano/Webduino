package com.webduino.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;

import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.iid.FirebaseInstanceId;
import com.webduino.Actuator;
import com.webduino.AsyncRequestDataResponse;
import com.webduino.R;
import com.webduino.Sensor;
import com.webduino.requestDataTask;

//import android.preference.PreferenceFragment;
import android.app.Fragment;

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

        new requestDataTask(new AsyncRequestDataResponse() {

            @Override
            public void processFinishRegister(long shieldId, boolean error, String errorMessage) {

            }

            @Override
            public void processFinishSensors(List<Sensor> sensors, boolean error, String errorMessage) {

            }

            @Override
            public void processFinishActuators(List<Actuator> actuators, boolean error, String errorMessage) {

            }

            @Override
            public void processFinishSendCommand(Actuator actuator, boolean error, String errorMessage) {

            }
        }, requestDataTask.REQUEST_REGISTERDEVICE).execute(refreshedToken, model);

    }
}