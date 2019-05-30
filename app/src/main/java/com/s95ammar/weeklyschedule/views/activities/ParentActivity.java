package com.s95ammar.weeklyschedule.views.activities;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.interfaces.ScheduleViewer;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment;

public class ParentActivity extends AppCompatActivity implements
        ScheduleViewerFragment.ScheduleEditor,
        ScheduleViewer {
    private static final String TAG = "ParentActivity";
    protected ScheduleViewerFragment scheduleViewerFragment;
    protected Menu menu;

    public void editListener(MenuItem item) {
        setEditVisibility(false);
        setDoneVisibility(true);
        scheduleViewerFragment.setMode(ScheduleViewerFragment.EDIT);
    }

    public void doneListener(MenuItem item) {
        setDoneVisibility(false);
        setEditVisibility(true);
        scheduleViewerFragment.setMode(ScheduleViewerFragment.VIEW);
    }

    @Override
    public void setEditVisibility(boolean visibility) {
        menu.findItem(R.id.button_edit).setVisible(visibility);
    }

    @Override
    public void setDoneVisibility(boolean visibility) {
        menu.findItem(R.id.button_done).setVisible(visibility);
    }

    @Override
    public void openScheduleViewerFragment(ScheduleItem schedule, int fragContainerId) {
        Bundle scheduleBundle = new Bundle();
        scheduleBundle.putSerializable(KEY_SHCEDULE, schedule);
        switchToFragment(scheduleViewerFragment != null ? scheduleViewerFragment : (scheduleViewerFragment = new ScheduleViewerFragment()),
                fragContainerId, scheduleBundle);

    }

    protected void switchToFragment(@NonNull Fragment fragment, int containerId, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction().replace(containerId, fragment).commit();
    }

    public void saveData() {
        Log.d(TAG, "saveData: Saving data");
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int activeScheduleIndex = SchedulesList.getInstance().indexOf(SchedulesList.getInstance().getActiveSchedule());
        String json = new Gson().toJson(SchedulesList.getInstance());
        Log.d(TAG, "saveData: active schedule index: " + activeScheduleIndex + " : " + SchedulesList.getInstance().getActiveSchedule());
        Log.d(TAG, "saveData: schedules list" + json);
        editor.putInt("active schedule index", activeScheduleIndex);
        editor.putString("schedules list", json);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        int activeScheduleIndex = sharedPreferences.getInt("active schedule index", -1);
        String jsonList = sharedPreferences.getString("schedules list", null);
        SchedulesList.createFromJson(jsonList);
        SchedulesList.getInstance().loadActiveSchedule(activeScheduleIndex);
    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }

}
