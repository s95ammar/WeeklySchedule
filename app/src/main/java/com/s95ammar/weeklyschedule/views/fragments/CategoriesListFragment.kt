package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.s95ammar.weeklyschedule.R

class CategoriesListFragment : Fragment() {
	private val t = "log_${javaClass.simpleName}"

	init {
        Log.d(t, "init: $this")
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_categories_list, container, false)
	}


}
