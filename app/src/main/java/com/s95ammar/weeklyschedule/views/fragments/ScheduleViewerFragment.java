package com.s95ammar.weeklyschedule.views.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.CategoriesList;
import com.s95ammar.weeklyschedule.models.Day;
import com.s95ammar.weeklyschedule.models.Event;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.models.SchedulesList;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleViewerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ScheduleViewerFragment";
    public static final int VIEW = 0;
    public static final int EDIT = 1;
    private static final int TEXT_VIEW_HEADER_HEIGHT = 100;
    private static final int TEXT_SIZE = 20;
    private static final int TEXT_VIEWS_WIDTH = 300;
    private static final int PADDING = 8;
    private ScheduleEditor mListener;
    private ScheduleItem schedule;
    private ConstraintLayout layoutScheduleViewer;
    private ArrayList<TextView> textViewsDays;
    private ArrayList<TextView> textViewsHours;
    private HashMap<Event, TextView> hashMapEvents;

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
    }

    private void setUpLayout() {
        getActivity().setTitle(schedule != null ? schedule.getName() : getString(R.string.title_active_schedule));
        getView().findViewById(R.id.textView_no_active_schedule).setVisibility(schedule != null ? View.GONE : View.VISIBLE);
        if (schedule != null) {
            showSchedule();
            setMode(getArguments().getInt(ScheduleEditor.KEY_MODE, VIEW));
        }
    }

    public void setMode(@Mode int mode) {
        mListener.setDoneCancelVisibility(mode == EDIT);
        mListener.setEditVisibility(mode == VIEW);
        getView().findViewById(R.id.button_add_event).setVisibility(mode == EDIT ? View.VISIBLE : View.GONE);
        setEventsTextViewsOnClickListeners(mode);
    }

    public void showSchedule() {
        layoutScheduleViewer = getView().findViewById(R.id.layout_schedule_viewer);
        prepareHeaderTextViews(textViewsHours = new ArrayList<>(), Day.TOTAL_HOURS, Day.getHoursStringArray());
        prepareHeaderTextViews(textViewsDays = new ArrayList<>(), ScheduleItem.WEEK_DAYS.length, ScheduleItem.WEEK_DAYS);
        prepareEventTextViews();
        connectTextViews();
    }

    private void prepareHeaderTextViews(ArrayList<TextView> textViews, int length, String[] stringArray) {
            for (int i = 0; i < length; i++) {
                TextView tv = getTableTextView();
                formatHeaderTextView(tv, stringArray[i]);
                textViews.add(tv);
                layoutScheduleViewer.addView(tv);
            }
    }

    private void prepareEventTextViews() {
        hashMapEvents = new HashMap<>();
        for (int i = 0; i < schedule.getDays().size(); i++) {
            Day day = schedule.getDays().get(i);
            for (int j = 0; j < day.getEvents().size(); j++) {
                Event event = day.getEvents().get(j);
                TextView tv = getTableTextView();
                formatEventTextView(tv, event);
                hashMapEvents.put(event, tv);
                layoutScheduleViewer.addView(tv);
            }
        }
    }

    private TextView getTableTextView() {
        TextView tv = new TextView(getActivity());
        tv.setId(View.generateViewId());
        tv.setMinWidth(TEXT_VIEWS_WIDTH);
        tv.setTextSize(TEXT_SIZE);
        tv.setBackground(getActivity().getDrawable(R.drawable.shape_rounded_rectangle).mutate());
        tv.setPaddingRelative(PADDING, PADDING, PADDING, PADDING);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    private void formatHeaderTextView(TextView tv, String text) {
        tv.setMinHeight(TEXT_VIEW_HEADER_HEIGHT);
        tv.setText(text);
        tv.setTextColor(Color.BLACK);
        tv.getBackground().mutate().setTint(Color.GRAY);
    }

    private void formatEventTextView(TextView tv, Event event) {
        tv.setText(event.getName());
        tv.setTextColor(event.getCategory().getTextColor());
        tv.getBackground().mutate().setTint(event.getCategory().getFillColor());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(TEXT_VIEWS_WIDTH, 0);
        tv.setLayoutParams(layoutParams);
    }

    private void connectTextViews() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layoutScheduleViewer);
        connectHeaderTextViews(textViewsHours, constraintSet, false);
        connectHeaderTextViews(textViewsDays, constraintSet, true);
        connectEventTextViews(constraintSet);
        constraintSet.applyTo(layoutScheduleViewer);

    }

    private void setEventsTextViewsOnClickListeners(@Mode int mode) {
        for (int i = 0; i < schedule.getDays().size(); i++) {
            Day day = schedule.getDays().get(i);
            for (int j = 0; j < day.getEvents().size(); j++) {
                Event event = day.getEvents().get(j);
                TextView textView = hashMapEvents.get(event);
                switch (mode) {
                    case VIEW:
                        textView.setOnClickListener(v -> {});
                        break;
                    case EDIT:
                        textView.setOnClickListener(v ->
                                mListener.startEventRefactorActivity(event, SchedulesList.getInstance().indexOf(schedule)));
                        break;
                }
            }
        }
    }

    private void connectHeaderTextViews(ArrayList<TextView> arrayList, ConstraintSet constraintSet, boolean horizontal) {
        for (int i = 0; i < arrayList.size(); i++) {
            switch (i) {
                case 0:
                    constraintSet.connect(arrayList.get(i).getId(),
                            horizontal ? ConstraintSet.LEFT : ConstraintSet.TOP,
                            R.id.layout_schedule_viewer,
                            horizontal ? ConstraintSet.LEFT : ConstraintSet.TOP,
                            horizontal ? 300 : 100);
                    break;
                default:
                    constraintSet.connect(arrayList.get(i).getId(),
                            horizontal ? ConstraintSet.LEFT : ConstraintSet.TOP,
                            arrayList.get(i - 1).getId(),
                            horizontal ? ConstraintSet.RIGHT : ConstraintSet.BOTTOM);
                    break;
            }
        }
    }

    private void connectEventTextViews(ConstraintSet constraintSet) {
        for (int i = 0; i < schedule.getDays().size(); i++) {
            Day day = schedule.getDays().get(i);
            for (int j = 0; j < day.getEvents().size(); j++) {
                Event event = day.getEvents().get(j);
                TextView eventTextView = hashMapEvents.get(event);
                TextView targetDay = textViewsDays.get(schedule.getIndexOfDay(event.getDay()));
                TextView targetStartHour = textViewsHours.get(event.getStartTime().getHourOfDay());
                TextView targetEndHour = textViewsHours.get(event.getEndTime().getHourOfDay());
                double headerHeight = TEXT_VIEW_HEADER_HEIGHT;
                constraintSet.connect(eventTextView.getId(), ConstraintSet.TOP, targetStartHour.getId(), ConstraintSet.TOP,
                        (int) ((headerHeight /Day.MINUTES_IN_HOUR) * event.getStartTime().getMinuteOfHour()));
                constraintSet.connect(eventTextView.getId(), ConstraintSet.BOTTOM, targetEndHour.getId(), ConstraintSet.BOTTOM,
                        (int) ((headerHeight /Day.MINUTES_IN_HOUR) * (Day.MINUTES_IN_HOUR - event.getEndTime().getMinuteOfHour())));
                constraintSet.connect(eventTextView.getId(), ConstraintSet.LEFT, targetDay.getId(), ConstraintSet.LEFT);
                constraintSet.connect(eventTextView.getId(), ConstraintSet.RIGHT, targetDay.getId(), ConstraintSet.RIGHT);
                constraintSet.constrainDefaultHeight(eventTextView.getId(), ConstraintSet.MATCH_CONSTRAINT_SPREAD);
            }
        }
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
                    mListener.startEventRefactorActivity(null, SchedulesList.getInstance().indexOf(schedule));
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
        void startEventRefactorActivity(Event event, int scheduleIndex);
        void openScheduleViewerFragment(ScheduleItem schedule, @Mode int mode, int fragContainerId);
        String KEY_MODE = "mode";
        String KEY_SCHEDULE = "schedule";
        String KEY_SCHEDULE_INDEX = "schedule index";
        String KEY_EVENT = "event";
    }


    @IntDef({EDIT, VIEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }


}
