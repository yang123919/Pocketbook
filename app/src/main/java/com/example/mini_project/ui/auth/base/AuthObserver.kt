package com.example.mini_project.ui.auth.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.mini_project.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/** Used by Login and Register: progress bar, error snackbar, go Home on success. */
fun Fragment.observeAuth(viewModel: BaseAuthViewModel, progress: View, button: View) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.loading.collect { loading ->
                    progress.visibility = if (loading) View.VISIBLE else View.GONE
                    button.isEnabled = !loading
                }
            }
            launch {
                viewModel.error.collect { message ->
                    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
                }
            }
            launch {
                viewModel.success.collect {
                    findNavController().navigate(R.id.action_global_home)
                }
            }
        }
    }
}