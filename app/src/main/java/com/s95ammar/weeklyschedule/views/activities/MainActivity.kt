package com.s95ammar.weeklyschedule.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.s95ammar.weeklyschedule.App
import com.s95ammar.weeklyschedule.R
import com.s95ammar.weeklyschedule.di.main.MainActivitySubcomponent
import com.s95ammar.weeklyschedule.util.addFragment
import com.s95ammar.weeklyschedule.util.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.s95ammar.weeklyschedule.viewModels.MainViewModel
import javax.inject.Inject


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var component: MainActivitySubcomponent
    private lateinit var viewModel: MainViewModel
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val t = "log_${javaClass.simpleName}"

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
        setSupportActionBar(toolbar_main_activity as Toolbar)
    }

    private fun startObservers() {

    }

    private fun initComponent() {
        component =
            (application as App).component.getMainActivityComponentFactory().create(this).apply {
                inject(this@MainActivity)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        setUpNavDrawer(toolbar_main_activity as Toolbar)
        return true
    }


    override fun onNavigationItemSelected(@NonNull menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_active_schedule -> {
                replaceFragment(component.getScheduleViewerFragment(), view_fragment_container_main_activity)
            }
            R.id.nav_schedules -> {
                replaceFragment(component.getSchedulesFragment(), view_fragment_container_main_activity)
            }
            R.id.nav_categories -> {
                replaceFragment(component.getCategoriesFragment(), view_fragment_container_main_activity)
            }
            R.id.nav_info -> {
                addFragment(component.getScheduleViewerFragment(), view_fragment_container_main_activity, true)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun setUpNavDrawer(toolbar: Toolbar) {
        nav_view.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                supportActionBar?.setHomeButtonEnabled(false)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)

                toggle.isDrawerIndicatorEnabled = true
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                toolbar.setNavigationOnClickListener { drawer_layout.openDrawer(GravityCompat.START) }

            } else {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                toggle.isDrawerIndicatorEnabled = false
                supportActionBar?.setHomeButtonEnabled(true)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
//                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_cancel)
                toolbar.setNavigationOnClickListener { onBackPressed() }

            }
        }

/*
        if (scheduleViewerFragment.isVisible) {
            nav_view.menu.getItem(ACTIVE_SCHEDULE).isChecked = true
        } else if (schedulesListFragment.isVisible) { //TODO: make sure this works
            nav_view.menu.getItem(SCHEDULES).isChecked = true
        }
*/
    }


}
