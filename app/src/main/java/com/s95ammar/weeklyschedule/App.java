package com.s95ammar.weeklyschedule;

import android.app.Application;
import android.text.format.DateFormat;

import com.s95ammar.weeklyschedule.models.Schedule;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		setTimePattern();
	}

	private void setTimePattern() {
		Schedule.sTimePattern = DateFormat.is24HourFormat(this) ? "HH:mm" : "hh:mm aa";
	}

}
