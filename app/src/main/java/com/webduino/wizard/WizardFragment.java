package com.webduino.wizard;

//import android.app.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.webduino.R;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class WizardFragment extends Fragment {


    public interface OnNextListener {
        public void OnNext(int option);
    }

    OnNextListener listener;

}
