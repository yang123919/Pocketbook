package com.example.mini_project.ui.auth.register

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.mini_project.R
import com.example.mini_project.base.BaseFragment
import com.example.mini_project.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.authHeader.tvTitle.setText(R.string.register_title)
        binding.authHeader.tvSubtitle.setText(R.string.register_subtitle)

        binding.tvSwitchMode.setOnClickListener {
            findNavController().navigateUp()
        }
        // btnRegister and Firebase come in the next step
    }
}