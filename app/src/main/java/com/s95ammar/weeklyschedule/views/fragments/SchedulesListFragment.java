package com.s95ammar.weeklyschedule.views.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Day;
import com.s95ammar.weeklyschedule.models.Event;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.adapters.ScheduleRecViewAdapter;
import com.s95ammar.weeklyschedule.models.ScheduleItem;

import java.util.ArrayList;

public class SchedulesListFragment extends Fragment implements ScheduleRecViewAdapter.OnItemClickListener{
    private static final String TAG = "SchedulesListFragment";
    private SchedulesListManager mListener;
    private RecyclerView mRecyclerView;
    private ScheduleRecViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SchedulesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedules_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpActionButtonListener();
        buildRecyclerView();
        refreshLayout();
        getActivity().setTitle(R.string.title_schedules);
    }



    private void refreshLayout() {
        getView().findViewById(R.id.textView_no_schedules).setVisibility(SchedulesList.getInstance().isEmpty() ? View.VISIBLE : View.GONE);
        getView().findViewById(R.id.recyclerView_schedules).setBackgroundColor(SchedulesList.getInstance().isEmpty() ? Color.WHITE : getResources().getColor(R.color.colorLightGray));
    }

    private void buildRecyclerView() {
        mRecyclerView = getView().findViewById(R.id.recyclerView_schedules);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ScheduleRecViewAdapter(SchedulesList.getInstance());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickedListener(this);

    }

    @Override
    public void onItemClicked(int i) {
        mListener.showScheduleInActivity(i);
        Log.d(TAG, "onItemClicked: " + SchedulesList.getInstance().get(i).getName());
    }

    @Override
    public void onMoreClicked(final int i, Button buttonMore) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), buttonMore);
        popupMenu.inflate(R.menu.schedules_more_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.schedules_more_rename:
                        mListener.showScheduleRefactorDialog(SchedulesList.getInstance().get(i).getName(), i); // onOk -> editCategory(newName, i)
                        break;
                    case R.id.schedules_more_delete:
                        deleteSchedule(i);
                        break;
                }
                return true;
            }
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(getActivity(), (MenuBuilder) popupMenu.getMenu(), buttonMore);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();
    }

    @Override
    public void onSwitchChecked(int i, boolean isChecked) {
        final int exActiveIndex = SchedulesList.getInstance().indexOf(SchedulesList.getInstance().getActiveSchedule());
        if (isChecked) {
            if (SchedulesList.getInstance().getActiveSchedule() != null) {
                SchedulesList.getInstance().get(exActiveIndex).setActive(false);
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyItemChanged(exActiveIndex);
                    }
                });
            }
            SchedulesList.getInstance().get(i).setActive(true);
            SchedulesList.getInstance().setActiveSchedule(SchedulesList.getInstance().get(i));

        } else {
            if (i == exActiveIndex) {
                SchedulesList.getInstance().get(i).setActive(false);
                SchedulesList.getInstance().setActiveSchedule(null);
            }
        }
        Log.d(TAG, "onSwitchChecked: changed: " + SchedulesList.getInstance().get(i).getName() + " -> " + isChecked);
        Log.d(TAG, "onSwitchChecked: " + SchedulesList.getInstance());
        Log.d(TAG, "onSwitchChecked: Active schedule : " + SchedulesList.getInstance().getActiveSchedule());
    }

    public void addSchedule(String name) {
        ArrayList<Day> days = new ArrayList<>();
        for (int i = 0; i < ScheduleItem.WEEK_DAYS.length; i++) {
            days.add(new Day(ScheduleItem.WEEK_DAYS[i], new ArrayList<Event>()));
        }
        SchedulesList.getInstance().add(0, new ScheduleItem(name, days));
        mAdapter.notifyItemInserted(0);
        mLayoutManager.scrollToPosition(0);
        refreshLayout();
        Log.d(TAG, "addSchedule: " + SchedulesList.getInstance());
    }

    public void renameSchedule(String newName, int i) {
        SchedulesList.getInstance().get(i).setName(newName);
        mAdapter.notifyItemChanged(i);
        Log.d(TAG, "editCategory: " + SchedulesList.getInstance());
    }

    public void deleteSchedule(int i) {
        if (SchedulesList.getInstance().get(i).isActive()) {
            SchedulesList.getInstance().setActiveSchedule(null);
        }
        SchedulesList.getInstance().remove(i);
        mAdapter.notifyItemRemoved(i);
        refreshLayout();
        Log.d(TAG, "deleteCategory: " + SchedulesList.getInstance());
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SchedulesListManager) {
            mListener = (SchedulesListManager) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CategoriesListManager");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SchedulesListManager {
        void showScheduleRefactorDialog(String name, int i);
        void showScheduleInActivity(int i);
        String KEY_NAME = "name";
        String KEY_INDEX = "index";
    }

}
