package com.webduino.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webduino.R;
import com.webduino.fragment.adapters.CardAdapter;
import com.webduino.fragment.cardinfo.CardInfo;
import com.webduino.fragment.cardinfo.WebduinoSystemCardInfo;
import com.webduino.scenarios.Scenario;
import com.webduino.webduinosystems.WebduinoSystem;
import com.webduino.webduinosystems.WebduinoSystems;

import java.util.ArrayList;
import java.util.List;

import static com.webduino.webduinosystems.WebduinoSystemFactory.HEATERSYSTEM;
import static com.webduino.webduinosystems.WebduinoSystemFactory.SECURITYSYSTEM;

public class WebduinoSystemsFragment extends Fragment implements WebduinoSystemFragment.OnWebduinoSystemFragmentListener {

    private List<CardInfo> list;
    private CardAdapter cardAdapter;

    WebduinoSystemFragment webduinoSystemFragment = null;

    @Override
    public void onWebduinoSystemRefresh() {
        for (WebduinoSystemsFragment.OnWebduinoSystemsFragmentListener listener: listeners) {
            listener.onWebduinoSystemsRefresh();
        }
    }

    public interface OnWebduinoSystemsFragmentListener {
        public void onWebduinoSystemsRefresh();
    }

    private List<OnWebduinoSystemsFragmentListener> listeners = new ArrayList<>();

    public void addListener(OnWebduinoSystemsFragmentListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
        }

        View v;
        v = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        recList.setLayoutManager(gridLayoutManager);

        cardAdapter = new CardAdapter(this, createWebduinoSystemList());
        recList.setAdapter(cardAdapter);
        cardAdapter.setListener(new CardAdapter.OnListener() {
            @Override
            public void onClick(int position, CardInfo cardInfo) {
                WebduinoSystemCardInfo webduinosystemCardInfordInfo = (WebduinoSystemCardInfo) cardInfo;
                showWebduinoSystemFragment(webduinosystemCardInfordInfo);
            }
        });

        return v;
    }

    public void update() {

        if (cardAdapter == null)
            return;

        list = createWebduinoSystemList();
        cardAdapter.swap(list);

        if (webduinoSystemFragment != null) {
            webduinoSystemFragment.update();
        }

    }

    public List<CardInfo> createWebduinoSystemList() {

        List<CardInfo> result = new ArrayList<CardInfo>();

        for (WebduinoSystem webduinoSystem : WebduinoSystems.list) {
            try {
                CardInfo ci = webduinoSystem.getCardInfo();
                result.add(ci);
            } catch (Exception e) {
            }
        }
        return result;
    }

    private void showWebduinoSystemFragment(WebduinoSystemCardInfo webduinoSystemCardInfo) {

        //WebduinoSystemFragment webduinoSystemFragment = null;

        Bundle bundle = new Bundle();
        bundle.putString("id", "" + webduinoSystemCardInfo.id);

        if (webduinoSystemCardInfo.webduionoSystemType.equals(HEATERSYSTEM)) {
            webduinoSystemFragment = new HeaterSystemFragment();
            webduinoSystemFragment.webduinoSystem = webduinoSystemCardInfo.webduionoSystem;

        } else if (webduinoSystemCardInfo.webduionoSystemType.equals(SECURITYSYSTEM)) {
            webduinoSystemFragment = new SecuritySystemFragment();
            webduinoSystemFragment.webduinoSystem = webduinoSystemCardInfo.webduionoSystem;
        } else {
            return;
        }

        webduinoSystemFragment.addListener(this);

        webduinoSystemFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, (Fragment) webduinoSystemFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
