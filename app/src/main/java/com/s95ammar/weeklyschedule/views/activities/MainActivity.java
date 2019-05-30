package com.s95ammar.weeklyschedule.views.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.s95ammar.weeklyschedule.interfaces.ScheduleViewer;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleNamerDialog;
import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.views.fragments.SchedulesListFragment;

public class MainActivity extends ParentActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ScheduleNamerDialog.DialogListener,
        SchedulesListFragment.SchedulesListManager,
        ScheduleViewer {

    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
//    private ScheduleViewerFragment scheduleViewerFragment;
    private SchedulesListFragment schedulesListFragment;
//    private Menu menu;


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
        Toolbar toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.menu = menu;
        Toolbar toolbar = findViewById(R.id.toolbar_main_activity);
        checkActiveSchedule();
        setUpNavDrawer(toolbar);
        return true;
    }

/*
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
*/

/*
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
*/

    private void checkActiveSchedule() {
        if (SchedulesList.getInstance().hasActiveSchedule()) {
            Log.d(TAG, "checkActiveSchedule: Active schedule found " + SchedulesList.getInstance().getActiveSchedule());
            openScheduleViewerFragment(SchedulesList.getInstance().getActiveSchedule(), R.id.fragment_container_main_activity);
        } else {
            Log.d(TAG, "checkActiveSchedule: Active schedule not found");
            switchToFragment(schedulesListFragment != null ? schedulesListFragment : (schedulesListFragment = new SchedulesListFragment()),
                    R.id.fragment_container_main_activity, null);
        }
    }

/*
    @Override
    public void openScheduleViewerFragment(ScheduleItem schedule) {
        Bundle scheduleBundle = new Bundle();
        scheduleBundle.putSerializable("schedule", schedule);
        switchToFragment(scheduleViewerFragment != null ? scheduleViewerFragment : (scheduleViewerFragment = new ScheduleViewerFragment()),
                R.id.fragment_container_main_activity, scheduleBundle);
    }
*/

    private void setUpNavDrawer(Toolbar toolbar) {
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Log.d(TAG, "setUpNavDrawer: ");
        if (scheduleViewerFragment == null) Log.d(TAG, "setUpNavDrawer: null");
        if (scheduleViewerFragment != null && scheduleViewerFragment.getUserVisibleHint()) {
            navigationView.getMenu().getItem(NavDrawerItems.ACTIVE_SCHEDULE).setChecked(true);
        } else if (schedulesListFragment != null && schedulesListFragment.getUserVisibleHint()) {
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
            case R.id.nav_active_schedule:
                openScheduleViewerFragment(SchedulesList.getInstance().getActiveSchedule(), R.id.fragment_container_main_activity);
                break;
            case R.id.nav_schedules:
                switchToFragment(schedulesListFragment != null ? schedulesListFragment : (schedulesListFragment = new SchedulesListFragment()),
                        R.id.fragment_container_main_activity, null);
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

    @Override
    public void showScheduleInActivity(ScheduleItem schedule) {
        Intent intent = new Intent(this, ScheduleViewerActivity.class);
        intent.putExtra("schedule", schedule);
        startActivity(intent);
    }

    /*
    private void switchToFragment(@NonNull Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container_main_activity), fragment).commit();
    }
*/

    @Override
    public void applyName(String name, int i) {
        switch (i) {
            case ScheduleNamerDialog.Action.ADD:
                schedulesListFragment.addSchedule(name);
                break;
            default:
                schedulesListFragment.renameSchedule(name, i);
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

/*
    @Override
    protected void onStop() {
        saveData();
        super.onStop();
    }
*/
}
