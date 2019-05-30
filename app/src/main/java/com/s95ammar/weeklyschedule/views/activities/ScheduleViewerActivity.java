package com.s95ammar.weeklyschedule.views.activities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.interfaces.ScheduleViewer;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment;

public class ScheduleViewerActivity extends AppCompatActivity implements ScheduleViewer {
    private ScheduleViewerFragment scheduleViewerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_viewer);
    }


    private void switchToFragment(@NonNull Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction().replace((R.id.fragment_container_schedule_viewer_activity), fragment).commit();
    }

    @Override
    public void viewScheduleInFragment(ScheduleItem schedule) {
        Bundle scheduleBundle = new Bundle();
        scheduleBundle.putSerializable("schedule", schedule);
        switchToFragment(scheduleViewerFragment != null ? scheduleViewerFragment : (scheduleViewerFragment = new ScheduleViewerFragment()), scheduleBundle);

    }
}
