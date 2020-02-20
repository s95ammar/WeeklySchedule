package com.s95ammar.weeklyschedule.views.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.models.data.Schedule
import com.s95ammar.weeklyschedule.util.close
import com.s95ammar.weeklyschedule.util.closeIfOpen
import com.s95ammar.weeklyschedule.util.isOpen
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), NavController.OnDestinationChangedListener, NavigationView.OnNavigationItemSelectedListener {

	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var navController: NavController
	private lateinit var appBarConfig: AppBarConfiguration
	private val topLevelDestinations = setOf(R.id.nav_top_level_schedule_viewer, R.id.nav_top_level_schedules, R.id.nav_top_level_categories)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
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


	override fun onNavigationItemSelected(navigationItem: MenuItem): Boolean {
		return if (navController.currentDestination?.id != navigationItem.itemId) {
			if (topLevelDestinations.contains(navigationItem.itemId)) navController.popBackStack()
			when (navigationItem.itemId) {
				R.id.nav_top_level_schedule_viewer -> {
					navController.navigate(R.id.nav_top_level_schedule_viewer, bundleOf(resources.getString(R.string.key_schedule_id) to Schedule.activeScheduleId))
					drawer_layout.closeIfOpen()
					true
				}
				else -> onNavDestinationSelected(navigationItem, navController).also { handled ->
					if (handled) drawer_layout.closeIfOpen()
				}
			}
		} else {
			drawer_layout.closeIfOpen()
			false
		}
	}

	override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
		drawer_layout.setDrawerLockMode(if (topLevelDestinations.contains(destination.id)) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
	}


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

}
