package com.s95ammar.weeklyschedule.views.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.fragments.ActiveScheduleFragment;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleNamerDialog;
import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.views.fragments.SchedulesFragment;

public class  MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ScheduleNamerDialog.DialogListener,
        SchedulesFragment.ScheduleManager {

    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    private ActiveScheduleFragment activeScheduleFragment;
    private SchedulesFragment schedulesFragment;

    private interface NavDrawerItems {
        int ACTIVE_SCHEDULE = 0;
        int SCHEDULES = 1;
        int SETTINGS = 2;
        int INFO = 3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkActiveSchedule();
        setUpNavDrawer(toolbar);
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

    private void checkActiveSchedule() {
        if (SchedulesList.getInstance().getActiveSchedule() != null) {
            Log.d(TAG, "checkActiveSchedule: Active schedule found " + SchedulesList.getInstance().getActiveSchedule());
            switchToFragment(activeScheduleFragment != null ? activeScheduleFragment : (activeScheduleFragment = new ActiveScheduleFragment()));
        } else {
            Log.d(TAG, "checkActiveSchedule: Active schedule not found");
            switchToFragment(schedulesFragment != null ? schedulesFragment : (schedulesFragment = new SchedulesFragment()));
        }
    }

    private void setUpNavDrawer(Toolbar toolbar) {
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (activeScheduleFragment != null && activeScheduleFragment.getUserVisibleHint()) {
            navigationView.getMenu().getItem(NavDrawerItems.ACTIVE_SCHEDULE).setChecked(true);
        } else if (schedulesFragment != null && schedulesFragment.getUserVisibleHint()) {
            navigationView.getMenu().getItem(NavDrawerItems.SCHEDULES).setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_current_schedule:
                switchToFragment(activeScheduleFragment != null ? activeScheduleFragment : (activeScheduleFragment = new ActiveScheduleFragment()));
                break;
            case R.id.nav_schedules:
                switchToFragment(schedulesFragment != null ? schedulesFragment : (schedulesFragment = new SchedulesFragment()));
                break;
            case R.id.nav_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
            case R.id.nav_info:
                Intent intentInfo = new Intent(this, InfoActivity.class);
                startActivity(intentInfo);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchToFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container), fragment).commit();

    }

    @Override
    public void applyName(String name, int i) {
        switch (i) {
            case ScheduleNamerDialog.Action.ADD:
                schedulesFragment.addSchedule(name);
                break;
            default:
                schedulesFragment.renameSchedule(name, i);
        }

    }

    @Override
    public void showScheduleRefactorDialog(String name, int i) {
        ScheduleNamerDialog dialog = new ScheduleNamerDialog();
        Bundle values = new Bundle();
        values.putString("name", name);
        values.putInt("index", i);
        dialog.setArguments(values);
        dialog.show(getSupportFragmentManager(), TAG);

    }

    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }
}
