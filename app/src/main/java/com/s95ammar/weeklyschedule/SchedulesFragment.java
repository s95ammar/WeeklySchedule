package com.s95ammar.weeklyschedule;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

public class SchedulesFragment extends Fragment {
    private static final String TAG = "SchedulesFragment";
    private ArrayList<ScheduleItem> mScheduleItems;
    private ScheduleCreator mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SchedulesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedules, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpActionButtonListener();
        buildRecyclerView();
        refreshBackgroundColor();
    }

    private void refreshBackgroundColor() {
        boolean isEmpty = mScheduleItems.size() == 0;
        getView().findViewById(R.id.textView_no_schedules).setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        getView().findViewById(R.id.recyclerView).setBackgroundColor(isEmpty ? Color.WHITE : getResources().getColor(R.color.colorLightGray));
    }

    private void buildRecyclerView() {
        if (mScheduleItems == null) {
            mScheduleItems = new ArrayList<>();
        }
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ScheduleAdapter(mScheduleItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void addSchedule(String name) {
        mScheduleItems.add(0, new ScheduleItem(name));
        mAdapter.notifyItemInserted(0);
        mLayoutManager.scrollToPosition(0);
        refreshBackgroundColor();
        Log.d(TAG, "addSchedule: " + mScheduleItems);

    }

    private void setUpActionButtonListener() {
        FloatingActionButton fab = getView().findViewById(R.id.button_add_schedule);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.showScheduleAddDialog();
            }
        });

    }

    private void setUpSwitchListener() { // TODO
        Switch sw = getView().findViewById(R.id.switch_is_active);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScheduleCreator) {
            mListener = (ScheduleCreator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScheduleCreator");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ScheduleCreator {
        void showScheduleAddDialog();
    }

}
