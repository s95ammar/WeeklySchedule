package com.s95ammar.weeklyschedule.views.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.google.android.material.navigation.NavigationView
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.*
import com.s95ammar.weeklyschedule.viewModels.CategoriesListViewModel
import com.s95ammar.weeklyschedule.viewModels.ScheduleViewerViewModel
import com.s95ammar.weeklyschedule.viewModels.SchedulesListViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), NavController.OnDestinationChangedListener, NavigationView.OnNavigationItemSelectedListener {
	private val t = "log_${javaClass.simpleName}"

	@Inject lateinit var factory: ViewModelProvider.Factory
	private lateinit var scheduleViewerViewModel: ScheduleViewerViewModel
	private lateinit var schedulesListViewModel: SchedulesListViewModel
	private lateinit var categoriesListViewModel: CategoriesListViewModel
	private lateinit var navController: NavController
	private lateinit var appBarConfig: AppBarConfiguration
	private lateinit var scheduleToolbarMenu: Menu
	private val topLevelDestinations = setOf(R.id.nav_top_level_schedule_viewer, R.id.nav_top_level_schedules, R.id.nav_top_level_categories)

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
		appBarConfig = AppBarConfiguration(topLevelDestinations, drawer_layout)
		navController = findNavController(R.id.nav_host_fragment)
		navController.setGraph(R.navigation.nav_graph, bundleOf(resources.getString(R.string.key_schedule_id) to Schedule.activeScheduleId))

		setupActionBarWithNavController(navController, appBarConfig)
		nav_view.setupWithNavController(navController)
		navController.addOnDestinationChangedListener(this)
		nav_view.setNavigationItemSelectedListener(this)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.toolbar_menu, menu)
		menu?.let { scheduleToolbarMenu = it }
		scheduleViewerViewModel.mode.observe(this, Observer { setScheduleToolbarMenuMode(it) })
		return true

	}

	private fun startObservers() {
		scheduleViewerViewModel.actionBarTitle.observe(this, Observer { supportActionBar?.title = it })
		scheduleViewerViewModel.showEventEditorFragment.observe(this, Observer {
			// TODO: make sure there's no need for an action
			navController.navigate(R.id.action_nav_schedule_viewer_to_eventEditorFragment)
		})
		schedulesListViewModel.onActiveScheduleIdChanged.observe(this, Observer { saveActiveScheduleId(Schedule.activeScheduleId) })
		schedulesListViewModel.showScheduleEditorDialog.observe(this, Observer {
			navController.navigate(R.id.action_nav_schedules_to_scheduleEditorDialog)
		})
		schedulesListViewModel.onScheduleItemClick.observe(this, Observer {
			navController.navigate(R.id.action_nav_schedules_to_nav_schedule_viewer, bundleOf(resources.getString(R.string.key_schedule_id) to it))
		})
		categoriesListViewModel.showCategoryEditorDialog.observe(this, Observer {
			navController.navigate(R.id.action_nav_categories_to_categoryEditorDialog)
		})
		categoriesListViewModel.showCategoryColorPicker.observe(this, Observer { openColorPicker(it) })
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean =
			if (navController.currentDestination?.id != item.itemId) {
				if (topLevelDestinations.contains(item.itemId)) navController.popBackStack()
				when (item.itemId) {
					R.id.nav_top_level_schedule_viewer -> {
						navController.navigate(R.id.nav_top_level_schedule_viewer, bundleOf(resources.getString(R.string.key_schedule_id) to Schedule.activeScheduleId))
						drawer_layout.closeIfOpen()
						true
					}
					else -> onNavDestinationSelected(item, navController).also { handled ->
						if (handled) drawer_layout.closeIfOpen()
					}
				}
			} else {
				drawer_layout.closeIfOpen()
				false
			}

	override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
		if (destination.id != R.id.nav_top_level_schedule_viewer) scheduleViewerViewModel.setMode(ScheduleMode.NOT_DISPLAYED)
	}

	private fun setScheduleToolbarMenuMode(mode: ScheduleMode) {
		scheduleToolbarMenu.findItem(R.id.button_edit).isVisible = when (mode) {
			ScheduleMode.VIEW -> true
			else -> false
		}
		scheduleToolbarMenu.findItem(R.id.button_done).isVisible = when (mode) {
			ScheduleMode.EDIT -> true
			else -> false
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
					drawer_layout.close()
					return true
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
