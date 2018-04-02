package com.webduino.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.webduino.MainActivity;
import com.webduino.R;
import com.webduino.WebduinoResponse;
import com.webduino.elements.Trigger;
import com.webduino.elements.Triggers;
import com.webduino.elements.requestDataTask;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.OptionCardInfo;
import com.webduino.fragment.cardinfo.optioncardvalue.BooleanOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.DateOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.IntegerOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.ListOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.MultiChoiceOptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.OptionCardValue;
import com.webduino.fragment.cardinfo.optioncardvalue.StringOptionCardValue;
import com.webduino.scenarios.ScenarioProgram;
import com.webduino.scenarios.ScenarioTimeInterval;
import com.webduino.scenarios.ScenarioTrigger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TriggerFragment extends Fragment {

    ScenarioTrigger trigger = new ScenarioTrigger();
    private CardAdapter optionsAdapter;
    OptionCardInfo optionCard_Enabled, optionCard_Name, optionCard_Description, optionCard_TriggerId, optionCard_Priority;

    private OnTriggerFragmentInteractionListener mListener;

    public TriggerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trigger, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView optionList = (RecyclerView) view.findViewById(R.id.optionList);
        optionList.setHasFixedSize(true);
        optionList.setLayoutManager(linearLayoutManager);
        optionsAdapter = new CardAdapter(this, createOptionList());
        optionList.setAdapter(optionsAdapter);
        optionsAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                OptionCardInfo optionCardInfo = (OptionCardInfo) cardInfo;
                optionCardInfo.value.addListener(new OptionCardValue.OptionCardListener() {
                    @Override
                    public void onSetValue(Object value) {
                        optionsAdapter.notifyDataSetChanged();
                    }
                });
                optionCardInfo.value.showPicker();
            }
        });

        Button okbutton = view.findViewById(R.id.confirmButton);
        MainActivity.setImageButton(okbutton, getResources().getColor(R.color.colorPrimary), true, getResources().getDrawable(R.drawable.check));
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                trigger.name = optionCard_Name.value.getStringValue();
                trigger.description = optionCard_Description.value.getStringValue();
                trigger.triggerid = optionCard_TriggerId.value.getIntValue();
                trigger.priority = optionCard_Priority.value.getIntValue();
                trigger.enabled = optionCard_Enabled.value.getBoolValue();
                saveTrigger();
            }
        });

        Button cancelbutton = view.findViewById(R.id.cancelButton);
        MainActivity.setImageButton(cancelbutton, getResources().getColor(R.color.colorPrimary), false, getResources().getDrawable(R.drawable.uncheck));
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        ((MainActivity) getActivity()).enableDeleteMenuItem(true);
        return view;
    }

    public void saveTrigger() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    trigger = (ScenarioTrigger) result;
                    if (mListener != null) {
                        mListener.onSaveTrigger(trigger);
                    }
                    getActivity().getFragmentManager().popBackStack();
                }
            }
        }, requestDataTask.POST_SCENARIOTRIGGER).execute(trigger, false);
    }

    public void deleteTrigger() {
        new requestDataTask(MainActivity.activity, new WebduinoResponse() {
            @Override
            public void processFinish(Object result, int requestType, boolean error, String errorMessage) {
                if (!error) {
                    mListener.onDeleteTrigger(trigger);
                    getActivity().getFragmentManager().popBackStack();
                    ((MainActivity) getActivity()).getScenarioData();
                }
            }
        }, requestDataTask.POST_SCENARIOTRIGGER).execute(trigger, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteTrigger();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void addListener(OnTriggerFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTriggerFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSaveTrigger(ScenarioTrigger trigger);

        void onDeleteTrigger(ScenarioTrigger trigger);
    }

    public List<CardInfo> createOptionList() {
        List<CardInfo> result = new ArrayList<CardInfo>();

        optionCard_Enabled = new OptionCardInfo();
        optionCard_Enabled.value = new BooleanOptionCardValue(getString(R.string.status), trigger.enabled, getString(R.string.enabled), getString(R.string.disabled));
        result.add(optionCard_Enabled);

        optionCard_Name = new OptionCardInfo();
        optionCard_Name.value = new StringOptionCardValue(getString(R.string.name), trigger.name);
        result.add(optionCard_Name);

        optionCard_Description = new OptionCardInfo();
        optionCard_Description.value = new StringOptionCardValue(getString(R.string.description), trigger.description);
        result.add(optionCard_Description);

        optionCard_Priority = new OptionCardInfo();
        optionCard_Priority.value = new IntegerOptionCardValue(getString(R.string.priority), trigger.priority);
        result.add(optionCard_Priority);

        CharSequence[] items = new CharSequence[Triggers.list.size()];
        int[] itemValues = new int[Triggers.list.size()];
        int i = 0;
        for (Trigger trigger : Triggers.list) {
            items[i] = trigger.name;
            itemValues[i] = trigger.id;
            i++;
        }
        optionCard_TriggerId = new OptionCardInfo();
        optionCard_TriggerId.value = new ListOptionCardValue(getString(R.string.trigger), trigger.triggerid, items, itemValues);
        result.add(optionCard_TriggerId);

        return result;
    }
}
