package com.webduino.fragment;

//import android.app.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.elements.Actuators;
import com.webduino.elements.HeaterActuator;
import com.webduino.elements.Program;
import com.webduino.elements.Programs;
import com.webduino.wizard.HeaterWizardActivity;
import com.webduino.wizard.ProgramWizardActivity;

import static android.app.Activity.RESULT_OK;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class ProgramFragment extends Fragment implements View.OnClickListener {

    public static final int PROGRAMWIZARD_EDIT = 1;  // The request code
    public static final int PROGRAMWIZARD_CREATE = 2;  // The request code

    private int programId;

    protected TextView textView;
    protected Button editButton;
    protected Program program = null;



    // Container Activity must implement this interface
    public interface OnProgramUpdatedListener {
        public void OnProgramUpdated(Program program);
    }

    OnProgramUpdatedListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnProgramUpdatedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnProgramUpdatedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        String strtext = getArguments().getString("programid");
        if (strtext != null) {
            programId = Integer.valueOf(strtext);
            program = Programs.getFromId(programId);
        }

        if (program == null)
            program = new Program();

        View v;
        v = inflater.inflate(R.layout.fragment_program, container, false);

        textView = (TextView) v.findViewById(R.id.textView);
        editButton = (Button) v.findViewById(R.id.editButton);
        editButton.setOnClickListener(this);


        String str = "";
        str += "id: " + program.id + "\n";
        str += "name: " + program.name + "\n";
        str += "date: " + program.dateEnabled + "\n";
        if (program.Monday)
            str += "L" + "\n";
        else
            str += "-" + "\n";
        if (program.Tuesday)
            str += "M" + "\n";
        else
            str += "-" + "\n";
        if (program.Wednesday)
            str += "M" + "\n";
        else
            str += "-" + "\n";
        if (program.Thursday)
            str += "G" + "\n";
        else
            str += "-" + "\n";
        if (program.Friday)
            str += "V" + "\n";
        else
            str += "-" + "\n";
        if (program.Saturday)
            str += "S" + "\n";
        else
            str += "-" + "\n";
        if (program.Sunday)
            str += "D" + "\n";
        else
            str += "-" + "\n";

        textView.setText(str);

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ProgramWizardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("programid", programId);
        intent.putExtras(bundle);
        startActivityForResult(intent, PROGRAMWIZARD_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PROGRAMWIZARD_EDIT || requestCode == PROGRAMWIZARD_CREATE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                MainActivity a = (MainActivity) getActivity();
                a.getProgramData();

            }
        }
    }

}
