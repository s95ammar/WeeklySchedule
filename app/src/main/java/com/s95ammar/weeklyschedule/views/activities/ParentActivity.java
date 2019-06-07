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
import com.google.gson.GsonBuilder;
import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.interfaces.ScheduleViewer;
import com.s95ammar.weeklyschedule.models.CategoriesList;
import com.s95ammar.weeklyschedule.models.Category;
import com.s95ammar.weeklyschedule.models.Day;
import com.s95ammar.weeklyschedule.models.Event;
import com.s95ammar.weeklyschedule.models.LocalTimeSerializer;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.models.SchedulesList;
//import com.s95ammar.weeklyschedule.views.fragments.EventRefactorDialog;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment;

import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.TreeSet;

public class ParentActivity extends AppCompatActivity implements
        ScheduleViewerFragment.ScheduleEditor,
        ScheduleViewer
//        EventRefactorDialog.EventCreatorListener, TODO: replace dialog with activity
        /*TimePickerDialog.OnTimeSetListener*/ {

    private static final String TAG = "ParentActivity";
    protected static final int REQUEST_APPLY_CHANGES = 0;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_APPLY_CHANGES) {
            if(resultCode == RESULT_OK) {
                if (scheduleViewerFragment != null) {
                    scheduleViewerFragment.showSchedule();
                } else {
                    throw new RuntimeException("scheduleViewerFragment not instantiated");
                }
            }
        }
    }

    @Override
    public void openEventRefactorActivity(Event event, int scheduleIndex) {
        Intent intent = new Intent(this, EventRefactorActivity.class);
        intent.putExtra(KEY_EVENT, event);
        intent.putExtra(KEY_SCHEDULE_INDEX, scheduleIndex);
        startActivityForResult(intent, REQUEST_APPLY_CHANGES);
    }

    public void saveData() {
        Log.d(TAG, "saveData: Saving data...");
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeSerializer())
                .excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int activeScheduleIndex = SchedulesList.getInstance().indexOf(SchedulesList.getInstance().getActiveSchedule());
        String schedulesJson = gson.toJson(SchedulesList.getInstance());
        Log.d(TAG, "saveData: schedules list" + schedulesJson);
        Log.d(TAG, "saveData: active schedule index: " + activeScheduleIndex + " : " + SchedulesList.getInstance().getActiveSchedule());
        editor.putInt("active schedule index", activeScheduleIndex);
        editor.putString("schedules list", schedulesJson);
//        editor.putString("categories list", categoriesJson);
        editor.apply();
    }

    public void loadData() {
        Log.d(TAG, "loadData: Loading data...");
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        int activeScheduleIndex = sharedPreferences.getInt("active schedule index", -1);
        String schedulesJson = sharedPreferences.getString("schedules list", null);
        SchedulesList.createFromJson(schedulesJson);
        SchedulesList.getInstance().loadActiveSchedule(activeScheduleIndex);
        rebuildEventLinks();
        Log.d(TAG, "loadData: " + SchedulesList.getInstance());
    }

    private void rebuildEventLinks() {
        for (int i = 0; i < SchedulesList.getInstance().size(); i++) {
            ScheduleItem schedule = SchedulesList.getInstance().get(i);
            for (int j = 0; j < schedule.getDays().size(); j++) {
                Day day = schedule.getDays().get(j);
                for (int k = 0; k < day.getEvents().size(); k++) {
                    Event event = day.getEvents().get(k);
                    event.setDay(day);
                    if (!CategoriesList.getInstance().contains(event.getCategory()))
                        CategoriesList.getInstance().add(event.getCategory());
                    Category category = CategoriesList.getInstance().get(CategoriesList.getInstance().indexOf(event.getCategory()));
                    if (category.getCategoryEvents() == null)
                        category.setCategoryEvents(new TreeSet<>(new Event.EventNameComparator()));
                    category.getCategoryEvents().add(event);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }

}
