package com.s95ammar.weeklyschedule.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.CategoriesList;
import com.s95ammar.weeklyschedule.models.Event;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.models.SchedulesList;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class ScheduleViewerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ScheduleViewerFragment";
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
        if (bundle != null && schedule == null) {
            Serializable object = bundle.getSerializable(ScheduleEditor.KEY_SCHEDULE);
            if (object instanceof ScheduleItem) {
                schedule = (ScheduleItem) object;
            }
        }

        return inflater.inflate(R.layout.fragment_schedule_viewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpLayout();
        setUpActionButtonListener();
//        getView().findViewById(R.id.button_add_event).setOnClickListener(this);
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
        mListener.setDoneCancelVisibility(mode == EDIT);
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
        mListener.setDoneCancelVisibility(false);
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_event:
                if (!CategoriesList.getInstance().isEmpty()) {
                    mListener.openEventRefactorActivity(null, SchedulesList.getInstance().indexOf(schedule));  // TODO: onOk -> addEvent(name)
                } else {
                    Toast.makeText(getActivity(), R.string.category_list_empty_error, Toast.LENGTH_SHORT).show();
                }
            break;
        }
    }

    private void setUpActionButtonListener() {
        FloatingActionButton fab = getView().findViewById(R.id.button_add_event);
        fab.setOnClickListener(this);

    }



    public interface ScheduleEditor {
        void setEditVisibility(boolean visibility);
        void setDoneCancelVisibility(boolean visibility);
        void openEventRefactorActivity(Event event, int scheduleIndex);
        String KEY_SCHEDULE = "schedule";
        String KEY_SCHEDULE_INDEX = "schedule index";
        String KEY_EVENT = "event";

    }


    @IntDef({EDIT, VIEW})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Mode {}





}
