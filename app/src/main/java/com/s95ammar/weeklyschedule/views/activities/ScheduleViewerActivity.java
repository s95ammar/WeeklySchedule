package com.s95ammar.weeklyschedule.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.Schedule;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment;

import static com.s95ammar.weeklyschedule.views.fragments.SchedulesListFragment.SchedulesListManager.KEY_INDEX;

public class ScheduleViewerActivity extends AbstractScheduleActivity {
    private static final String TAG = "ScheduleViewerActivity";

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
        showScheduleViewerFragment();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_APPLY_CHANGES) {
            if (resultCode == RESULT_OK) {
                applyEventChanges(data.getIntExtra(KEY_SCHEDULE_INDEX, -1), R.id.fragment_container_schedule_viewer_activity);
            }
        }

    }

    private void showScheduleViewerFragment() {
        Schedule schedule = SchedulesList.getInstance().get(getIntent().getIntExtra(KEY_INDEX, -1));
        openScheduleViewerFragment(schedule, ScheduleViewerFragment.VIEW, R.id.fragment_container_schedule_viewer_activity);
    }


}
