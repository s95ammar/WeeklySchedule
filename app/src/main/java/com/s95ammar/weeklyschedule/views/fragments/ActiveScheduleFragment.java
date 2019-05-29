package com.s95ammar.weeklyschedule.views.fragments;

import android.content.Context;
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
    private ScheduleEditor mListener;

    public ActiveScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_active_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout(false);
    }


    public void refreshLayout(boolean editMode) {
        boolean hasActiveSchedule = SchedulesList.getInstance().hasActiveSchedule();
        getActivity().setTitle(hasActiveSchedule ? SchedulesList.getInstance().getActiveSchedule().getName() : getString(R.string.title_active_schedule));
        getView().findViewById(R.id.textView_no_active_schedule).setVisibility(hasActiveSchedule ? View.GONE : View.VISIBLE);

        if (!editMode) {
            mListener.setEditVisibility(hasActiveSchedule);
        } else {
            mListener.setDoneVisibility(hasActiveSchedule);
        }

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScheduleEditor) {
            mListener = (ScheduleEditor) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScheduleManager");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.setEditVisibility(false);
        mListener.setDoneVisibility(false);
        mListener = null;
    }


    public interface ScheduleEditor {
        void setEditVisibility(boolean visibility);
        void setDoneVisibility(boolean visibility);
    }

/*
    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {}
*/


}
