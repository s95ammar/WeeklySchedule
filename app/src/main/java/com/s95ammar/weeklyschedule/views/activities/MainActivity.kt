package com.s95ammar.weeklyschedule.views.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.iterator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.CategoriesListViewModel
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), NavController.OnDestinationChangedListener {
	private val t = "log_${javaClass.simpleName}"

	@Inject lateinit var factory: ViewModelProvider.Factory
	private lateinit var scheduleViewerViewModel: ScheduleViewerViewModel
	private lateinit var schedulesListViewModel: SchedulesListViewModel
	private lateinit var categoriesListViewModel: CategoriesListViewModel
	private lateinit var navController: NavController
	private lateinit var appBarConfig: AppBarConfiguration
	private lateinit var scheduleModeMenu: Menu

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

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.toolbar_menu, menu)
		menu?.let { scheduleModeMenu = it }
		navController.addOnDestinationChangedListener(this)
		scheduleViewerViewModel.mode.observe(this, Observer { setScheduleMenuMode(it) })
		return true

	}

	private fun startObservers() {
		scheduleViewerViewModel.actionBarTitle.observe(this, Observer { supportActionBar?.title = it })
		schedulesListViewModel.onActiveScheduleIdChanged.observe(this, Observer { saveActiveScheduleId(Schedule.activeScheduleId) })
		schedulesListViewModel.showScheduleEditorDialog.observe(this, Observer {
			navController.navigate(R.id.action_nav_schedules_to_scheduleEditorDialog)
		})
		categoriesListViewModel.showCategoryEditorDialog.observe(this, Observer {
			navController.navigate(R.id.action_nav_categories_to_categoryEditorDialog)
		})
		categoriesListViewModel.showCategoryColorPicker.observe(this, Observer { openColorPicker(it) })
	}

	override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
		when (destination.id) {
			R.id.nav_active_schedule -> {
				schedulesListViewModel.getActiveSchedule().observeOnce(Observer {
					scheduleViewerViewModel.setSchedule(it)
					if (it == null) hideScheduleMenu()
				})
			}
			else -> hideScheduleMenu()
		}
	}

	private fun hideScheduleMenu() { if (scheduleModeMenu.hasVisibleItems()) scheduleModeMenu.iterator().forEach { it.isVisible = false } }

	private fun setScheduleMenuMode(mode: ScheduleMode) {
		scheduleModeMenu.findItem(R.id.button_edit).isVisible = when (mode) {
			ScheduleMode.VIEW -> true
			ScheduleMode.EDIT -> false
		}
		scheduleModeMenu.findItem(R.id.button_done).isVisible = when (mode) {
			ScheduleMode.VIEW -> false
			ScheduleMode.EDIT -> true
		}
	}

	fun scheduleMenuEditListener(item: MenuItem) = scheduleViewerViewModel.setMode(ScheduleMode.EDIT)

	fun scheduleMenuDoneListener(item: MenuItem) = scheduleViewerViewModel.setMode(ScheduleMode.VIEW)

	// adds functionality to burger icon (only for opening the drawer) and back arrow
	override fun onSupportNavigateUp() = navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home ->
				if (drawer_layout.isOpen()) {
					drawer_layout.close(); return true
				}
		}
		return false
	}

	override fun onBackPressed() {
		if (drawer_layout.isOpen())
			drawer_layout.close()
		else
			super.onBackPressed()
	}

	private fun openColorPicker(colorDetails: ColorDetails) {
		MaterialDialog(this).show {
			title(when (colorDetails.target) {
				ColorTarget.FILL -> R.string.fill_color
				ColorTarget.TEXT -> R.string.text_color
			})
			colorChooser(
					MAIN_COLORS_ARRAY,
					subColors = SHADES_OF_MAIN_COLORS,
					allowCustomArgb = true,
					showAlphaSelector = true,
					initialSelection = colorDetails.color
			) { _, selectedColor ->
				categoriesListViewModel.setCategoryColor(ColorDetails(selectedColor, colorDetails.target))
			}
			positiveButton(R.string.select)
			negativeButton(R.string.cancel)
		}

	}

	private fun saveActiveScheduleId(activeScheduleId: Int) {
		Log.d(t, "saveActiveScheduleId: $activeScheduleId")
		getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
				.edit()
				.putInt(ACTIVE_SCHEDULE_ID_KEY, activeScheduleId)
				.apply()
	}

}
