package com.example.mini_project

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mini_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val navHost = supportFragmentManager.findFragmentById(binding.navHost.id) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNav.setupWithNavController(navController)
        binding.fabAdd.setOnClickListener { navController.navigate(R.id.action_global_add) }

        val tabs = setOf(R.id.homeFragment, R.id.transactionsFragment, R.id.reportsFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val onTab = destination.id in tabs
            binding.bottomNav.isVisible = onTab
            binding.fabAdd.isVisible = onTab
        }
    }
}