package com.webduino.fragment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.webduino.R;
import com.webduino.controls.TimeView;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.ProgramCardInfo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class ProgramAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CardInfo> programList;

    OnListener mCallback;
    public interface OnListener {
        void onProgramClick(int id);
    }
    public void setListener(OnListener listener) {
        mCallback = listener;
    }

    private static final int TYPE_PROGRAM = 1;
    //private static final int TYPE_HEATER = 2;

    public ProgramAdapter(List<CardInfo> programList) {
        this.programList = programList;
    }

    public void swap(List list){
        if (programList != null) {
            programList.clear();
            programList.addAll(list);
        }
        else {
            programList = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return programList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        CardInfo ci = programList.get(i);

        switch (viewHolder.getItemViewType()) {

            /*case TYPE_HEATER:
                HeaterViewHolder heaterViewHolder = (HeaterViewHolder) viewHolder;
                HeaterCardInfo hci = (HeaterCardInfo) ci;

                heaterViewHolder.id = hci.actuatorId;
                //heaterViewHolder.heater = hci.heater;
                heaterViewHolder.title.setText(hci.name);
                heaterViewHolder.target.setText(""+hci.target+"°");
                heaterViewHolder.status.setText(hci.status);
                if (hci.releStatus)
                    heaterViewHolder.status.setBackgroundColor(Color.GREEN);
                else
                    heaterViewHolder.status.setBackgroundColor(Color.RED);

                break;*/

            case TYPE_PROGRAM:
                ProgramViewHolder programViewHolder = (ProgramViewHolder) viewHolder;
                ProgramCardInfo pci = (ProgramCardInfo) ci;

                programViewHolder.programId = pci.program.id;
                programViewHolder.title.setText(pci.program.name + "(" + pci.program.id + ")");
                if (!pci.program.active) {
                    programViewHolder.startDate.setText("non attivo");
                    programViewHolder.endDate.setText("");
                } else if (pci.program.dateEnabled) {
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm");
                    programViewHolder.startDate.setText(df.format(pci.program.startDate));
                    programViewHolder.endDate.setText(df.format(pci.program.endDate));
                } else {
                    programViewHolder.startDate.setText("sempre attivo");
                    programViewHolder.endDate.setText("");
                }
                programViewHolder.su.setChecked(pci.program.Sunday);
                programViewHolder.mo.setChecked(pci.program.Monday);
                programViewHolder.tu.setChecked(pci.program.Tuesday);
                programViewHolder.we.setChecked(pci.program.Wednesday);
                programViewHolder.th.setChecked(pci.program.Thursday);
                programViewHolder.fr.setChecked(pci.program.Friday);
                programViewHolder.sa.setChecked(pci.program.Saturday);
                programViewHolder.su.setEnabled(false);
                programViewHolder.mo.setEnabled(false);
                programViewHolder.tu.setEnabled(false);
                programViewHolder.we.setEnabled(false);
                programViewHolder.th.setEnabled(false);
                programViewHolder.fr.setEnabled(false);
                programViewHolder.sa.setEnabled(false);


                programViewHolder.timeLine.removeAll();
                programViewHolder.timeLine.add(pci.program.timeRanges);

                //programViewHolder.target.setText(""+pci.target+"°");

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        /*if (programList.get(position) instanceof HeaterCardInfo )
            return TYPE_HEATER;
        else */if (programList.get(position) instanceof ProgramCardInfo )
            return TYPE_PROGRAM;

        // here your custom logic to choose the view type
        //return position == 0 ? TYPE_HEATER : TYPE_PROGRAM;
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PROGRAM) {
            return (RecyclerView.ViewHolder) new ProgramViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_program, parent, false));
        } /*else if (viewType == TYPE_HEATER) {
            return (RecyclerView.ViewHolder) new HeaterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_heater, parent, false));
        }*/
        return null;
    }

    public class ProgramViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected TextView startDate, endDate;
        protected CheckBox su, mo, tu, we, th, fr, sa;
        protected TimeView timeLine;
        protected int programId;


        public ProgramViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.titleEditText);
            startDate = (TextView) v.findViewById(R.id.startDateTextView);
            endDate = (TextView) v.findViewById(R.id.endDateTextView);
            timeLine = (TimeView) v.findViewById(R.id.timeLineView);

            su = (CheckBox) v.findViewById((R.id.suCheckBox));
            mo = (CheckBox) v.findViewById((R.id.moCheckBox));
            tu = (CheckBox) v.findViewById((R.id.tuCheckBox));
            we = (CheckBox) v.findViewById((R.id.weCheckBox));
            th = (CheckBox) v.findViewById((R.id.thCheckBox));
            fr = (CheckBox) v.findViewById((R.id.frCheckBox));
            sa = (CheckBox) v.findViewById((R.id.saCheckBox));

            v.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    mCallback.onProgramClick(programId);
                }
            });
        }
    }

    /*public class HeaterViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView status;
        protected TextView target;
        protected int id;


        public HeaterViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.title);
            status = (TextView)  v.findViewById(R.id.status);
            target = (TextView) v.findViewById(R.id.target);
            v.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    mCallback.onHeaterClick(id);
                }
            });
        }
    }*/
}