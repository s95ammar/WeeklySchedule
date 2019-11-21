package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.viewModels.CategoriesListViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_categories_list.*
import javax.inject.Inject
import androidx.recyclerview.widget.LinearLayoutManager
import com.s95ammar.weeklyschedule.views.recViewAdapters.CategoriesListAdapter

class CategoriesListFragment : DaggerFragment(), CategoriesListAdapter.OnItemClickListener {
	private val t = "log_${javaClass.simpleName}"

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: CategoriesListViewModel
	@Inject
	lateinit var listAdapter: CategoriesListAdapter

	init {
		Log.d(t, "init: $this")
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_categories_list, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let { viewModel = ViewModelProviders.of(it, factory).get(CategoriesListViewModel::class.java) }
		buildRecyclerView()
		startObservers()
		button_add_category.setOnClickListener { viewModel.showCategoryRefactorDialog() }
	}

	private fun startObservers() {
		viewModel.getAllCategories().observe(viewLifecycleOwner, Observer {
			Log.d(t, "startObservers: allCategories onChanged $it")
			onCategoriesChanged(it)
		})
	}

	private fun buildRecyclerView() {
		recyclerView_categories.setHasFixedSize(true)
		recyclerView_categories.layoutManager = LinearLayoutManager(activity)
		recyclerView_categories.adapter = listAdapter
		listAdapter.onItemClickListener = this
	}

	private fun onCategoriesChanged(list: List<Category>) {
		listAdapter.submitList(list)
		text_no_categories.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
	}

	override fun onItemClicked(i: Int) {
		listAdapter.getCategoryAt(i).let {
			viewModel.setEditedCategory(it)
			viewModel.showCategoryRefactorDialog(it)
		}
	}

	override fun onMoreClicked(i: Int, buttonMore: Button) {
		activity?.let { activity ->
			val popupMenu = PopupMenu(activity, buttonMore).apply {
				inflate(R.menu.categories_more_menu)
				setOnMenuItemClickListener { menuItem ->
					when (menuItem.itemId) {
						R.id.categories_more_edit -> onItemClicked(i)
						R.id.categories_more_delete -> listAdapter.getCategoryAt(i).also { viewModel.delete(it) }
					}
					true
				}
			}
			MenuPopupHelper(activity, popupMenu.menu as MenuBuilder, buttonMore).apply {
				setForceShowIcon(true)
			}.show()
		}
	}


}
