package com.s95ammar.weeklyschedule.views.activities;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.CategoriesList;
import com.s95ammar.weeklyschedule.models.Category;
import com.s95ammar.weeklyschedule.models.Event;
import com.s95ammar.weeklyschedule.views.adapters.CategorySpinnerAdapter;

import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.Calendar;

import static com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment.ScheduleEditor.KEY_EVENT;

public class EventRefactorActivity extends AppCompatActivity {
    private static final String TAG = "EventRefactorActivity";
    private Event event;
    private String[] days ;
    private String timePattern;
    private EditText mEditTextName;
    private TextView mTextViewStart;
    private TextView mTextViewEnd;
    private Spinner mSpinnerDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime defaultTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_refactor);
        setTimePattern();
        initializeViews();
        setValues();
        setUpToolbar();
        setUpCategorySpinner();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_event_refactor_activity);
        setSupportActionBar(toolbar);
        setTitle(event == null ? getString(R.string.new_event) : event.getName());
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
        defaultTime = new LocalTime(12,00);
        mTextViewStart.setText(defaultTime.toString(timePattern));
        mTextViewEnd.setText(defaultTime.toString(timePattern));

    }

    private void setTimePattern() {
        timePattern = DateFormat.is24HourFormat(this) ? "HH:mm" : "hh:mm aa";
    }

    private void setValues() {
        Serializable obj = getIntent().getExtras().getSerializable(KEY_EVENT);
        if (obj instanceof Event) {
            Event event = (Event) obj;
            mEditTextName.setText(event.getName());
            mTextViewStart.setText(event.getStartTime().toString(timePattern));
            mTextViewEnd.setText(event.getEndTime().toString(timePattern));
        } else {
            findViewById(R.id.button_delete).setVisibility(View.GONE);
        }
    }

    public void setTime(View view) {
        openTimePicker(view.getId());
    }

    public void openTimePicker(final int viewId) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
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

    private void setUpCategorySpinner() {
        Spinner spinnerCategories = findViewById(R.id.spinner_categories);
        CategorySpinnerAdapter mAdapter = new CategorySpinnerAdapter(this, CategoriesList.getInstance());
        spinnerCategories.setAdapter(mAdapter);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category clickedItem = (Category) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: " + clickedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void submitEvent(View view) {
    }

    public void deleteEvent(View view) {
    }

    public void cancelListener(View view) {
        finish();
    }
}
