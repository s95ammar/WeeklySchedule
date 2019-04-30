package com.s95ammar.weeklyschedule;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DialogAdd.DialogListener, SchedulesFragment.ScheduleCreator {
    private DrawerLayout drawer;

    private CurrentScheduleFragment currentScheduleFragment;
    private SchedulesFragment schedulesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpNavDrawer(toolbar);
    }

    private void setUpNavDrawer(Toolbar toolbar) {
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
                if (currentScheduleFragment == null) {
                    currentScheduleFragment = new CurrentScheduleFragment();
                }
                switchToFragment(currentScheduleFragment = new CurrentScheduleFragment());
                break;
            case R.id.nav_schedules:
                if (schedulesFragment == null) {
                    schedulesFragment = new SchedulesFragment();
                }
                switchToFragment(schedulesFragment);
                break;
            case R.id.nav_settings:
//              TODO: Implement
                break;
            case R.id.nav_info:
//              TODO: Implement
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchToFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container), fragment).commit();

    }

    @Override
    public void applyName(String name) {
        schedulesFragment.addSchedule(name);
    }

    @Override
    public void showScheduleAddDialog() {
        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add schedule");

    }
}
