package com.s95ammar.weeklyschedule.interfaces;

import com.s95ammar.weeklyschedule.models.ScheduleItem;

public interface ScheduleViewer {
    void openScheduleViewerFragment(ScheduleItem schedule, int fragContainerId);
}
