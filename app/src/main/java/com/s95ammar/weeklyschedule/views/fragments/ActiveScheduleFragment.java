package com.s95ammar.weeklyschedule.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.SchedulesList;

public class ActiveScheduleFragment extends Fragment {
//    private ScheduleManager mListener;

    public ActiveScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getTitle());
    }

    private String getTitle() {
        String title;
        if (SchedulesList.getInstance().getActiveSchedule() != null) {
            title = SchedulesList.getInstance().getActiveSchedule().getName();
        } else {
            title = getString(R.string.title_active_schedule);
        }
        return title;
    }

    //    Uncomment if communicator needed
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScheduleManager) {
            mListener = (ScheduleManager) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScheduleManager");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ScheduleManager {
    }
*/
}
