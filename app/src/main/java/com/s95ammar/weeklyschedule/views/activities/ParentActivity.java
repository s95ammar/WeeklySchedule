package com.s95ammar.weeklyschedule.views.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.interfaces.ScheduleViewer;
import com.s95ammar.weeklyschedule.models.CategoriesList;
import com.s95ammar.weeklyschedule.models.Event;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.models.SchedulesList;
//import com.s95ammar.weeklyschedule.views.fragments.EventRefactorDialog;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment;

import java.util.Calendar;

public class ParentActivity extends AppCompatActivity implements
        ScheduleViewerFragment.ScheduleEditor,
        ScheduleViewer
//        EventRefactorDialog.EventCreatorListener, TODO: replace dialog with activity
        /*TimePickerDialog.OnTimeSetListener*/ {

    private static final String TAG = "ParentActivity";
    protected ScheduleViewerFragment scheduleViewerFragment;
    protected Menu menu;
//    protected EventRefactorDialog eventRefactorDialog; TODO: replace dialog with activity

    protected void switchToFragment(@NonNull Fragment fragment, int containerId, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction().replace(containerId, fragment).commit();
    }

    public void editListener(MenuItem item) {
        setEditVisibility(false);
        setDoneCancelVisibility(true);
        scheduleViewerFragment.setMode(ScheduleViewerFragment.EDIT);
    }

    public void doneListener(MenuItem item) {
        setDoneCancelVisibility(false);
        setEditVisibility(true);
        scheduleViewerFragment.setMode(ScheduleViewerFragment.VIEW);
//        TODO: apply changes
    }


    @Override
    public void setEditVisibility(boolean visibility) {
        menu.findItem(R.id.button_edit).setVisible(visibility);
    }

    @Override
    public void setDoneCancelVisibility(boolean visibility) {
        menu.findItem(R.id.button_done).setVisible(visibility);
    }

    @Override
    public void openScheduleViewerFragment(ScheduleItem schedule, int fragContainerId) {
        Bundle scheduleBundle = new Bundle();
        scheduleBundle.putSerializable(KEY_SCHEDULE, schedule);
        switchToFragment(scheduleViewerFragment = new ScheduleViewerFragment(),
                fragContainerId, scheduleBundle);

    }

    @Override
    public void openEventRefactorActivity(Event event) {
        Intent intent = new Intent(this, EventRefactorActivity.class);
        intent.putExtra(KEY_EVENT, event);
        startActivity(intent);
    }

//    TODO: replace dialog with activity
/*
    @Override
    public void createEvent(String day, String name, String startTime, String endTime) {
        Log.d(TAG, "createEvent: " + day + ", " + name + ", " + startTime + ", " + endTime);
    }
*/


    public void saveData() {
        Log.d(TAG, "saveData: Saving data");
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int activeScheduleIndex = SchedulesList.getInstance().indexOf(SchedulesList.getInstance().getActiveSchedule());
        String schedulesJson = new Gson().toJson(SchedulesList.getInstance());
        String categoriesJson = new Gson().toJson(CategoriesList.getInstance());
        Log.d(TAG, "saveData: categories list" + categoriesJson);
        Log.d(TAG, "saveData: schedules list" + schedulesJson);
        Log.d(TAG, "saveData: active schedule index: " + activeScheduleIndex + " : " + SchedulesList.getInstance().getActiveSchedule());
        editor.putInt("active schedule index", activeScheduleIndex);
        editor.putString("schedules list", schedulesJson);
        editor.putString("categories list", categoriesJson);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        int activeScheduleIndex = sharedPreferences.getInt("active schedule index", -1);
        String schedulesJson = sharedPreferences.getString("schedules list", null);
        String categoriesJson = sharedPreferences.getString("categories list", null);
        SchedulesList.createFromJson(schedulesJson);
        CategoriesList.createFromJson(categoriesJson);
        SchedulesList.getInstance().loadActiveSchedule(activeScheduleIndex);
    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }


}
