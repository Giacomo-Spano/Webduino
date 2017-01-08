package com.webduino.wizard;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.webduino.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giacomo Spanò on 21/12/2016.
 */

public class WizardActivity extends AppCompatActivity implements WizardFragment.OnNextListener {


    public static Activity activity;
    protected List<Fragment> fragmentList = new ArrayList<>();

    Button nextButton, backButton;
    protected int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        setContentView(R.layout.activity_wizard);

        backButton = (Button) findViewById(R.id.cancelButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }

    public void addStep(Fragment fragment) {
        fragmentList.add(fragment);
    }

    protected void showFragment(Fragment fragment) {

        if (fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void showFirstFragment() {
        position = 0;
        showCurrentFragment();
    }

    public void next() {

        if (fragmentList.size() > 0 && position < fragmentList.size() - 1) {
            position++;
            showCurrentFragment();
        } else if (position == fragmentList.size() - 1) {
            onWizardComplete();
        }
    }

    public void back() {
        if (fragmentList.size() > 0 && position > 0) {
            position--;
            showCurrentFragment();
        } else if (position == 0) {
            onWizardCancel();
        }
    }

    public void showCurrentFragment() {

        if (position == 0) {
            //backButton.setEnabled(false);
            backButton.setText("Annulla");
            nextButton.setText("Avanti");
        } else if (position == fragmentList.size() - 1) {
            backButton.setEnabled(true);
            backButton.setText("Indietro");
            nextButton.setText("Invia");
        } else {

            backButton.setEnabled(true);
            backButton.setText("Indietro");
            nextButton.setText("Avanti");
        }
        showFragment(fragmentList.get(position));

    }

    public void onWizardComplete() {

    }

    public void onWizardCancel() {
        setResult(RESULT_CANCELED);
        finish();     //Terminate the wizard
    }

    @Override
    public void OnNext(int option) {
        next();
    }
}