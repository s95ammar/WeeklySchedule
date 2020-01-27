package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.viewModels.CategoriesListViewModel
import com.s95ammar.weeklyschedule.views.recViewAdapters.CategoriesListAdapter
import com.s95ammar.weeklyschedule.views.recViewAdapters.OnListItemClickListener
import kotlinx.android.synthetic.main.fragment_categories_list.*
import javax.inject.Inject

class CategoriesListFragment : AbstractDaggerListFragment<Category, CategoriesListViewModel, CategoriesListAdapter>(),
		OnListItemClickListener<Category> {

	@Inject lateinit var factory: ViewModelProvider.Factory
	@Inject lateinit var listAdapter: CategoriesListAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_categories_list, container, false)
	}

	override fun setListeners() = button_add_category.setOnClickListener { viewModel.showCategoryEditorDialog() }

	override fun initViewModel() = ViewModelProviders.of(requireActivity(), factory).get(CategoriesListViewModel::class.java)

	override fun assignItemsList() = viewModel.getAllCategories()

	override fun assignRecyclerView(): RecyclerView = recyclerView_categories

	override fun assignListAdapter() = listAdapter.apply { onItemClickListener = this@CategoriesListFragment }

	override fun onListChanged(itemsList: List<Category>) {
		listAdapter.submitList(itemsList)
		textView_no_categories.visibility = if (itemsList.isEmpty()) View.VISIBLE else View.GONE
	}

	override fun onItemClicked(item: Category) {
		viewModel.setEditedCategory(item)
		viewModel.showCategoryEditorDialog()
	}

	override fun onMoreClicked(item: Category, buttonMore: Button) = showPopupMenu(R.menu.categories_more_menu, buttonMore,
			PopupMenu.OnMenuItemClickListener { menuItem -> onMenuItemClick(item, menuItem) })

	private fun onMenuItemClick(category: Category, menuItem: MenuItem): Boolean {
		when (menuItem.itemId) {
			R.id.categories_more_edit -> onItemClicked(category)
			R.id.categories_more_delete -> viewModel.delete(category)
		}
		return true
	}
}
