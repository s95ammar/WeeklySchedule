package com.s95ammar.weeklyschedule.views.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.s95ammar.weeklyschedule.R;
import com.s95ammar.weeklyschedule.models.CategoriesList;
import com.s95ammar.weeklyschedule.models.Category;
import com.s95ammar.weeklyschedule.models.Day;
import com.s95ammar.weeklyschedule.models.Event;
import com.s95ammar.weeklyschedule.models.Schedule;
import com.s95ammar.weeklyschedule.models.SchedulesList;
import com.s95ammar.weeklyschedule.adapters.CategorySpinnerAdapter;
import com.s95ammar.weeklyschedule.adapters.DaySpinnerAdapter;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment.ScheduleEditor.KEY_EVENT;
import static com.s95ammar.weeklyschedule.views.fragments.ScheduleViewerFragment.ScheduleEditor.KEY_SCHEDULE_INDEX;

public class EventRefactorActivity extends AppCompatActivity {
	private static final String TAG = "EventRefactorActivity";
	public static final int ADD = 0;
	public static final int EDIT = 1;
	private @Mode int mode;
	private Event editedEvent;
	private Schedule schedule;
	protected @BindView(R.id.eText_event_name) AutoCompleteTextView mEditTextName;
	protected @BindView(R.id.text_event_start_value) TextView mTextViewStart;
	protected @BindView(R.id.text_event_end_value) TextView mTextViewEnd;
	protected @BindView(R.id.spinner_categories) Spinner mSpinnerCategory;
	protected @BindView(R.id.spinner_days) Spinner mSpinnerDay;
	private LocalTime startTime;
	private LocalTime endTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_refactor);
		ButterKnife.bind(this);
		setMode();
		setSchedule();
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

	private void setViews() {
		switch (mode) {
			case ADD:
				mTextViewStart.setText(Event.DEFAULT_TIME.toString(Schedule.sTimePattern));
				mTextViewEnd.setText(Event.DEFAULT_TIME.toString(Schedule.sTimePattern));
				findViewById(R.id.button_delete).setVisibility(View.GONE);
				break;
			case EDIT:
				mEditTextName.setText(editedEvent.getName());
				mEditTextName.setSelection(mEditTextName.getText().length());
				mSpinnerCategory.setSelection(CategoriesList.getInstance().indexOf(editedEvent.getCategory()));
				mSpinnerDay.setSelection(schedule.getIndexOfDay(editedEvent.getDay()));
				mTextViewStart.setText(editedEvent.getStartTime().toString(Schedule.sTimePattern));
				mTextViewEnd.setText(editedEvent.getEndTime().toString(Schedule.sTimePattern));
				break;
		}
	}

	@OnClick({R.id.text_event_start_value, R.id.text_event_end_value})
	public void openTimePicker(View clickedView) {
		final int clickedViewId = clickedView.getId();
		String timeString = ((TextView) findViewById(clickedViewId)).getText().toString();
		LocalTime time = LocalTime.parse(timeString, DateTimeFormat.forPattern(Schedule.sTimePattern));
		int hour = time.getHourOfDay();
		int minute = time.getMinuteOfHour();
		TimePickerDialog timePickerDialog =new TimePickerDialog(this,
				getOnTimeSetListener(clickedViewId), hour, minute, DateFormat.is24HourFormat(this));
		timePickerDialog.show();
	}

	private TimePickerDialog.OnTimeSetListener getOnTimeSetListener(int clickedViewId) {
		return (view, hourOfDay, minute) -> {
			switch (clickedViewId) {
				case R.id.text_event_start_value:
					startTime = new LocalTime(hourOfDay, minute);
					endTime = LocalTime.parse(mTextViewEnd.getText().toString(), DateTimeFormat.forPattern(Schedule.sTimePattern));
					mTextViewStart.setText(startTime.toString(Schedule.sTimePattern));
					if (startTime.isAfter(endTime)) {
						mTextViewEnd.setText(startTime.toString(Schedule.sTimePattern));
					}
					break;
				case R.id.text_event_end_value:
					endTime = new LocalTime(hourOfDay, minute);
					mTextViewEnd.setText(endTime.toString(Schedule.sTimePattern));
					break;
			}
		};
	}

	private void setUpCategorySpinner() {
		CategorySpinnerAdapter mAdapter = new CategorySpinnerAdapter(this, CategoriesList.getInstance());
		mSpinnerCategory.setAdapter(mAdapter);
	}

	@OnItemSelected(R.id.spinner_categories)
	public void setUpNameAdapter(AdapterView<?> parent, int position) {
		Category clickedItem = (Category) parent.getItemAtPosition(position);
		mEditTextName.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clickedItem.getCategoryEventsNames()));
	}

	private void setUpDaySpinner() {
		DaySpinnerAdapter adapter = new DaySpinnerAdapter(this, schedule.getDays());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerDay.setAdapter(adapter);
	}

	@OnClick(R.id.button_ok)
	public void submitEvent() {
		String eventName = mEditTextName.getText().toString();
		Log.d(TAG, "submitEvent: " + eventName);
		LocalTime eventStartTime = LocalTime.parse(mTextViewStart.getText().toString(), DateTimeFormat.forPattern(Schedule.sTimePattern));
		LocalTime eventEndTime = LocalTime.parse(mTextViewEnd.getText().toString(), DateTimeFormat.forPattern(Schedule.sTimePattern));
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
					if (editedEvent.getDay().equals(eventDay)) {
						eventDay.getEvents().set(eventDay.getEvents().indexOf(editedEvent), event);
					} else {
						int editedEventDayIndex = schedule.getDays().indexOf(editedEvent.getDay());
						schedule.getDays().get(editedEventDayIndex).getEvents().remove(editedEvent);
						eventDay.getEvents().add(event);
					}
					break;
			}

			Collections.sort(eventDay.getEvents(), (o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));
			eventCategory.getCategoryEvents().add(event);
			Intent intent = new Intent();
			intent.putExtra(KEY_SCHEDULE_INDEX, SchedulesList.getInstance().indexOf(schedule));
            setResult(RESULT_OK, intent);
            finish();
		}

	}

	@OnClick(R.id.button_delete)
	public void deleteEvent() {
		int dayIndex = schedule.getDays().indexOf(editedEvent.getDay());
		schedule.getDays().get(dayIndex).getEvents().remove(editedEvent);
        Intent intent = new Intent();
        intent.putExtra(KEY_SCHEDULE_INDEX, SchedulesList.getInstance().indexOf(schedule));
        setResult(RESULT_OK, intent);
        finish();
    }

	@OnClick(R.id.button_cancel)
	public void closeActivity() {
		finish();
	}

	@IntDef({EDIT, ADD})
	@Retention(RetentionPolicy.SOURCE)
	private @interface Mode {
	}

}
