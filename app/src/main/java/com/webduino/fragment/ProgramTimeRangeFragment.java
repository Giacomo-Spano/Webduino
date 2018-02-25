package com.webduino.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.ActionButtonCardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.ProgramActionCardInfo;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.BooleanOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.TimeOptionCardValue;
import com.webduino.scenarios.ProgramAction;
import com.webduino.scenarios.ScenarioProgramTimeRange;

import java.util.ArrayList;
import java.util.List;

public class ProgramTimeRangeFragment extends Fragment implements ProgramActionFragment.OnProgramActionFragmentInteractionListener {

    ScenarioProgramTimeRange timeRange;
    private CardAdapter actionsAdapter, optionsAdapter;
    OptionCardInfo optionCard_Name, optionCard_Description, optionCard_StartTime, optionCard_EndTime, optionCard_Enabled;

    private OnProgramTimeRangeFragmentInteractionListener mListener;

    public ProgramTimeRangeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_timerange, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView optionList = (RecyclerView) view.findViewById(R.id.optionList);
        optionList.setHasFixedSize(false);
        optionList.setLayoutManager(linearLayoutManager);
        optionsAdapter = new CardAdapter(this, createOptionList());
        optionList.setAdapter(optionsAdapter);
        optionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                OptionCardInfo optionCardInfo = (OptionCardInfo) cardInfo;
                optionCardInfo.value.setListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        optionsAdapter.notifyDataSetChanged();
                    }
                });
                optionCardInfo.value.showPicker();
            }
        });


        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView actionRecyclerView = (RecyclerView) view.findViewById(R.id.actionList);
        actionRecyclerView.setHasFixedSize(false);
        actionRecyclerView.setLayoutManager(linearLayoutManager);
        actionsAdapter = new CardAdapter(this, createActionList());
        actionRecyclerView.setAdapter(actionsAdapter);
        actionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                onActionClick(position,cardInfo);
            }
        });


        Button okbutton = view.findViewById(R.id.confirmButton);
        MainActivity.setImageButton(okbutton,getResources().getColor(R.color.colorPrimary),true,getResources().getDrawable(R.drawable.check));
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeRange.enabled = optionCard_Enabled.value.getBoolValue();
                timeRange.name = optionCard_Name.value.getStringValue();
                timeRange.description = optionCard_Description.value.getStringValue();
                timeRange.startTime = optionCard_StartTime.value.getDateValue();
                timeRange.endTime = optionCard_EndTime.value.getDateValue();

                if (mListener != null) {
                    mListener.onSaveProgramTimeRange(timeRange);

                }
                getActivity().getFragmentManager().popBackStack();
            }
        });

        Button cancelbutton = view.findViewById(R.id.cancelButton);
        MainActivity.setImageButton(cancelbutton,getResources().getColor(R.color.colorPrimary),false,getResources().getDrawable(R.drawable.uncheck));
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private List<CardInfo> createActionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();
        for(ProgramAction action:timeRange.programActionList) {
            ProgramActionCardInfo actioncardinfo = new ProgramActionCardInfo();
            actioncardinfo.id = action.id;
            actioncardinfo.name = action.name;
            actioncardinfo.action = action;
            actioncardinfo.setEnabled(action.enabled);
            result.add(actioncardinfo);
        }
        CardInfo addButton = new ActionButtonCardInfo();
        addButton.id = 0;
        addButton.name = "Aggiungi azione";
        addButton.label = " ";
        addButton.imageDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.calendar, null);
        addButton.setColor(Color.BLUE);
        result.add(addButton);
        return result;
    }

    public List<CardInfo> createOptionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();

        optionCard_Enabled = new OptionCardInfo();
        optionCard_Enabled.value = new BooleanOptionCardValue("Stato",timeRange.enabled,"Abilitato","Disabilitato");
        result.add(optionCard_Enabled);

        optionCard_Name = new OptionCardInfo();
        optionCard_Name.value = new StringOptionCardValue("Nome",timeRange.name);
        result.add(optionCard_Name);

        optionCard_Description = new OptionCardInfo();
        optionCard_Description.value = new StringOptionCardValue("Descrizione",timeRange.description);
        result.add(optionCard_Description);

        optionCard_StartTime = new OptionCardInfo();
        optionCard_StartTime.value = new TimeOptionCardValue("Ora inizio",timeRange.startTime);
        result.add(optionCard_StartTime);

        optionCard_EndTime = new OptionCardInfo();
        optionCard_EndTime.value = new TimeOptionCardValue("Ora fine",timeRange.endTime);
        result.add(optionCard_EndTime);
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void addListener(OnProgramTimeRangeFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onActionClick(int position, CardInfo cardInfo) {
        if (cardInfo instanceof ProgramActionCardInfo) {

            ProgramActionCardInfo programActionCardInfo = (ProgramActionCardInfo) cardInfo;
            showActionFragment(programActionCardInfo);
        } else if (cardInfo instanceof ActionButtonCardInfo) {

            ProgramActionCardInfo programActionCardInfo = new ProgramActionCardInfo();
            showActionFragment(programActionCardInfo);
        }
    }

    private void showActionFragment(ProgramActionCardInfo programActionCardInfo) {
        ProgramActionFragment programActionFragment = new ProgramActionFragment();
        programActionFragment.addListener(this);
        ProgramAction action;
        if (programActionCardInfo.id == 0)
            action = new ProgramAction();
        else
            action = programActionCardInfo.action;

        if (timeRange != null) {
            programActionFragment.action = action;

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, (Fragment) programActionFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onSaveProgramAction(ProgramAction action) {

    }

    public interface OnProgramTimeRangeFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveProgramTimeRange(ScenarioProgramTimeRange timeRange);
    }
}
