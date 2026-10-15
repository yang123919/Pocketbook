package com.example.mini_project.base

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.mini_project.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/** Collect a Flow only while the screen is visible (safe with ViewBinding). */
fun <T> Fragment.collectWhenStarted(flow: Flow<T>, action: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { action(it) }
        }
    }
}

/** Opens the Edit screen for one transaction (used by Home and Transactions). */
fun Fragment.openEditTransaction(id: String) {
    findNavController().navigate(R.id.action_global_edit, bundleOf("transactionId" to id))
}