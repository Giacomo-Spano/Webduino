package com.webduino.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.webduino.R;
import com.webduino.scenarios.ScenarioTimeInterval;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeIntervalFragment extends Fragment {

    ScenarioTimeInterval timeInterval = new ScenarioTimeInterval();
    EditText startdate, enddate;
    CheckBox cbenabled,cbsunday,cbmonday,cbtuesday,cbwednesday,cbthursday,cbfriday,cbsaturday;
    TextView tvname, tvdescription;

    private OnTimeIntervalFragmentInteractionListener mListener;

    public TimeIntervalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeinterval, container, false);

        tvname = view.findViewById(R.id.nameEditText);
        tvname.setText(timeInterval.name);
        tvdescription = view.findViewById(R.id.descriptionEditText);
        tvdescription.setText(timeInterval.description);

        cbenabled = view.findViewById(R.id.enabledCheckBox);
        cbenabled.setChecked(timeInterval.enabled);

        cbsunday = view.findViewById(R.id.sunday);
        cbsunday.setChecked(timeInterval.enabled);
        cbmonday = view.findViewById(R.id.monday);
        cbmonday.setChecked(timeInterval.monday);
        cbtuesday = view.findViewById(R.id.tuesday);
        cbtuesday.setChecked(timeInterval.tuesday);
        cbwednesday = view.findViewById(R.id.wednesday);
        cbwednesday.setChecked(timeInterval.wednesday);
        cbthursday = view.findViewById(R.id.thursday);
        cbthursday.setChecked(timeInterval.thursday);
        cbfriday = view.findViewById(R.id.friday);
        cbfriday.setChecked(timeInterval.friday);
        cbsaturday = view.findViewById(R.id.saturday);
        cbsaturday.setChecked(timeInterval.saturday);

        startdate = (EditText) view.findViewById(R.id.startdate);
        startdate.setKeyListener(null);
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartDateTimePickerDialog(timeInterval.startDateTime);
            }
        });
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        startdate.setText(df.format(timeInterval.startDateTime));

        startdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    showStartDateTimePickerDialog(timeInterval.startDateTime);
            }
        });

        enddate = (EditText) view.findViewById(R.id.enddate);
        enddate.setKeyListener(null);
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEndDateTimePickerDialog(timeInterval.endDateTime);
            }
        });
        enddate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    showEndDateTimePickerDialog(timeInterval.endDateTime);
            }
        });
        enddate.setText(df.format(timeInterval.endDateTime));

        ImageButton okbutton = view.findViewById(R.id.confirmButton);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeInterval.name = tvname.getText().toString();
                timeInterval.description = tvdescription.getText().toString();
                timeInterval.enabled = cbenabled.isChecked();
                timeInterval.sunday = cbsunday.isChecked();
                timeInterval.monday = cbmonday.isChecked();
                timeInterval.tuesday = cbtuesday.isChecked();
                timeInterval.wednesday = cbwednesday.isChecked();
                timeInterval.thursday = cbthursday.isChecked();
                timeInterval.friday = cbfriday.isChecked();
                timeInterval.saturday = cbsaturday.isChecked();

                if (mListener != null) {
                    mListener.onSaveTimeInterval(timeInterval);

                }
                getActivity().getFragmentManager().popBackStack();
            }
        });

        ImageButton cancelbutton = view.findViewById(R.id.cancelButton);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void addListener(OnTimeIntervalFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTimeIntervalFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveTimeInterval(ScenarioTimeInterval timeInterval);
    }

    public void showStartDateTimePickerDialog(Date datetime) {
        DatePickerFragment dateFragment= new DatePickerFragment();
        dateFragment.setDate(datetime);
        dateFragment.setListener(new DatePickerFragment.DataPickerListener() {
            @Override
            public void setDate(Date date) {
                timeInterval.startDateTime.setTime(date.getTime());
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                startdate.setText(df.format(date));
            }
            @Override
            public void cancel() {

            }
        });
        dateFragment.show(getFragmentManager(), "Data inizio");
    }
    public void showEndDateTimePickerDialog(Date datetime) {
        DatePickerFragment dateFragment= new DatePickerFragment();
        dateFragment.setDate(datetime);
        dateFragment.setListener(new DatePickerFragment.DataPickerListener() {
            @Override
            public void setDate(Date date) {
                timeInterval.endDateTime.setTime(date.getTime());
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                startdate.setText(df.format(date));
            }
            @Override
            public void cancel() {

            }
        });
        dateFragment.show(getFragmentManager(), "Data fine");
    }
}
