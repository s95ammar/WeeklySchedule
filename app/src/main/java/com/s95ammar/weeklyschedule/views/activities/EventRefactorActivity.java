package com.s95ammar.weeklyschedule.views.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.s95ammar.weeklyschedule.views.adapters.DaySpinnerAdapter;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment.ScheduleEditor.KEY_EVENT;
import static com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment.ScheduleEditor.KEY_SCHEDULE_INDEX;

public class EventRefactorActivity extends AppCompatActivity {
	private static final String TAG = "EventRefactorActivity";
	public static final int ADD = 0;
	public static final int EDIT = 1;
	private @Mode int mode;
	private Event editedEvent;
	private ScheduleItem schedule;
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
		initializeViews();
		setUpCategorySpinner();
		setUpDaySpinner();
		setViews();
		setUpToolbar();
	}

	private void setMode() {
		Serializable obj = getIntent().getSerializableExtra(KEY_EVENT);
		if (obj instanceof Event) {
			editedEvent = (Event) obj;
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
				title = editedEvent.getName();
				break;
			default:
				throw new RuntimeException();
		}
		setTitle(title);
	}

	private void initializeViews() {
		mEditTextName = findViewById(R.id.eText_event_name);
		mTextViewStart = findViewById(R.id.tView_event_start_value);
		mTextViewEnd = findViewById(R.id.tView_event_end_value);
		mSpinnerCategory = findViewById(R.id.spinner_categories);
		mSpinnerDay = findViewById(R.id.spinner_days);
	}

	private void setViews() {
		switch (mode) {
			case ADD:
				defaultTime = new LocalTime(12, 00);
				mTextViewStart.setText(defaultTime.toString(ScheduleItem.timePattern));
				mTextViewEnd.setText(defaultTime.toString(ScheduleItem.timePattern));
				findViewById(R.id.button_delete).setVisibility(View.GONE);
				break;
			case EDIT:
				mEditTextName.setText(editedEvent.getName());
				mEditTextName.setSelection(mEditTextName.getText().length());
				mSpinnerCategory.setSelection(CategoriesList.getInstance().indexOf(editedEvent.getCategory()));
				mSpinnerDay.setSelection(schedule.getIndexOfDay(editedEvent.getDay()));
				defaultTime = new LocalTime(12, 00);
				mTextViewStart.setText(editedEvent.getStartTime().toString(ScheduleItem.timePattern));
				mTextViewEnd.setText(editedEvent.getEndTime().toString(ScheduleItem.timePattern));
				break;
		}
	}

	public void openTimePicker(View view) {
		final int viewId = view.getId();
		String timeString = ((TextView) findViewById(viewId)).getText().toString();
		LocalTime time = LocalTime.parse(timeString, DateTimeFormat.forPattern(ScheduleItem.timePattern));
		int hour = time.getHourOfDay();
		int minute = time.getMinuteOfHour();
		TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				switch (viewId) {
					case R.id.tView_event_start_value:
						startTime = new LocalTime(hourOfDay, minute);
						endTime = LocalTime.parse(mTextViewEnd.getText().toString(), DateTimeFormat.forPattern(ScheduleItem.timePattern));
						mTextViewStart.setText(startTime.toString(ScheduleItem.timePattern));
						if (startTime.isAfter(endTime)) {
							mTextViewEnd.setText(startTime.toString(ScheduleItem.timePattern));
						}
						break;
					case R.id.tView_event_end_value:
						endTime = new LocalTime(hourOfDay, minute);
						mTextViewEnd.setText(endTime.toString(ScheduleItem.timePattern));
						break;
				}
			}
		}, hour, minute, DateFormat.is24HourFormat(this));
		timePickerDialog.show();

	}

	private void setUpNameAdapter(ArrayList<String> list) {
		mEditTextName.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
	}

	private void setUpCategorySpinner() {
		CategorySpinnerAdapter mAdapter = new CategorySpinnerAdapter(this, CategoriesList.getInstance());
		mSpinnerCategory.setAdapter(mAdapter);

		mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Category clickedItem = (Category) parent.getItemAtPosition(position);
				setUpNameAdapter(clickedItem.getCategoryEventsNames());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void setUpDaySpinner() {
		DaySpinnerAdapter adapter = new DaySpinnerAdapter(this, schedule.getDays());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerDay.setAdapter(adapter);
	}

	public void submitEvent(View view) {
		String eventName = mEditTextName.getText().toString();
		Log.d(TAG, "submitEvent: " + eventName);
		LocalTime eventStartTime = LocalTime.parse(mTextViewStart.getText().toString(), DateTimeFormat.forPattern(ScheduleItem.timePattern));
		LocalTime eventEndTime = LocalTime.parse(mTextViewEnd.getText().toString(), DateTimeFormat.forPattern(ScheduleItem.timePattern));
		Category eventCategory = (Category) mSpinnerCategory.getSelectedItem();
		Day eventDay = (Day) mSpinnerDay.getSelectedItem();

		if (!Event.isTimeValid(eventStartTime, eventEndTime)) {
			Toast.makeText(this, R.string.start_end_time_error, Toast.LENGTH_SHORT).show();
		} else { //TODO: simplify
			Event event = new Event(eventName, eventCategory, eventDay, eventStartTime, eventEndTime);
			for (int i = 0; i < eventDay.getEvents().size(); i++) {
				switch (mode) {
					case ADD:
						if (event.overlapsWith(eventDay.getEvents().get(i))) {
							Toast.makeText(this, R.string.event_overlap_error, Toast.LENGTH_SHORT).show();
							return;
						}
						break;
					case EDIT:
						if (!editedEvent.equals(eventDay.getEvents().get(i)) && event.overlapsWith(eventDay.getEvents().get(i))) {
							Toast.makeText(this, R.string.event_overlap_error, Toast.LENGTH_SHORT).show();
							return;
						}
						break;
				}
			}
			switch (mode) {
				case ADD:
					eventDay.getEvents().add(event);
					break;
				case EDIT:
					eventDay.getEvents().set(eventDay.getEvents().indexOf(editedEvent), event);
					break;
			}

			Collections.sort(eventDay.getEvents(), new /*Event.EventNameComparator()*/ Comparator<Event>() { // TODO: restore this
				@Override
				public int compare(Event o1, Event o2) {
					return o1.getStartTime().compareTo(o2.getStartTime());
				}
			});
			eventCategory.getCategoryEvents().add(event);
			Intent intent = new Intent();
			intent.putExtra(KEY_SCHEDULE_INDEX, SchedulesList.getInstance().indexOf(schedule));
            setResult(RESULT_OK, intent);
            finish();
		}

	}

	public void deleteEvent(View view) {
		int dayIndex = schedule.getDays().indexOf(editedEvent.getDay());
		schedule.getDays().get(dayIndex).getEvents().remove(editedEvent);
        Intent intent = new Intent();
        intent.putExtra(KEY_SCHEDULE_INDEX, SchedulesList.getInstance().indexOf(schedule));
        setResult(RESULT_OK, intent);
        finish();
    }

	public void cancelListener(View view) {
		finish();
	}

	@IntDef({EDIT, ADD})
	@Retention(RetentionPolicy.SOURCE)
	private @interface Mode {
	}

}
