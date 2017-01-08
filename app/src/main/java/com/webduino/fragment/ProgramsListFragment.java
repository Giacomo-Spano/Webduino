package com.webduino.fragment;

//import android.app.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webduino.AsyncRequestDataResponse;
import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.elements.Actuator;
import com.webduino.elements.Program;
import com.webduino.elements.Programs;
import com.webduino.elements.Sensor;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.ProgramAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.ProgramCardInfo;

import java.util.ArrayList;
import java.util.List;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.Fragment;

/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class ProgramsListFragment extends Fragment implements ProgramAdapter.OnListener {

    public static final int HEATERWIZARD_REQUEST = 1;  // The request code

    private List<CardInfo> list;
    private ProgramAdapter programAdapter;
    private ProgramFragment programFragment = null;
    private int currentProgramId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.fragment_programlist, container, false);

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        recList.setLayoutManager(gridLayoutManager);


        programAdapter = new ProgramAdapter(createProgramList());
        recList.setAdapter(programAdapter);

        programAdapter.setListener(this);

        return v;
    }

    public void update() {

        list = createProgramList();
        programAdapter.swap(list);

        /*if (heaterFragment != null) {

            heaterFragment.update();
        }*/
    }

    public List<CardInfo> createProgramList() {

        List<CardInfo> result = new ArrayList<CardInfo>();

        for (Program program : Programs.list) {

            try {
                ProgramCardInfo ci = programCardInfoFromProgram((Program) program);
                result.add(ci);
            } catch (Exception e) {

            }
        }

        return result;
    }

    @NonNull
    private ProgramCardInfo programCardInfoFromProgram(Program program) {
        //HeaterActuator heater = actuator;
        ProgramCardInfo ci = new ProgramCardInfo();
        ci.program = program;
        return ci;
    }

    @Override
    public void onProgramClick(int id) {

        currentProgramId = id;

        showProgram(id);
    }

    private void showProgram(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("programid", id);
        programFragment = new ProgramFragment();
        programFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, programFragment);
        ft.addToBackStack(null);
        ft.commit();
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

    public void enableMenuItem(boolean enable) {
        MainActivity ma = (MainActivity) getActivity();
        ma.enableProgramListFragmentMenuItem(enable);
    }


    public void createProgram() {

        showProgram(-1); //
    }

    public void deleteProgram() {

        programFragment.deleteProgram();

    }


}
