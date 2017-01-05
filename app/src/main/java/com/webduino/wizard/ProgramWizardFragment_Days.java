package com.webduino.wizard;

//import android.app.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.webduino.R;
import com.webduino.elements.Program;
import com.webduino.elements.Sensor;
import com.webduino.elements.Sensors;

import java.util.ArrayList;
import java.util.List;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class ProgramWizardFragment_Days extends Fragment {

    private CheckBox monCheckBox;
    private CheckBox tueCheckBox;
    private CheckBox wedCheckBox;
    private CheckBox thuCheckBox;
    private CheckBox friCheckBox;
    private CheckBox satCheckBox;
    private CheckBox sunCheckBox;

    private boolean mon;
    private boolean tue;
    private boolean wed;
    private boolean thu;
    private boolean fri;
    private boolean sat;
    private boolean sun;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.wizard_fragment_program_days, container, false);

        monCheckBox = (CheckBox) v.findViewById(R.id.monCheckBox);
        tueCheckBox = (CheckBox) v.findViewById(R.id.tueCheckBox);
        wedCheckBox = (CheckBox) v.findViewById(R.id.wedCheckBox);
        thuCheckBox = (CheckBox) v.findViewById(R.id.thuCheckBox);
        friCheckBox = (CheckBox) v.findViewById(R.id.friCheckBox);
        satCheckBox = (CheckBox) v.findViewById(R.id.satCheckBox);
        sunCheckBox = (CheckBox) v.findViewById(R.id.sunCheckBox);

        update();

        return v;
    }

    private void update() {
        monCheckBox.setChecked(mon);
        tueCheckBox.setChecked(tue);
        wedCheckBox.setChecked(wed);
        thuCheckBox.setChecked(thu);
        friCheckBox.setChecked(fri);
        satCheckBox.setChecked(sat);
        sunCheckBox.setChecked(sun);
    }

    public void init(Program program) {

        mon = program.Monday;
        tue = program.Tuesday;
        wed = program.Wednesday;
        thu = program.Thursday;
        fri = program.Friday;
        sat = program.Saturday;
        sun = program.Sunday;
    }

    public boolean getSunday() {
        return sunCheckBox.isChecked();
    }

    public boolean getMonday() {
        return monCheckBox.isChecked();
    }

    public boolean getTuesday() {
        return tueCheckBox.isChecked();
    }

    public boolean getWednesday() {
        return wedCheckBox.isChecked();
    }

    public boolean getThursday() {
        return thuCheckBox.isChecked();
    }

    public boolean getFriday() {
        return friCheckBox.isAccessibilityFocused();
    }

    public boolean getSaturday() {
        return satCheckBox.isChecked();
    }

}
