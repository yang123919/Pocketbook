package com.example.mini_project.ui.auth.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mini_project.R
import com.example.mini_project.base.BaseFragment
import com.example.mini_project.databinding.FragmentRegisterBinding
import com.example.mini_project.ui.auth.base.observeAuth

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authHeader.tvTitle.setText(R.string.register_title)
        binding.authHeader.tvSubtitle.setText(R.string.register_subtitle)

        binding.btnRegister.setOnClickListener {
            viewModel.register(
                name = binding.tilName.editText?.text?.toString().orEmpty().trim(),
                email = binding.credentials.tilEmail.editText?.text?.toString().orEmpty().trim(),
                pass = binding.credentials.tilPassword.editText?.text?.toString().orEmpty(),
                confirm = binding.tilConfirm.editText?.text?.toString().orEmpty()
            )
        }

        binding.tvSwitchMode.setOnClickListener { findNavController().navigateUp() }

        // After registering the user is already signed in, so this goes straight to Home
        observeAuth(viewModel, binding.progress, binding.btnRegister)
    }
}