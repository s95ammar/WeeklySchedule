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
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleNamerDialog;
import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.views.fragments.SchedulesListFragment;

public class MainActivity extends ParentActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ScheduleNamerDialog.ScheduleNamerListener,
        SchedulesListFragment.SchedulesListManager,
        ScheduleViewer {

    private static final int REQUEST_CODE = 0;
    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    private SchedulesListFragment schedulesListFragment;


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                switchToFragment(schedulesListFragment != null ? schedulesListFragment : (schedulesListFragment = new SchedulesListFragment()),
                        R.id.fragment_container_main_activity, null);

            }
        }
    }


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
    public void showScheduleInActivity(int i) {
        Intent intent = new Intent(this, ScheduleViewerActivity.class);
        intent.putExtra(KEY_INDEX, i);
        startActivityForResult(intent, REQUEST_CODE);
    }

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
        values.putInt(KEY_INDEX, i);
        values.putString(KEY_NAME, name);
        dialog.setArguments(values);
        dialog.show(getSupportFragmentManager(), TAG);

    }

}
