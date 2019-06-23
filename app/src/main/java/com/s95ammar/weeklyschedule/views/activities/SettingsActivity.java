package com.s95ammar.weeklyschedule.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.s95ammar.weeklyschedule.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.title_settings);
    }
}
