package com.s95ammar.weeklyschedule.views.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.ScheduleItem;

import java.io.Serializable;

public class ScheduleViewerActivity extends ParentActivity /*implements ScheduleViewer*/ {
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
        Serializable object = getIntent().getSerializableExtra("schedule");
        if (object instanceof ScheduleItem) {
            openScheduleViewerFragment((ScheduleItem) object, R.id.fragment_container_schedule_viewer_activity);
        }

        return true;
    }



/*
    @Override
    public void openScheduleViewerFragment(ScheduleItem schedule) {
        Bundle scheduleBundle = new Bundle();
        scheduleBundle.putSerializable("schedule", schedule);
        switchToFragment(scheduleViewerFragment != null ? scheduleViewerFragment : (scheduleViewerFragment = new ScheduleViewerFragment()), scheduleBundle);

    }
*/
}
