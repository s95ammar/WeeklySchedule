package com.s95ammar.weeklyschedule.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.models.SchedulesList;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class ScheduleViewerFragment extends Fragment implements View.OnClickListener {
    public static final int VIEW = 0;
    public static final int EDIT = 1;
    private ScheduleEditor mListener;
    private ScheduleItem schedule;

    public ScheduleViewerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Serializable object = bundle.getSerializable("schedule");
            if (object instanceof ScheduleItem) {
                schedule = (ScheduleItem) object;
            }
        }

        return inflater.inflate(R.layout.fragment_active_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpLayout();
        getView().findViewById(R.id.button_add_event).setOnClickListener(this);
    }

    private void setUpLayout() {
//        boolean hasActiveSchedule = SchedulesList.getInstance().hasActiveSchedule();

        getActivity().setTitle(schedule != null ? schedule.getName() : getString(R.string.title_active_schedule));
        getView().findViewById(R.id.textView_no_active_schedule).setVisibility(schedule != null ? View.GONE : View.VISIBLE);
        if (schedule != null) {
            setMode(VIEW);
        }
    }


    public void setMode(@Mode int mode) {
        mListener.setDoneVisibility(mode == EDIT);
        mListener.setEditVisibility(mode == VIEW);
        getView().findViewById(R.id.button_add_event).setVisibility(mode == EDIT ? View.VISIBLE : View.GONE);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScheduleEditor) {
            mListener = (ScheduleEditor) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScheduleEditor");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.setEditVisibility(false);
        mListener.setDoneVisibility(false);
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_event:
//                TODO: open event-creator dialog
            break;
        }
    }


    public interface ScheduleEditor {
        void setEditVisibility(boolean visibility);
        void setDoneVisibility(boolean visibility);
    }


    @IntDef({EDIT, VIEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {}

/*
    public interface EventManager() {

    }
*/



}
