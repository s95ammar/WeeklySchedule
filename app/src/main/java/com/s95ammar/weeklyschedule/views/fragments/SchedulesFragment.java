package com.s95ammar.weeklyschedule.views.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.recyclerView.ScheduleAdapter;
import com.s95ammar.weeklyschedule.models.ScheduleItem;

public class SchedulesFragment extends Fragment {
    private static final String TAG = "SchedulesFragment";
    private ScheduleManager mListener;
    private RecyclerView mRecyclerView;
    private ScheduleAdapter mAdapter;
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
        refreshBackground();
        getActivity().setTitle(R.string.title_schedules);
    }



    private void refreshBackground() {
        boolean isEmpty = SchedulesList.getInstance().size() == 0;
        getView().findViewById(R.id.textView_no_schedules).setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        getView().findViewById(R.id.recyclerView).setBackgroundColor(isEmpty ? Color.WHITE : getResources().getColor(R.color.colorLightGray));
    }

    private void buildRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ScheduleAdapter(SchedulesList.getInstance());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickedListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int i) {
                SchedulesList.getInstance().get(i).showSchedule();
                Log.d(TAG, "onItemClicked: " + i);
            }

            @Override
            public void onMoreClicked(final int i, Button buttonMore) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), buttonMore);
                popupMenu.getMenuInflater().inflate(R.menu.schedule_item_more, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.rename) {
                            mListener.showScheduleRefactorDialog(SchedulesList.getInstance().get(i).getName(), i); // onOk -> renameSchedule(newName, i)
                        } else /*if (menuItem.getItemId() == R.id.delete)*/ {
                            deleteSchedule(i);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    public void addSchedule(String name) {
        SchedulesList.getInstance().add(0, new ScheduleItem(name));
        mAdapter.notifyItemInserted(0);
        mLayoutManager.scrollToPosition(0);
        refreshBackground();
        Log.d(TAG, "addSchedule: " + SchedulesList.getInstance());
    }

    public void deleteSchedule(int i) {
        SchedulesList.getInstance().remove(i);
        mAdapter.notifyItemRemoved(i);
        refreshBackground();
        Log.d(TAG, "deleteSchedule: " + SchedulesList.getInstance());
    }

    public void renameSchedule(String newName, int i) {
        SchedulesList.getInstance().get(i).setName(newName);
        mAdapter.notifyItemChanged(i);
        refreshBackground();
        Log.d(TAG, "renameSchedule: " + SchedulesList.getInstance());
    }

    private void setUpActionButtonListener() {
        FloatingActionButton fab = getView().findViewById(R.id.button_add_schedule);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.showScheduleRefactorDialog(null, ScheduleNamerDialog.Action.ADD);  // onOk -> addSchedule(name)
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
        if (context instanceof ScheduleManager) {
            mListener = (ScheduleManager) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScheduleManager");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ScheduleManager {
        void showScheduleRefactorDialog(String action, int i);
        void saveData();
        void loadData();
    }

}
