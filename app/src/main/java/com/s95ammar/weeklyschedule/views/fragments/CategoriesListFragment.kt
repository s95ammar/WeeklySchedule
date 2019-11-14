package com.s95ammar.weeklyschedule.views.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Category
import com.s95ammar.weeklyschedule.viewModels.MainViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_categories_list.*
import javax.inject.Inject

class CategoriesListFragment : DaggerFragment() {
	private val t = "log_${javaClass.simpleName}"

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: MainViewModel

	init {
		Log.d(t, "init: $this")
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_categories_list, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let { viewModel = ViewModelProviders.of(it, factory).get(MainViewModel::class.java) }
		startObservers()
		button_add_category.setOnClickListener { viewModel.showCategoryRefactorDialog() }
	}

	private fun startObservers() {
		viewModel.getAllCategories().observe(viewLifecycleOwner, Observer {
			Log.d(t, "startObservers: allCategories onChanged $it") // TODO: remove
		})
	}

	fun insert(category: Category) = viewModel.insert(category)
	fun update(category: Category) = viewModel.update(category)
	fun delete(category: Category) = viewModel.delete(category)


}
