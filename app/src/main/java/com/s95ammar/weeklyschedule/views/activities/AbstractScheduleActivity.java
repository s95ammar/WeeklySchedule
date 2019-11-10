package com.s95ammar.weeklyschedule.views.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.CategoriesList;
import com.s95ammar.weeklyschedule.models.Category;
import com.s95ammar.weeklyschedule.models.Day;
import com.s95ammar.weeklyschedule.models.Event;
import com.s95ammar.weeklyschedule.models.LocalTimeSerializer;
import com.s95ammar.weeklyschedule.models.Schedule;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment;

import org.joda.time.LocalTime;

import java.util.HashSet;

public abstract class AbstractScheduleActivity extends AppCompatActivity implements
        ScheduleViewerFragment.ScheduleEditor {

    private static final String TAG = "AbstractActivity";
    private static final String SHARED_PREFERENCES = "shared preferences";
    private static final String KEY_SCHEDULES_LIST = "schedules list";
    private static final String KEY_ACTIVE_SCHEDULE_INDEX = "active schedule index";
    private static final String KEY_CATEGORIES_LIST = "categories list";
    protected static final int REQUEST_APPLY_CHANGES = 0;
    protected ScheduleViewerFragment scheduleViewerFragment;
    protected Menu menu;

    protected void switchToFragment(@NonNull Fragment fragment, int containerId, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction().replace(containerId, fragment).commit();
    }

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
    public void openScheduleViewerFragment(Schedule schedule, int mode, int fragContainerId) {
        Bundle scheduleBundle = new Bundle();
        scheduleBundle.putSerializable(KEY_SCHEDULE, schedule);
        scheduleBundle.putInt(ScheduleViewerFragment.ScheduleEditor.KEY_MODE, mode);
        switchToFragment(scheduleViewerFragment = new ScheduleViewerFragment(),
                fragContainerId, scheduleBundle);

    }

    @Override
    public void startEventRefactorActivity(Event event, int scheduleIndex) {
        Intent intent = new Intent(this, EventRefactorActivity.class);
        intent.putExtra(KEY_EVENT, event);
        intent.putExtra(KEY_SCHEDULE_INDEX, scheduleIndex);
        startActivityForResult(intent, REQUEST_APPLY_CHANGES);
    }

    protected void applyEventChanges(int editedScheduleIndex, int fragmentContainerId) {
        if (editedScheduleIndex != -1) {
            Schedule editedSchedule = SchedulesList.getInstance().get(editedScheduleIndex);
            openScheduleViewerFragment(editedSchedule, ScheduleViewerFragment.EDIT, fragmentContainerId);
        }
    }

    public void saveData() {
        Log.d(TAG, "saveData: Saving data...");
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(LocalTime.class, new LocalTimeSerializer())
                .excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String schedulesJson = gson.toJson(SchedulesList.getInstance());
        int activeScheduleIndex = SchedulesList.getInstance().indexOf(SchedulesList.getInstance().getActiveSchedule());
        String categoriesJson = gson.toJson(CategoriesList.getInstance());
        Log.d(TAG, "saveData: schedules list" + schedulesJson);
        Log.d(TAG, "saveData: active schedule index: " + activeScheduleIndex + " : " + SchedulesList.getInstance().getActiveSchedule());
        Log.d(TAG, "saveData: categories list" + categoriesJson);
        editor.putString(KEY_SCHEDULES_LIST, schedulesJson);
        editor.putInt(KEY_ACTIVE_SCHEDULE_INDEX, activeScheduleIndex);
        editor.putString(KEY_CATEGORIES_LIST, categoriesJson);
        editor.apply();
    }


    public void loadData() {
        Log.d(TAG, "loadData: Loading data...");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String schedulesJson = sharedPreferences.getString(KEY_SCHEDULES_LIST, null);
        int activeScheduleIndex = sharedPreferences.getInt(KEY_ACTIVE_SCHEDULE_INDEX, -1);
        String categoriesJson = sharedPreferences.getString(KEY_CATEGORIES_LIST, null);
        SchedulesList.createFromJson(schedulesJson);
        SchedulesList.getInstance().loadActiveSchedule(activeScheduleIndex);
        CategoriesList.createFromJson(categoriesJson);
        rebuildEventLinks();
        Log.d(TAG, "loadData: " + SchedulesList.getInstance());
        Log.d(TAG, "loadData: " + CategoriesList.getInstance());
    }

    private void rebuildEventLinks() {
        for (int i = 0; i < CategoriesList.getInstance().size(); i++) {
            Category category = CategoriesList.getInstance().get(i);
            if (category.getCategoryEvents() == null) {
                category.setCategoryEvents(new HashSet<>());
            }
        }

        for (int i = 0; i < SchedulesList.getInstance().size(); i++) {
            Schedule schedule = SchedulesList.getInstance().get(i);
            for (int j = 0; j < schedule.getDays().size(); j++) {
                Day day = schedule.getDays().get(j);
                for (int k = 0; k < day.getEvents().size(); k++) {
                    Event event = day.getEvents().get(k);
                    event.setDay(day);
                    int categoryIndex = CategoriesList.getInstance().indexOf(event.getCategory());
                    if (categoryIndex != -1) {
                        Category category = CategoriesList.getInstance().get(categoryIndex);
                        category.getCategoryEvents().add(event);
                        event.setCategory(category);
                    }
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