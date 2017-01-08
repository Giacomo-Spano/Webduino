package com.webduino.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.elements.Program;
import com.webduino.elements.Programs;
import com.webduino.fragment.adapters.NextProgramAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.NextProgramCardInfo;
import com.webduino.fragment.cardinfo.ProgramCardInfo;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Giacomo Spanò on 16/11/2016.
 */

public class NextProgramsFragment extends Fragment implements NextProgramAdapter.OnListener {

    //public static final int HEATERWIZARD_REQUEST = 1;  // The request code

    private List<CardInfo> list;
    private NextProgramAdapter nextProgramAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

        }

        View v;
        v = inflater.inflate(R.layout.fragment_nextprograms, container, false);

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        recList.setLayoutManager(gridLayoutManager);


        nextProgramAdapter = new NextProgramAdapter(createNextProgramList());
        recList.setAdapter(nextProgramAdapter);

        nextProgramAdapter.setListener(this);

        return v;
    }

    public void update() {

        list = createNextProgramList();
        nextProgramAdapter.swap(list);
    }

    public List<CardInfo> createNextProgramList() {

        List<CardInfo> result = new ArrayList<CardInfo>();

        for (Program program : Programs.list) {

            try {
                NextProgramCardInfo ci = nextProgramCardInfoFromProgram((Program) program);
                result.add(ci);
            } catch (Exception e) {

            }
        }

        return result;
    }

    @NonNull
    private NextProgramCardInfo nextProgramCardInfoFromProgram(Program program) {
        //HeaterActuator heater = actuator;
        NextProgramCardInfo ci = new NextProgramCardInfo();
        ci.programName = program.name;
        return ci;
    }

    @Override
    public void onProgramClick(int id) {


        // questo potrebbe servire
        //showProgram(id);
    }

    // questo potrebbe servire
    /*private void showProgram(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("programid", id);
        programFragment = new ProgramFragment();
        programFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, programFragment);
        ft.addToBackStack(null);
        ft.commit();
    }*/

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
        // mettere gli item giusti
        //ma.enableProgramListFragmentMenuItem(enable);
    }

}
