package com.s95ammar.weeklyschedule.views.activities

import android.graphics.Color.*
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.s95ammar.weeklyschedule.R
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.s95ammar.weeklyschedule.util.close
import com.s95ammar.weeklyschedule.util.isOpen
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.s95ammar.weeklyschedule.util.ColorType
import com.s95ammar.weeklyschedule.viewModels.CategoriesListViewModel
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel


class MainActivity : DaggerAppCompatActivity() {
	private val t = "log_${javaClass.simpleName}"

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var scheduleViewerViewModel: ScheduleViewerViewModel
	private lateinit var schedulesListViewModel: SchedulesListViewModel
	private lateinit var categoriesListViewModel: CategoriesListViewModel
	private lateinit var navController: NavController
	private lateinit var appBarConfig: AppBarConfiguration


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		scheduleViewerViewModel = ViewModelProviders.of(this, factory).get(ScheduleViewerViewModel::class.java)
		schedulesListViewModel = ViewModelProviders.of(this, factory).get(SchedulesListViewModel::class.java)
		categoriesListViewModel = ViewModelProviders.of(this, factory).get(CategoriesListViewModel::class.java)
		startObservers()
		initNavController()
	}

	private fun initNavController() {
		val topLevelDestinations = setOf(R.id.nav_active_schedule, R.id.nav_schedules, R.id.nav_categories)
		appBarConfig = AppBarConfiguration(topLevelDestinations, drawer_layout)
		navController = findNavController(R.id.nav_host_fragment)
		setupActionBarWithNavController(navController, appBarConfig)
		nav_view.setupWithNavController(navController)
	}

	// adds functionality to burger icon (only for opening the drawer) and back arrow
	override fun onSupportNavigateUp(): Boolean {
		return navController.navigateUp(appBarConfig) /*|| super.onSupportNavigateUp()*/
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home ->
				if (drawer_layout.isOpen()) { drawer_layout.close(); return true }
		}
		return false
	}

	override fun onBackPressed() {
		if (drawer_layout.isOpen())
			drawer_layout.close()
		else
			super.onBackPressed()
	}

	private fun startObservers() {
		categoriesListViewModel.onAddCategoryActionButtonClick.observe(this, Observer {
			navController.navigate(R.id.action_nav_categories_to_categoryRefactorDialog)
		})
		categoriesListViewModel.onCategoryColorButtonClick.observe(this, Observer { colorType ->
			MaterialDialog(this).show {
				title(when (colorType) {
					ColorType.TEXT -> R.string.text_color
					ColorType.FILL -> R.string.fill_color
				})
				colorChooser(intArrayOf(RED, GREEN, BLUE), allowCustomArgb = true) { dialog, color ->
					categoriesListViewModel.receiveColor(colorType, color)
				}
				positiveButton(R.string.select)
				negativeButton(R.string.cancel)

			}
		})
	}


}
