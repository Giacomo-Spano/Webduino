package com.webduino.fragment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.webduino.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


import static com.webduino.R.layout.heaterdatarowlayout;

/**
 * Created by giacomo on 19/07/2015.
 */
public interface HeaterListListener {

    public void onClickCheckBox(int position, boolean selected);
    public void onClick(long spotId);



    class HeaterListArrayAdapter extends ArrayAdapter<ListItem> {

        private LayoutInflater mInflater;

        ArrayList<ListItem> dataList = new ArrayList<>();

        private final Context context;
        HeaterListListener mListener;

        public HeaterListArrayAdapter(Context context, ArrayList<ListItem> list, HeaterListListener listener) {

            super(context, heaterdatarowlayout, list);

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            //
            mListener = listener;
            this.context = context;

            this.dataList = list;
        }



        @Override
        public int getViewTypeCount() {
            return ListItem.HeaterItemTypescount;//5; //return 2, you have two types that the getView() method will return, normal(0) and for the last row(1)
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public ListItem getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            ListItem item = getItem(position);
            return item.type;
            //return (position == 0) ? 0 : 1; //if we are at the last position then return 1, for any other position return 0
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("getView " + position + " " + convertView);
            ViewHolder holder = null;
            if (convertView == null) {

                if (dataList.get(position).type == ListItem.HeaterDataHeader) {
                    convertView = mInflater.inflate(R.layout.heaterdataheaderlayout, null);
                    holder = new HeaderDataHeaderViewHolder();
                    holder.textView = (TextView)convertView.findViewById(R.id.descriptionTextView);

                } else if (dataList.get(position).type == ListItem.HeaterDataRow) {
                    convertView = mInflater.inflate(R.layout.heaterdatarowlayout, null);
                    holder = new HeaterDataRowViewHolder();
                    HeaterDataRowViewHolder rvHolder = (HeaterDataRowViewHolder) holder;
                    rvHolder.textView = (TextView)convertView.findViewById(R.id.descriptionTextView);
                    rvHolder.valueTextView = (TextView) convertView.findViewById(R.id.valueTextView);
                    rvHolder.commentTextView = (TextView) convertView.findViewById(R.id.commentTextView);

                } else if (dataList.get(position).type == ListItem.HeaterNextActionRow) {
                    convertView = mInflater.inflate(R.layout.heaternextactionrowlayout, null);
                    holder = new HeaterNextActionRowViewHolder();
                    HeaterNextActionRowViewHolder rvHolder = (HeaterNextActionRowViewHolder) holder;
                    rvHolder.startTextView = (TextView)convertView.findViewById(R.id.startTextView);
                    rvHolder.endTextView = (TextView) convertView.findViewById(R.id.endTextView);
                    rvHolder.targetTextView = (TextView) convertView.findViewById(R.id.targetTextView);
                    rvHolder.scenarioTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
                    rvHolder.programTextView = (TextView) convertView.findViewById(R.id.programTextView);
                    rvHolder.actionTextView = (TextView) convertView.findViewById(R.id.actionTextView);
                    rvHolder.zoneTextView = (TextView) convertView.findViewById(R.id.zoneTextView);

                } else if (dataList.get(position).type == ListItem.HeaterNextActionIdleRow) {
                    convertView = mInflater.inflate(R.layout.heaternextactionidlerowlayout, null);
                    holder = new HeaterNextActionIdleViewHolder();
                    HeaterNextActionIdleViewHolder rvHolder = (HeaterNextActionIdleViewHolder) holder;
                    rvHolder.startTextView = (TextView)convertView.findViewById(R.id.startTextView);
                    rvHolder.endTextView = (TextView) convertView.findViewById(R.id.endTextView);
                    rvHolder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);

                } else if (dataList.get(position).type == ListItem.HeaterNextActionHeader) {
                    convertView = mInflater.inflate(R.layout.heaternextactionheaderlayout, null);
                    holder = new HeaterNextActionHeaderViewHolder();
                    HeaterNextActionHeaderViewHolder rvHolder = (HeaterNextActionHeaderViewHolder) holder;
                    rvHolder.textView = (TextView)convertView.findViewById(R.id.descriptionTextView);

                } else if (dataList.get(position).type == ListItem.HeaterCommandLogRow) {
                    convertView = mInflater.inflate(R.layout.heatercommandlogrowlayout, null);
                    holder = new HeaterCommandLogRowViewHolder();
                    HeaterCommandLogRowViewHolder rvHolder = (HeaterCommandLogRowViewHolder) holder;
                    rvHolder.dateTextView = (TextView)convertView.findViewById(R.id.dateTextView);
                    rvHolder.commandTextView = (TextView) convertView.findViewById(R.id.commandTextView);
                    rvHolder.durationTextView = (TextView) convertView.findViewById(R.id.durationTextView);
                    rvHolder.targetTextView = (TextView) convertView.findViewById(R.id.targetTextView);

                } else {
                    return null;
                }
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            //holder.textView.setText(dataList.get(position).description);
            holder.update(dataList.get(position));
            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView textView;
        public void update(ListItem item) {
            textView.setText(item.description);
        }
    }

    public static class HeaderDataHeaderViewHolder extends ViewHolder {
        public void update(ListItem item) {
            if (item instanceof HeaterDataHeaderItem) {
                HeaterDataHeaderItem dataItem = (HeaterDataHeaderItem) item;
                textView.setText(dataItem.description);
            }
        }
    }

    public static class HeaterDataRowViewHolder extends ViewHolder {

        public TextView valueTextView;
        public TextView commentTextView;

        public void update(ListItem item) {
            if (item instanceof HeaterDataRowItem) {
                HeaterDataRowItem dataItem = (HeaterDataRowItem) item;
                textView.setText(dataItem.description);
                valueTextView.setText(dataItem.value);
                commentTextView.setText("");
            }
        }
    }

    public static class HeaterNextActionRowViewHolder extends ViewHolder {

        public TextView targetTextView;
        public TextView startTextView;
        public TextView endTextView;
        public TextView scenarioTextView;
        public TextView programTextView;
        public TextView actionTextView;
        public TextView zoneTextView;

        public void update(ListItem item) {
            if (item instanceof HeaterNextActionRowItem) {
                HeaterNextActionRowItem dataItem = (HeaterNextActionRowItem) item;
                String target = "" + dataItem.targetvalue + "°C";
                targetTextView.setText(target);

                SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                if (dataItem.start != null)
                    startTextView.setText(tf.format(dataItem.start));
                if (dataItem.end != null)
                    endTextView.setText(tf.format(dataItem.end));
                scenarioTextView.setText(dataItem.scenario);
                programTextView.setText(dataItem.program);
                actionTextView.setText(dataItem.actiontype);
                zoneTextView.setText(dataItem.zone);
            }
        }
    }

    public static class HeaterNextActionIdleViewHolder extends ViewHolder {

        public TextView startTextView;
        public TextView endTextView;
        public TextView descriptionTextView;

        public void update(ListItem item) {
            if (item instanceof HeaterNextActionIdleRowItem) {
                HeaterNextActionIdleRowItem dataItem = (HeaterNextActionIdleRowItem) item;
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                if (dataItem.start != null)
                    startTextView.setText(tf.format(dataItem.start));
                if (dataItem.end != null)
                    endTextView.setText(tf.format(dataItem.end));
                descriptionTextView.setText(dataItem.description);
            }
        }
    }

    public static class HeaterNextActionHeaderViewHolder extends ViewHolder {
        public void update(ListItem item) {
            HeaterNextActionHeaderItem dataItem = (HeaterNextActionHeaderItem)item;
            SimpleDateFormat df = new SimpleDateFormat("EEEEE dd-MM-yyyy");
            textView.setText(df.format(dataItem.date));
        }
    }

    public static class HeaterCommandLogRowViewHolder extends ViewHolder {

        public TextView targetTextView;
        public TextView dateTextView;
        public TextView durationTextView;
        public TextView commandTextView;

        public void update(ListItem item) {
            if (item instanceof HeaterCommandLogRowItem) {
                HeaterCommandLogRowItem dataItem = (HeaterCommandLogRowItem) item;
                String target = "" + dataItem.targetvalue + "°C";
                targetTextView.setText(target);

                SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                if (dataItem.date != null)
                    dateTextView.setText(tf.format(dataItem.date));
                //targetTextView.setText("" + dataItem.targetvalue);
                durationTextView.setText(""+dataItem.duration);
                commandTextView.setText(dataItem.command);
            }
        }
    }
}
