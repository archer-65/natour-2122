package com.unina.natourkt.core.presentation.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unina.natourkt.R
import com.unina.natourkt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Container activity for all fragments
 * with a companion object used by [com.unina.natourkt.domain.repository.AuthRepository]
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //private lateinit var firebaseAnalytics: FirebaseAnalytics

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

        if (savedInstanceState == null) {
            mainViewModel.sessionStarted()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlaces()
        setupUi()
        setListeners()
        checkAuthState()
        collect()
    }

    /**
     * Basic settings for UI
     */
    private fun setupUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)

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

    private fun collect() = with(mainViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiState.collectLatest {
                    if (it.user?.isAdmin == false) {
                        navView.menu.removeItem(R.id.navigation_admin_board)
                    }
                }
            }
        }
    }

    /**
     * Check if user is authenticated, if not, then go to Login Fragment
     */
    private fun checkAuthState() = with(mainViewModel) {
        val user = loggedUser

        if (!isUserAuthenticated || user == null) {
            if (navController.currentDestination?.id == R.id.navigation_home) {
                navController.navigate(R.id.action_home_to_auth_flow)
            }
        }

        user?.let { if (!it.isAdmin) navView.menu.removeItem(R.id.navigation_admin_board) }
    }

    /**
     * Function to set listeners for views
     */
    private fun setListeners() {
        navController.addOnDestinationChangedListener { navController, destination, _ ->
            when (destination.id) {
                R.id.navigation_routes -> navView.visibility = View.VISIBLE
                R.id.navigation_home -> {
                    navView.visibility = View.VISIBLE
                    mainViewModel.updateLoggedUser()
                }
                R.id.navigation_profile -> navView.visibility = View.VISIBLE
                R.id.navigation_chat_list -> navView.visibility = View.VISIBLE
                R.id.navigation_admin_board -> navView.visibility = View.VISIBLE
                R.id.saveIntoCompilationDialog -> {
                    if (navController.previousBackStackEntry?.destination?.id == R.id.navigation_routes) {
                        navView.visibility = View.VISIBLE
                    }
                }
                R.id.reportPostDialog -> {
                    if (navController.previousBackStackEntry?.destination?.id == R.id.navigation_home) {
                        navView.visibility = View.VISIBLE
                    }
                }
                else -> navView.visibility = View.GONE
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            mainViewModel.destinationChanged(destination.id)
        }
    }

    private fun initPlaces() {
        val apiKey = resources.getString(R.string.MAPS_API_KEY)
        Places.initialize(applicationContext, apiKey)
        Places.createClient(this)
    }
}