package com.unina.natourkt.core.presentation.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unina.natourkt.R
import com.unina.natourkt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter

/**
 * Container activity for all fragments
 * with a companion object used by [com.unina.natourkt.domain.repository.AuthRepository]
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var instance: MainActivity
            private set
    }

    private lateinit var binding: ActivityMainBinding

    // NavView and NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    // ViewModel
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        instance = this

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlaces()
        setupUi()
        setListeners()
        checkAuthState()
    }

    /**
     * Basic settings for UI
     */
    private fun setupUi() {
        navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragmentActivityMain.id) as NavHostFragment

        navController = navHostFragment.navController

        navView = binding.navView
        navView.setupWithNavController(navController)

        navView.applyInsetter {
            type(navigationBars = true) {
                margin()
            }
        }
    }

    /**
     * Check if user is authenticated, if not, then go to Login Fragment
     */
    private fun checkAuthState() = with(mainViewModel) {
        val isUserAuthenticated = isUserAuthenticated
        val loggedUser = loggedUser

        if (!isUserAuthenticated || loggedUser == null) {
            if (navController.currentDestination?.id == R.id.navigation_home) {
                navController.navigate(R.id.action_home_to_auth_flow)
            }
        }
    }

    /**
     * Function to set listeners for views
     */
    private fun setListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_routes -> navView.visibility = View.VISIBLE
                R.id.navigation_home -> navView.visibility = View.VISIBLE
                R.id.navigation_profile -> navView.visibility = View.VISIBLE
                else -> navView.visibility = View.GONE
            }
        }
    }

    private fun initPlaces() {
        val apiKey = resources.getString(R.string.MAPS_API_KEY)
        Places.initialize(applicationContext, apiKey)
        Places.createClient(this)
    }
}