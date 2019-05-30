package com.s95ammar.weeklyschedule.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.models.SchedulesList;

import java.io.Serializable;

import static com.s95ammar.weeklyschedule.views.fragments.SchedulesListFragment.SchedulesListManager.KEY_INDEX;

public class ScheduleViewerActivity extends ParentActivity {
    private static final String TAG = "ScheduleViewerActivity";
//    protected ScheduleViewerFragment scheduleViewerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar_schedule_viewer_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.menu = menu;
        showSchedule();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK, new Intent());
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void showSchedule() {
        ScheduleItem schedule = SchedulesList.getInstance().get(getIntent().getIntExtra(KEY_INDEX, -1));
        Serializable object = getIntent().getSerializableExtra(KEY_SCHEDULE);
        openScheduleViewerFragment(schedule, R.id.fragment_container_schedule_viewer_activity);
    }

}
