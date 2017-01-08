package com.webduino.fragment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.webduino.R;
import com.webduino.controls.TimeView;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.NextProgramCardInfo;
import com.webduino.fragment.cardinfo.ProgramCardInfo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Giacomo Spanò on 17/12/2016.
 */

public class NextProgramAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CardInfo> nextProgramList;

    OnListener mCallback;
    public interface OnListener {
        void onProgramClick(int id);
    }
    public void setListener(OnListener listener) {
        mCallback = listener;
    }

    private static final int TYPE_NEXTPROGRAM = 1;
    //private static final int TYPE_HEATER = 2;

    public NextProgramAdapter(List<CardInfo> nextProgramList) {
        this.nextProgramList = nextProgramList;
    }

    public void swap(List list){
        if (nextProgramList != null) {
            nextProgramList.clear();
            nextProgramList.addAll(list);
        }
        else {
            nextProgramList = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return nextProgramList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        CardInfo ci = nextProgramList.get(i);

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

            case TYPE_NEXTPROGRAM:
                NextProgramViewHolder nextProgramViewHolder = (NextProgramViewHolder) viewHolder;
                NextProgramCardInfo pci = (NextProgramCardInfo) ci;

                nextProgramViewHolder.programId = pci.programId;
                nextProgramViewHolder.title.setText(pci.programName + "(" + pci.programName + ")");

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        /*if (nextProgramList.get(position) instanceof HeaterCardInfo )
            return TYPE_HEATER;
        else */if (nextProgramList.get(position) instanceof NextProgramCardInfo )
            return TYPE_NEXTPROGRAM;

        // here your custom logic to choose the view type
        //return position == 0 ? TYPE_HEATER : TYPE_NEXTPROGRAM;
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NEXTPROGRAM) {
            return (RecyclerView.ViewHolder) new NextProgramViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_nextprogram, parent, false));
        } /*else if (viewType == TYPE_HEATER) {
            return (RecyclerView.ViewHolder) new HeaterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_heater, parent, false));
        }*/
        return null;
    }

    public class NextProgramViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected TextView startDate, endDate;
        protected int programId;


        public NextProgramViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.titleEditText);
            startDate = (TextView) v.findViewById(R.id.startDateTextView);
            endDate = (TextView) v.findViewById(R.id.endDateTextView);

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