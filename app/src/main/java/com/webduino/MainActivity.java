package com.webduino;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.webduino.chart.HistoryData;
import com.webduino.elements.Actuator;
import com.webduino.elements.Actuators;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.NextProgram;
import com.webduino.elements.NextPrograms;
import com.webduino.elements.Program;
import com.webduino.elements.Programs;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.HeaterFragment;
import com.webduino.fragment.HistoryFragment;
import com.webduino.fragment.NextProgramsFragment;
import com.webduino.fragment.PanelFragment;
import com.webduino.fragment.PrefsFragment;
import com.webduino.fragment.ProgramFragment;
import com.webduino.fragment.ProgramsListFragment;
import com.webduino.fragment.SensorsFragment;
import com.webduino.scenarios.Scenario;
import com.webduino.scenarios.Scenarios;
import com.webduino.zones.Zone;
import com.webduino.zones.Zones;

import android.Manifest;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.webduino.elements.HeaterActuator.Command_Off;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HeaterFragment.OnHeaterUpdatedListener, ProgramFragment.OnProgramUpdatedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status> {

    static final public int notificationId_ChangeStatus = 1;
    static final public int notificationId_ReleStatus = 2;
    static final public int notificationId_ActionButton = 3;

    public static final String notification_statuschange = "status_change";
    public static final String notification_restarted = "restart";
    public static final String notification_programchange = "program_change";
    public static final String notification_relestatuschange = "relestatus_change";
    public static final String notification_offline = "offline";
    public static final String notification_error = "error";
    public static final String notification_register = "register";


    protected ArrayList<Geofence> mGeofenceList;
    protected GoogleApiClient mGoogleApiClient;
    private Button mAddGeofencesButton;


    // GPSTracker class
    GPSTracker gps;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;


    public static Activity activity = null;

    PanelFragment panelFragment;
    SensorsFragment sensorsFragment;
    ProgramsListFragment programsFragment;
    NextProgramsFragment nextProgramFragment;
    HistoryFragment historyFragment;
    PrefsFragment preferencesFragment;
    private Menu menu;


    private MyReceiver myReceiver;
    private IntentFilter intentFilter;

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //MainActivity mainActivity = ((MainActivity) context.getApplicationContext());
            //getActuatorData();
            //mainActivity.etReceivedBroadcast.append("broadcast: "+intent.getAction()+"\n");
            }
    }


    public void getLocation() {
        // create class object
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;


        String action = getIntent().getStringExtra("action");
        int notificationId = getIntent().getIntExtra("notificationId", 0);


        if (notificationId != 0) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getSystemService(ns);
            nMgr.cancel(notificationId);
        }

        if (action != null && action.equals("manual_off")) {
            int actuatorId = getIntent().getIntExtra("actuatorId", 0);
            SendManualOff(actuatorId);
        }

        setContentView(R.layout.activity_main);


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ////geofence
        //mAddGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();
        // Get the geofences used. Geofence data is hard coded in this sample.
        populateGeofenceList();
        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();
        /// fine geofence


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                refreshData();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        panelFragment = new PanelFragment();
        sensorsFragment = new SensorsFragment();
        programsFragment = new ProgramsListFragment();
        nextProgramFragment = new NextProgramsFragment();
        historyFragment = new HistoryFragment();
        preferencesFragment = new PrefsFragment();

        showFragment(sensorsFragment);
        refreshData();

        myReceiver = new MyReceiver();
        intentFilter = new IntentFilter("com.webduino.USER_ACTION");
    }

    private void SendManualOff(int actuatorId) {

        String command = Command_Off;
        double temperature = 0;
        int zoneId = 0;
        int duration = 30;
        boolean remoteSensor = false;
        new requestDataTask(this, getAsyncResponse(), requestDataTask.POST_ACTUATOR_COMMAND).execute(actuatorId, command, duration, temperature, zoneId/*, remoteSensor*/);
    }


    // geofence
    public void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.LANDMARKS.entrySet()) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void addGeofencesButtonHandler(/*View view*/) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // end geofence

    private void refreshData() {
        getSensorData();
        getZoneData();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;

        //menu.removeItem(R.id.action_create_program);

        //MenuItem item = menu.findItem(R.id.action_create_program);
        //item.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_create_program) {
            programsFragment.createProgram();
            return true;
        } else if (id == R.id.action_delete_program) {
            programsFragment.deleteProgram();
            return true;
        } else if (id == R.id.action_get_location) {
            getLocation();
            return true;
        } else if (id == R.id.action_add_geofence) {
            addGeofencesButtonHandler();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Getting reference to the FragmentManager
        FragmentManager fragmentManager = getFragmentManager();
        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (id == R.id.nav_programs) {
            // Handle the camera action
            //getProgramData();
            //ft.replace(R.id.content_frame, programsFragment);
            showPrograms();
            //enableMenuItem(R.id.action_create_program);
        } else if (id == R.id.nav_sensors) {
            // Handle the gallery action
            ft.replace(R.id.content_frame, sensorsFragment);

            /*MenuItem actionitem = menu.findItem(R.id.action_create_program);
            actionitem.setVisible(false);*/

        } else if (id == R.id.nav_slideshow) {
            //getNextProgramData();
            //ft.replace(R.id.content_frame, nextProgramFragment);
            showSchedule();

        } else if (id == R.id.nav_manage) {

            ft.replace(R.id.content_frame, preferencesFragment);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

            //ft.replace(R.id.content_frame, historyFragment);
            //getDataLog();

        }
        ft.addToBackStack(null);
        // Committing the transaction
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void enableMenuItem(int id, boolean enable) {
        MenuItem actionitem = menu.findItem(id);
        actionitem.setVisible(enable);
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showPrograms() {
        getProgramData();
        showFragment(programsFragment);
    }

    public void showSchedule() {
        getNextProgramData();
        showFragment(nextProgramFragment);
    }

    public void showHistory(int actuatorId) {

        showFragment(historyFragment);
        getDataLog(actuatorId);
    }

    public void getSensorData() {
        new requestDataTask(MainActivity.activity, getAsyncResponse(), requestDataTask.REQUEST_SENSORS).execute();
    }

    public void getZoneData() {
        new requestDataTask(MainActivity.activity, getAsyncResponse(), requestDataTask.REQUEST_ZONES).execute();
    }

    public void getScenarioData() {
        new requestDataTask(MainActivity.activity, getAsyncResponse(), requestDataTask.REQUEST_SCENARIOS).execute();
    }

    public void getProgramData() {
        new requestDataTask(MainActivity.activity, getAsyncResponse(), requestDataTask.REQUEST_PROGRAMS).execute();
    }

    public void getNextProgramData() {
        new requestDataTask(MainActivity.activity, getAsyncResponse(), requestDataTask.REQUEST_NEXTPROGRAMS).execute();
    }

    public void getDataLog(int actuatorId) {

        new requestDataTask(MainActivity.activity, getAsyncResponse(), requestDataTask.REQUEST_DATALOG).execute(actuatorId,"heater");

        /*for (Sensor sensor : Sensors.list) {
            new requestDataTask(MainActivity.activity, getAsyncResponse(), requestDataTask.REQUEST_DATALOG).execute(sensor.getId(),"temperature");
        }*/

    }

    public void enableProgramListFragmentMenuItem(boolean enable) {
        enableMenuItem(R.id.action_create_program, enable);
    }

    public void enableProgramFragmentMenuItem(boolean enable) {
        enableMenuItem(R.id.action_create_program, enable);
        enableMenuItem(R.id.action_delete_program, enable);
    }

    /*@NonNull
    private AsyncRequestDataResponse getAsyncResponse() {
        return new AsyncRequestDataResponse() {*/
    @NonNull
    private WebduinoResponse getAsyncResponse() {
        return new WebduinoResponse() {

            @Override
            public void processFinishRegister(long shieldId, boolean error, String errorMessage) {
            }

            @Override
            public void processFinishSensors(List<Sensor> sensors, boolean error, String errorMessage) {

                if (error)
                    return;
                Sensors.list.clear();
                for (Sensor sensor : sensors) {
                    Sensors.add(sensor);
                }
                sensorsFragment.update();
            }

            @Override
            public void processFinishZones(List<Zone> zones, boolean error, String errorMessage) {

                if (error)
                    return;
                Zones.list.clear();
                for (Zone zone : zones) {
                    Zones.add(zone);
                }
                //sensorsFragment.update();
            }

            @Override
            public void processFinishScenarios(List<Scenario> scenarios, boolean error, String errorMessage) {

                if (error)
                    return;
                Scenarios.list.clear();
                for (Scenario scenario : scenarios) {
                    Scenarios.add(scenario);
                }
                //sensorsFragment.update();
            }

            @Override
            public void processFinishActuators(List<Actuator> actuators, boolean error, String errorMessage) {

                if (error)
                    return;
                Actuators.list.clear();
                for (Actuator actuator : actuators) {
                    Actuators.add(actuator);
                }
                sensorsFragment.update();
            }

            @Override
            public void processFinishPrograms(List<Object> programs, int requestType, boolean error, String errorMessage) {
                if (error)
                    return;
                if (requestType == requestDataTask.REQUEST_PROGRAMS) {
                    Programs.list.clear();
                    for (Object program : programs) {
                        Programs.add((Program) program);
                    }
                    programsFragment.update();

                } else if (requestType == requestDataTask.REQUEST_NEXTPROGRAMS) {
                    NextPrograms.list.clear();
                    for (Object nextProgram : programs) {
                        NextPrograms.add((NextProgram) nextProgram);
                    }
                    nextProgramFragment.update();
                }
            }
            @Override
            public void processFinishObjectList(List<Object> objectList, int requestType, boolean error, String errorMessage) {
                if (requestType == requestDataTask.REQUEST_DATALOG) {
                    List<HistoryData> list = new ArrayList<>();
                    for (Object data : objectList) {
                        list.add((HistoryData) data);
                    }
                    historyFragment.setDataLogList(list);

                }
            }

        };
    }

    @Override
    public void OnHeaterUpdated(HeaterActuator heaterActuator) {
        //sensorsFragment.updateActuator(heaterActuator);
        Actuators.update(heaterActuator);
    }

    @Override
    public void OnProgramUpdated() {
        getProgramData(); // questo serve a fare il refresh dei dati
        showFragment(programsFragment);
    }

    @Override
    public void OnProgramDeleted(int programId) {

        getProgramData();
        Programs.delete(programId);
        showFragment(programsFragment);
    }

    // geofence
    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do something with result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(
                    this,
                    "Geofences Added",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
        }
    }
    // end geofence
}
