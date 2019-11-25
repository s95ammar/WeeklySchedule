package com.s95ammar.weeklyschedule.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment

/**
* An abstraction layer for fragments, which are built by the MVVM architecture pattern
* and display an observable list of items in a RecyclerView, where a popup menu can be
* opened from any item.
*
* @param <T> Type of the List items
* @param <VM> Type of the fragment's ViewModel
*
*/

abstract class AbstractListFragment<T, VM : ViewModel> : DaggerFragment() {

	protected lateinit var viewModel: VM
	private lateinit var itemsList: LiveData<List<T>>
	private lateinit var recView: RecyclerView

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setListeners()
	}

	abstract fun setListeners()

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = initViewModel()
		itemsList = assignItemsList()
		recView = assignRecyclerView()
		buildRecyclerView()
		itemsList.observe(viewLifecycleOwner, Observer {
			Log.d("log_${javaClass.simpleName}", "onActivityCreated: $it");
			onListChanged(it)
		})
	}

	abstract fun initViewModel(): VM

	abstract fun assignItemsList(): LiveData<List<T>>

	abstract fun assignRecyclerView(): RecyclerView

	private fun buildRecyclerView() {
		recView.apply {
			setHasFixedSize(true)
			layoutManager = LinearLayoutManager(activity)
		}
		initRecViewAdapter()
	}

	abstract fun initRecViewAdapter()

	abstract fun onListChanged(itemsList: List<T>)

	fun showPopupMenu(@MenuRes menuRes: Int, anchor: View, listener: PopupMenu.OnMenuItemClickListener?) {
		val popupMenu = PopupMenu(requireActivity(), anchor).apply {
			inflate(menuRes)
			setOnMenuItemClickListener(listener)
		}
		MenuPopupHelper(requireActivity(), popupMenu.menu as MenuBuilder, anchor)
				.apply { setForceShowIcon(true) }
				.show()
	}
}
