package com.webduino.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.WebduinoResponse;
import com.webduino.elements.Program;
import com.webduino.elements.Programs;
import com.webduino.elements.TimeRange;
import com.webduino.elements.requestDataTask;
import com.webduino.wizard.ProgramWizardActivity;

import java.sql.Time;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class ProgramFragment extends Fragment implements View.OnClickListener {

    public static final int PROGRAMWIZARD_EDIT = 1;  // The request code
    public static final int PROGRAMWIZARD_CREATE = 2;  // The request code

    protected TextView textView;
    protected Button editButton;
    protected Program program = new Program();

    public void deleteProgram() {
        new requestDataTask(getActivity(), requestDataCallback(), requestDataTask.POST_DELETEPROGRAM).execute(program.id);
    }


    // Container Activity must implement this interface
    public interface OnProgramUpdatedListener {
        public void OnProgramUpdated();
        public void OnProgramDeleted(int programId);
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

        View v;
        v = inflater.inflate(R.layout.fragment_program, container, false);

        textView = (TextView) v.findViewById(R.id.textView);
        editButton = (Button) v.findViewById(R.id.editButton);
        editButton.setOnClickListener(this);

        int id = getArguments().getInt("programid");
        if (id != -1) {
            program = Programs.getFromId(id);
            update();
        } else {
            /*program = new Program();
            int newId = Programs.getMaxId();
            program.id = newId+1;
            TimeRange tr = new TimeRange();
            tr.starTime = Time.valueOf("00:00:00");
            tr.endTime = Time.valueOf("23:59:00");
            tr.temperature = 15.0;
            tr.name = "fascia1";
            program.timeRanges.add(tr);
            update();*/
            startProgramWizard();
        }

        return v;
    }

    private void update() {
        String str = "";
        str += "id: " + program.id + "\n";
        str += "name: " + program.name + "\n";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (!program.active) {
            str += "\nNon attivo";
        } else if (program.dateEnabled ) {
            str += "\nDa: " + sdf.format(program.startDate);
            str += "\nA" + sdf.format(program.endDate);
        } else {
            str += "\nSempre attivo";
        }

        str += "date: " + program.dateEnabled + "\n";
        if (program.Monday)
            str += "L";
        else
            str += "-";
        if (program.Tuesday)
            str += "M";
        else
            str += "-";
        if (program.Wednesday)
            str += "M";
        else
            str += "-";
        if (program.Thursday)
            str += "G";
        else
            str += "-";
        if (program.Friday)
            str += "V";
        else
            str += "-";
        if (program.Saturday)
            str += "S";
        else
            str += "-";
        if (program.Sunday)
            str += "D";
        else
            str += "-";

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:00");
        for (TimeRange timeRange : program.timeRanges) {
            str += "\n DALLE " + df.format(timeRange.starTime);
            str += " ALLE " + df.format(timeRange.endTime);
            str += " - " + timeRange.name;
            str += " " + timeRange.temperature + "°C";
        }

        textView.setText(str);
    }

    @Override
    public void onClick(View v) {
        startProgramWizard();

    }

    private void startProgramWizard() {
        Intent intent = new Intent(getActivity(), ProgramWizardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("programid", program.id);
        intent.putExtras(bundle);
        startActivityForResult(intent, PROGRAMWIZARD_EDIT);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        enableMenuItem(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        enableMenuItem(false);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == PROGRAMWIZARD_EDIT || requestCode == PROGRAMWIZARD_CREATE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mListener.OnProgramUpdated();
                //MainActivity a = (MainActivity) getActivity();
                //a.getProgramData(); // questo serve a fare il refresh dei dati
            }
        }
    }

    public void enableMenuItem(boolean enable) {
        MainActivity ma = (MainActivity) getActivity();
        ma.enableProgramFragmentMenuItem(enable);
    }

    @NonNull
    private WebduinoResponse requestDataCallback() {
        return new WebduinoResponse() {

            @Override
            public void processFinishPostProgram(boolean response, int requestType, boolean error, String errorMessage) {
                if (!error && requestType == requestDataTask.POST_DELETEPROGRAM) {

                    new AlertDialog.Builder(getContext())
                            .setTitle("Cancellazione programma")
                            .setMessage("Programma cancellato")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Whatever...
                                    mListener.OnProgramDeleted(program.id);
                                }
                            }).show();

                } else {
                    /// aggiungere messaggio di errore
                }
            }
        };
    }
}
