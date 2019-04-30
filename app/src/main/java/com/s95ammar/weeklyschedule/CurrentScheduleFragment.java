package com.s95ammar.weeklyschedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CurrentScheduleFragment extends Fragment {
//    private ScheduleCreator mListener;

    public CurrentScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_schedule, container, false);
    }

//    Uncomment if communicator needed
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScheduleCreator) {
            mListener = (ScheduleCreator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScheduleCreator");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ScheduleCreator {
    }
*/
}
