package com.webduino.fragment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.webduino.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import static com.webduino.R.layout.meteoitemrowlayout;

/**
 * Created by giacomo on 19/07/2015.
 */
public interface HeaterListListener {

    public void onClickCheckBox(int position, boolean selected);

    public void onClick(long spotId);

    class HeaterListArrayAdapter extends ArrayAdapter<HeaterListItem> {

        private LayoutInflater mInflater;

        ArrayList<HeaterListItem> dataList = new ArrayList<>();

        private final Context context;
        HeaterListListener mListener;

        public HeaterListArrayAdapter(Context context, ArrayList<HeaterListItem> list, HeaterListListener listener) {

            super(context, meteoitemrowlayout, list);

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            //
            mListener = listener;
            this.context = context;

            this.dataList = list;
        }



        @Override
        public int getViewTypeCount() {
            return 2; //return 2, you have two types that the getView() method will return, normal(0) and for the last row(1)
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public HeaterListItem getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            HeaterListItem item = getItem(position);
            return item.type;
            //return (position == 0) ? 0 : 1; //if we are at the last position then return 1, for any other position return 0
        }

        //@Override
        public View getViewx(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            //int type = getItemViewType(position);

            HeaterListItem item = dataList.get(position);

            if (v == null) {
                //HeaterListItem item = getItem(position);
                // Inflate the layout according to the view type
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (item.type == 0) {
                    // Inflate the layout with image
                    v = inflater.inflate(R.layout.meteoitemheaderrowlayout, parent, false);
                    TextView descriptioTextView = (TextView) v.findViewById(R.id.descriptionTextView);
                    descriptioTextView.setText(item.description);
                }
                else {
                    v = inflater.inflate(R.layout.meteoitemrowlayout, parent, false);
                    TextView descriptioTextView = (TextView) v.findViewById(R.id.descriptionTextView);
                    descriptioTextView.setText(item.description);
                    TextView valueTextView = (TextView) v.findViewById(R.id.valueTextView);
                    valueTextView.setText(item.value);
                    TextView commentTextView = (TextView) v.findViewById(R.id.commentTextView);
                    //DateFormat df = new SimpleDateFormat("ddMMyyyyHHmm");
                    if (item.date != null) {
                        DateFormat df = new SimpleDateFormat("HH:mm");
                        commentTextView.setText(df.format(item.date));
                    } else {
                        commentTextView.setText("");
                    }
                }
            }
            //
            return v;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("getView " + position + " " + convertView);
            ViewHolder holder = null;
            if (convertView == null) {

                if (dataList.get(position).type == 0) {
                    convertView = mInflater.inflate(R.layout.meteoitemheaderrowlayout, null);
                    holder = new HeaderViewHolder();
                    holder.textView = (TextView)convertView.findViewById(R.id.descriptionTextView);
                } else {
                    convertView = mInflater.inflate(R.layout.meteoitemrowlayout, null);
                    holder = new RowViewHolder();
                    RowViewHolder rvHolder = (RowViewHolder) holder;
                    rvHolder.textView = (TextView)convertView.findViewById(R.id.descriptionTextView);
                    rvHolder.valueTextView = (TextView) convertView.findViewById(R.id.valueTextView);
                    rvHolder.commentTextView = (TextView) convertView.findViewById(R.id.commentTextView);
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


        public void update(HeaterListItem item) {
            textView.setText(item.description);

        }
    }

    public static class HeaderViewHolder extends ViewHolder {
        public void update(HeaterListItem item) {
            textView.setText(item.description);
        }
    }

    public static class RowViewHolder extends ViewHolder {

        public TextView valueTextView;
        public TextView commentTextView;

        public void update(HeaterListItem item) {
            textView.setText(item.description);
            valueTextView.setText(item.value);
            commentTextView.setText("");
        }
    }
}
