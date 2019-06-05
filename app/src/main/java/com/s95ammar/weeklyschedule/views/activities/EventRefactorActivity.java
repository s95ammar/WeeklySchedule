package com.s95ammar.weeklyschedule.views.activities;

import android.app.TimePickerDialog;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.CategoriesList;
import com.s95ammar.weeklyschedule.models.Category;
import com.s95ammar.weeklyschedule.models.Day;
import com.s95ammar.weeklyschedule.models.Event;
import com.s95ammar.weeklyschedule.models.ScheduleItem;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.views.adapters.CategorySpinnerAdapter;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment.ScheduleEditor.KEY_EVENT;
import static com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment.ScheduleEditor.KEY_SCHEDULE_INDEX;

public class EventRefactorActivity extends AppCompatActivity {
    private static final String TAG = "EventRefactorActivity";
    public static final int ADD = 0;
    public static final int EDIT = 1;
    private @Mode int mode;
    private Event event;
    private ScheduleItem schedule;
    private String timePattern;
    private AutoCompleteTextView mEditTextName;
    private TextView mTextViewStart;
    private TextView mTextViewEnd;
    private Spinner mSpinnerCategory;
    private Spinner mSpinnerDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime defaultTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_refactor);
        setMode();
        setSchedule();
        setTimePattern();
        initializeViews();
        setViews();
        setUpToolbar();
        setUpCategorySpinner();
        setUpDaySpinner();
    }

    private void setMode() {
        Serializable obj = getIntent().getSerializableExtra(KEY_EVENT);
        if (obj instanceof Event) {
            event = (Event) obj;
            mode = EDIT;
        } else {
            mode = ADD;
        }
    }

    private void setSchedule() {
        int index = getIntent().getIntExtra(KEY_SCHEDULE_INDEX, -1);
        schedule = SchedulesList.getInstance().get(index);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_event_refactor_activity);
        setSupportActionBar(toolbar);
        String title;
        switch (mode) {
            case ADD:
                title = getString(R.string.new_event);
                break;
            case EDIT:
                title = event.getName();
                break;
            default:
                throw new RuntimeException();
        }
        setTitle(title);
/*
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
*/
    }

    private void initializeViews() {
        mEditTextName = findViewById(R.id.eText_event_name);
        mTextViewStart = findViewById(R.id.tView_event_start_value);
        mTextViewEnd = findViewById(R.id.tView_event_end_value);
        mSpinnerCategory = findViewById(R.id.spinner_categories);
    }

    private void setViews() {
        switch (mode) {
            case ADD:
                defaultTime = new LocalTime(12,00);
                mTextViewStart.setText(defaultTime.toString(timePattern));
                mTextViewEnd.setText(defaultTime.toString(timePattern));
                findViewById(R.id.button_delete).setVisibility(View.GONE);



                break;
            case EDIT:
                mEditTextName.setText(event.getName());
                mSpinnerCategory.setSelection(CategoriesList.getInstance().indexOf(event.getCategory()));
                defaultTime = new LocalTime(12,00);
                mTextViewStart.setText(event.getStartTime().toString(timePattern));
                mTextViewEnd.setText(event.getEndTime().toString(timePattern));





                break;
        }
    }

    private void setTimePattern() {
        timePattern = DateFormat.is24HourFormat(this) ? "HH:mm" : "hh:mm aa";
    }

    public void setTime(View view) {
        openTimePicker(view.getId());
    }

    public void openTimePicker(final int viewId) {
        String timeString = ((TextView) findViewById(viewId)).getText().toString();
        LocalTime time = LocalTime.parse(timeString, DateTimeFormat.forPattern(timePattern));
        int hour = time.getHourOfDay();
        int minute = time.getMinuteOfHour();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                switch (viewId) {
                    case R.id.tView_event_start_value:
                        startTime = new LocalTime(hourOfDay, minute);
                        mTextViewStart.setText(startTime.toString(timePattern));
                        break;
                    case R.id.tView_event_end_value:
                        endTime = new LocalTime(hourOfDay, minute);
                        mTextViewEnd.setText(endTime.toString(timePattern));
                }


            }
        }, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    private void setUpNameAdapter(ArrayList<String> list) {
        mEditTextName.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
    }

    private void setUpCategorySpinner() {
        mSpinnerCategory = findViewById(R.id.spinner_categories);
        CategorySpinnerAdapter mAdapter = new CategorySpinnerAdapter(this, CategoriesList.getInstance());
        mSpinnerCategory.setAdapter(mAdapter);

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category clickedItem = (Category) parent.getItemAtPosition(position);
                setUpNameAdapter(clickedItem.getCategoryEventsNames());
/*
                Drawable drawable = getDrawable(R.drawable.shape_rounded_rectangle);
                drawable.setTint(clickedItem.getFillColor());
                mEditTextName.setBackground(drawable);
                mEditTextName.setTextColor(clickedItem.getTextColor());
*/
                Log.d(TAG, "onItemSelected: " + clickedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setUpDaySpinner() {
        mSpinnerDay= findViewById(R.id.spinner_day);
        CategorySpinnerAdapter mAdapter = new CategorySpinnerAdapter(this, CategoriesList.getInstance());
        mSpinnerDay.setAdapter(mAdapter);
        ArrayAdapter<Day> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_days,
                schedule.getDays());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDay.setAdapter(adapter);
    }

    public void submitEvent(View view) {
        String eventName = mEditTextName.getText().toString();
        LocalTime eventStartTime = LocalTime.parse(mTextViewStart.getText().toString(), DateTimeFormat.forPattern(timePattern));
        LocalTime eventEndTime = LocalTime.parse(mTextViewEnd.getText().toString(), DateTimeFormat.forPattern(timePattern));
        Category eventCategory = (Category) mSpinnerCategory.getSelectedItem();
        Day eventDay = (Day) mSpinnerDay.getSelectedItem(); // TODO: make sure this works correctly
        Event event = new Event(eventName, eventCategory, eventDay, eventStartTime, eventEndTime);
        for (int i = 0; i < eventDay.getEvents().size(); i++) {
            if (event.overlapsWith(eventDay.getEvents().get(i))) {
                Toast.makeText(this, R.string.event_overlap_error, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!event.isNameValid()) {
            Toast.makeText(this, R.string.empty_name_error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!event.isTimeValid()) {
            Toast.makeText(this, R.string.start_end_time_error, Toast.LENGTH_SHORT).show();
            return;
        }

        eventDay.getEvents().add(event);
        Collections.sort(eventDay.getEvents(), new Event.EventTimeComparator());
        eventCategory.getCategoryEvents().add(event);
        event.setDay(eventDay);
        event.setCategory(eventCategory);

        Log.d(TAG, "submitEvent: added event:" + event);
        Log.d(TAG, "submitEvent: day:" + eventDay.getEvents());
        Log.d(TAG, "submitEvent: category" + eventCategory.getCategoryEvents());
    }

    public void deleteEvent(View view) {
    }

    public void cancelListener(View view) {
        finish();
    }

    @IntDef({EDIT, ADD})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Mode {}

}
