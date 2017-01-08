package com.webduino.wizard;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.webduino.R;
import com.webduino.elements.Program;

import static android.R.attr.name;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class ProgramWizardFragment_Name extends Fragment {

    private EditText nameEditText;
    private String name = "program name";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.wizard_fragment_program_name, container, false);

        nameEditText = (EditText) v.findViewById(R.id.programNameEditText);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = editable.toString();
            }
        });
        update();

        return v;
    }

    private void update() {
        nameEditText.setText(name);
    }

    public void init(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

}
