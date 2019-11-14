package com.s95ammar.weeklyschedule.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.s95ammar.weeklyschedule.App
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.di.main.MainActivitySubcomponent
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.s95ammar.weeklyschedule.util.close
import com.s95ammar.weeklyschedule.util.isOpen
import com.s95ammar.weeklyschedule.viewModels.MainViewModel
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
	private val t = "log_${javaClass.simpleName}"
	private lateinit var component: MainActivitySubcomponent
	@Inject
	lateinit var factory: ViewModelProvider.Factory
	private lateinit var viewModel: MainViewModel
	private lateinit var navController: NavController
	private lateinit var appBarConfig: AppBarConfiguration

	//    private enum class NavDrawerItems(var itemNum: Int) { ACTIVE_SCHEDULE(0), SCHEDULES(1) }
	private companion object NavDrawerItems {
		const val ACTIVE_SCHEDULE = 0
		const val SCHEDULES = 1
		//        const val SETTINGS = 2 // TODO: implement
		const val INFO = 3
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		initComponent()
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
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

	}

	private fun initComponent() {
		component =
				(application as App).component.getMainActivityComponentFactory().create(this)
						.apply { inject(this@MainActivity) }
	}


}
