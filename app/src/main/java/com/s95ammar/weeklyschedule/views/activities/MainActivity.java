package com.s95ammar.weeklyschedule.views.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.s95ammar.weeklyschedule.models.Category;
import com.s95ammar.weeklyschedule.models.Schedule;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.fragments.CategoriesListFragment;
import com.s95ammar.weeklyschedule.views.fragments.CategoryRefactorDialog;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleNamerDialog;
import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment;
import com.s95ammar.weeklyschedule.views.fragments.SchedulesListFragment;

import yuku.ambilwarna.AmbilWarnaDialog;


public class MainActivity extends AbstractScheduleActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        SchedulesListFragment.SchedulesListManager,
        ScheduleNamerDialog.ScheduleNamerListener,
        CategoriesListFragment.CategoriesListManager,
        CategoryRefactorDialog.CategoryRefactor {

    private static final int REQUEST_BACK_TO_SCHEDULES = 1;
    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    private SchedulesListFragment schedulesListFragment;
    private CategoriesListFragment categoriesListFragment;



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
        setTimePattern();
    }

    private void setTimePattern() {
        Schedule.timePattern = DateFormat.is24HourFormat(this) ? "HH:mm" : "hh:mm aa";
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
            openScheduleViewerFragment(SchedulesList.getInstance().getActiveSchedule(), ScheduleViewerFragment.VIEW, R.id.fragment_container_main_activity);
        } else {
            Log.d(TAG, "checkActiveSchedule: Active schedule not found");
            switchToFragment(schedulesListFragment != null ? schedulesListFragment : (schedulesListFragment = new SchedulesListFragment()),
                    R.id.fragment_container_main_activity, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BACK_TO_SCHEDULES) {
            if(resultCode == RESULT_OK) {
                switchToFragment(schedulesListFragment != null ? schedulesListFragment : (schedulesListFragment = new SchedulesListFragment()),
                        R.id.fragment_container_main_activity, null);
            }
        } else if (requestCode == REQUEST_APPLY_CHANGES) {
            if (resultCode == RESULT_OK) {
                applyEventChanges(data.getIntExtra(KEY_SCHEDULE_INDEX, -1), R.id.fragment_container_main_activity);
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
                openScheduleViewerFragment(SchedulesList.getInstance().getActiveSchedule(), ScheduleViewerFragment.VIEW, R.id.fragment_container_main_activity);
                break;
            case R.id.nav_schedules:
                switchToFragment(schedulesListFragment != null ? schedulesListFragment : (schedulesListFragment = new SchedulesListFragment()),
                        R.id.fragment_container_main_activity, null);
                break;
            case R.id.nav_categories:
                switchToFragment(categoriesListFragment != null ? categoriesListFragment : (categoriesListFragment = new CategoriesListFragment()),
                        R.id.fragment_container_main_activity, null);
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
        intent.putExtra(SchedulesListFragment.SchedulesListManager.KEY_INDEX, i);
        startActivityForResult(intent, REQUEST_BACK_TO_SCHEDULES);
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
        values.putInt(SchedulesListFragment.SchedulesListManager.KEY_INDEX, i);
        values.putString(SchedulesListFragment.SchedulesListManager.KEY_NAME, name);
        dialog.setArguments(values);
        dialog.show(getSupportFragmentManager(), TAG);
    }

    @Override
    public void showCategoryRefactorDialog(Category category, int i) {
        CategoryRefactorDialog dialog = new CategoryRefactorDialog();
        Bundle values = new Bundle();
        values.putInt(CategoriesListFragment.CategoriesListManager.KEY_INDEX, i);
        values.putSerializable(CategoriesListFragment.CategoriesListManager.KEY_CATEGORY, category);
        dialog.setArguments(values);
        dialog.show(getSupportFragmentManager(), TAG);
    }

    @Override
    public void applyCategory(Category category, int i) {
        switch (i) {
            case CategoryRefactorDialog.Action.ADD:
                categoriesListFragment.addCategory(category);
                break;
            default:
                categoriesListFragment.editCategory(category, i);
        }
    }

    @Override
    public void openColorPicker(final CategoryRefactorDialog categoryRefactorDialog, final int viewId, final int defaultColor) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_ID, viewId);
                bundle.putInt(KEY_COLOR, color);
                categoryRefactorDialog.setArguments(bundle);
                categoryRefactorDialog.receiveColor();
            }
        });
        colorPicker.show();
    }

}
