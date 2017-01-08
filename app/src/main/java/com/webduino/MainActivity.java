package com.webduino;

//import android.app.FragmentManager;
//import android.app.FragmentTransaction;

;
//import android.app.FragmentTransaction;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.webduino.elements.Actuator;
import com.webduino.elements.Actuators;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.Program;
import com.webduino.elements.Programs;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.HeaterFragment;
import com.webduino.fragment.NextProgramsFragment;
import com.webduino.fragment.PanelFragment;
import com.webduino.fragment.PrefsFragment;
import com.webduino.fragment.ProgramFragment;
import com.webduino.fragment.ProgramsListFragment;
import com.webduino.fragment.SensorsFragment;


import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HeaterFragment.OnHeaterUpdatedListener, ProgramFragment.OnProgramUpdatedListener {

    public static Activity activity;

    PanelFragment panelFragment;
    SensorsFragment sensorsFragment;
    ProgramsListFragment programsFragment;
    NextProgramsFragment nextProgramFragment;
    PrefsFragment preferencesFragment;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        setContentView(R.layout.activity_main);
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
        preferencesFragment = new PrefsFragment();

        showFragment(sensorsFragment);
        refreshData();
    }

    private void refreshData() {
        getSensorData();
        getActuatorData();

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
            getProgramData();
            ft.replace(R.id.content_frame, programsFragment);
            //enableMenuItem(R.id.action_create_program);
        } else if (id == R.id.nav_sensors) {
            // Handle the gallery action
            ft.replace(R.id.content_frame, sensorsFragment);

            /*MenuItem actionitem = menu.findItem(R.id.action_create_program);
            actionitem.setVisible(false);*/

        } else if (id == R.id.nav_slideshow) {

            ft.replace(R.id.content_frame, nextProgramFragment);

        } else if (id == R.id.nav_manage) {

            ft.replace(R.id.content_frame, preferencesFragment);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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

    public void getSensorData() {
        new requestDataTask(MainActivity.activity, getAsyncResponse(), requestDataTask.REQUEST_SENSORS).execute();
    }

    public void getActuatorData() {
        new requestDataTask(MainActivity.activity, getAsyncResponse(), requestDataTask.REQUEST_ACTUATORS).execute();
    }

    public void getProgramData() {
        new requestDataTask(MainActivity.activity, getAsyncResponse(), requestDataTask.REQUEST_PROGRAMS).execute();
    }

    public void enableProgramListFragmentMenuItem(boolean enable) {
        enableMenuItem(R.id.action_create_program, enable);
    }

    public void enableProgramFragmentMenuItem(boolean enable) {
        enableMenuItem(R.id.action_create_program, enable);
        enableMenuItem(R.id.action_delete_program,enable);
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
            public void processFinishPrograms(List<Program> programs, int requestType, boolean error, String errorMessage) {
                if (error)
                    return;

                Programs.list.clear();
                for (Program program : programs) {
                    Programs.add(program);
                }

                programsFragment.update();
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
}
