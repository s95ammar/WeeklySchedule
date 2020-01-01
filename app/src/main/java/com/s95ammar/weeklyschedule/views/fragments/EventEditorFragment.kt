package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.s95ammar.weeklyschedule.R
import dagger.android.support.DaggerFragment

class EventEditorFragment : DaggerFragment() {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_event_editor, container, false)
	}


}
