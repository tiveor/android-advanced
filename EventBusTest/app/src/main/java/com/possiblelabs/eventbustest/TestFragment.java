package com.possiblelabs.eventbustest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Alvaro Orellana on 6/14/16.
 */
public class TestFragment extends Fragment {

    private TextView txtFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        txtFragment = (TextView) view.findViewById(R.id.txt_fragment);
        return view;
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onHiEvent(HiEvent event) {
        txtFragment.setText(event.getMessage());
    }

}
