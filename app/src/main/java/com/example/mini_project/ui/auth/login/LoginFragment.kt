package com.example.mini_project.ui.auth.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.mini_project.R
import com.example.mini_project.base.BaseFragment
import com.example.mini_project.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authHeader.tvTitle.setText(R.string.login_title)
        binding.authHeader.tvSubtitle.setText(R.string.login_subtitle)

        binding.tvSwitchMode.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
        // btnLogin and Firebase come in the next step
    }
}