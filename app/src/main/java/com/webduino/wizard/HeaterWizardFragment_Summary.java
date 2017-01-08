package com.webduino.wizard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webduino.R;

import org.w3c.dom.Text;


/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class HeaterWizardFragment_Summary extends Fragment {

    private String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.wizard_fragment_heater_summary, container, false);

        TextView tv = (TextView) v.findViewById(R.id.titleTextView);
        tv.setText(title);
        return v;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
