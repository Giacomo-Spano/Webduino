package com.webduino.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.webduino.R;
import com.webduino.fragment.adapters.HeaterListItem;
import com.webduino.fragment.adapters.HeaterListListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giaco on 07/01/2018.
 */

public class PageFragment extends Fragment {
    //private static final String ARG_PAGE_NUMBER = "page_number";

    public boolean adaptercreated = false;
    protected ArrayList<HeaterListItem> list = new ArrayList<>();
    protected ListView listView;

    public PageFragment() {
    }

    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        /* Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);*/
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        //String strtext = getArguments().getString("shieldid");
        //shieldId = Integer.valueOf(strtext);
        //strtext = getArguments().getString("id");
        //actuatorId = Integer.valueOf(strtext);
        //refreshData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_layout, container, false);

        /*TextView txt = (TextView) rootView.findViewById(R.id.page_number_label);
        int page = getArguments().getInt(ARG_PAGE_NUMBER, -1);
        txt.setText(String.format("Page %d", page));*/

        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setDivider(null);
        listView.setDividerHeight(0);


        return rootView;
    }

    public interface OnMeteoItemListListener {
        void onSpotListChangeSelection(List<Long> list);
    }

    protected HeaterListListener listener;

    public void setListener(HeaterListListener listener) {
        this.listener = listener;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyAdapter();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //getListView().setDivider(null);
        //getListView().setDividerHeight(0);
        createAdapter();
        refreshData();
    }

    public void createAdapter() {
        adaptercreated = true;
    }

    public void destroyAdapter() {
        adaptercreated = false;
    }


    public void refreshData() {

    }

}
