package com.example.mini_project.ui.auth.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mini_project.R
import com.example.mini_project.base.BaseFragment
import com.example.mini_project.core.AuthService
import com.example.mini_project.databinding.FragmentLoginBinding
import com.example.mini_project.ui.auth.base.observeAuth

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Already signed in: skip Login
        if (AuthService.isUserLoggedIn()) {
            findNavController().navigate(R.id.action_global_home)
            return
        }

        binding.authHeader.tvTitle.setText(R.string.login_title)
        binding.authHeader.tvSubtitle.setText(R.string.login_subtitle)

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                email = binding.credentials.tilEmail.editText?.text?.toString().orEmpty().trim(),
                pass = binding.credentials.tilPassword.editText?.text?.toString().orEmpty()
            )
        }

        binding.tvSwitchMode.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        observeAuth(viewModel, binding.progress, binding.btnLogin)
    }
}