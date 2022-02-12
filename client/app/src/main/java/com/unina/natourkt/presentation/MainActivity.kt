package com.unina.natourkt.presentation

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unina.natourkt.R
import com.unina.natourkt.databinding.ActivityMainBinding
import com.unina.natourkt.domain.model.User
import dagger.hilt.android.AndroidEntryPoint

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
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    private var isUserAuthenticated: Boolean = false
    private var loggedUser: User? = null

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        instance = this

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        setListeners()

        checkAuthState()
    }

    /**
     * Basic settings for UI
     */
    private fun setupUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        navView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
    }

    /**
     * Check if user is authenticated, if not, then go to Login Fragment
     */
    private fun checkAuthState() {
        isUserAuthenticated = mainViewModel.isUserAuthenticated
        loggedUser = mainViewModel.loggedUser

        if (!isUserAuthenticated || loggedUser == null) {
            navController.navigate(R.id.navigation_home_to_navigation_auth_flow)
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
}