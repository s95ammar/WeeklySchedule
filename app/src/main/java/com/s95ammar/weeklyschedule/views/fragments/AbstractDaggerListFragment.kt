package com.s95ammar.weeklyschedule.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.weeklyschedule.util.LOG_TAG
import dagger.android.support.DaggerFragment

/**
* An abstraction layer for fragments, which are built by the MVVM architecture pattern,
* injected by Dagger and display an observable list of items in a RecyclerView
*
* @param <T> Type of the List items
* @param <VM> Type of the fragment's ViewModel
* @param <LA> Type of the ListAdapter, used in the RecyclerView
*
*/

abstract class AbstractDaggerListFragment<T, VM : ViewModel, LA : ListAdapter<T, out RecyclerView.ViewHolder>> : DaggerFragment() {

	protected lateinit var viewModel: VM
	private lateinit var itemsList: LiveData<List<T>>

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) = setListeners()

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = initViewModel()
		itemsList = assignItemsList()
		buildRecyclerView()
		startObservers()
	}

	private fun buildRecyclerView() {
		val recView = assignRecyclerView()
		val listAdapter = assignListAdapter()

		recView.apply {
			setHasFixedSize(true)
			layoutManager = LinearLayoutManager(activity)
			adapter = listAdapter
		}

	}

	protected fun showPopupMenu(@MenuRes menuRes: Int, anchor: View, listener: PopupMenu.OnMenuItemClickListener?) {
		val popupMenu = PopupMenu(requireActivity(), anchor).apply {
			inflate(menuRes)
			setOnMenuItemClickListener(listener)
		}
		MenuPopupHelper(requireActivity(), popupMenu.menu as MenuBuilder, anchor)
				.apply { setForceShowIcon(true) }
				.show()
	}

	@CallSuper
	protected open fun startObservers() {
		itemsList.observe(viewLifecycleOwner, Observer {
			Log.d(LOG_TAG, "onListChanged: $it")
			onListChanged(it)
		})
	}

	abstract fun setListeners()

	abstract fun initViewModel(): VM

	abstract fun assignItemsList(): LiveData<List<T>>

	abstract fun assignRecyclerView(): RecyclerView

	abstract fun assignListAdapter(): LA

	abstract fun onListChanged(itemsList: List<T>)
}
