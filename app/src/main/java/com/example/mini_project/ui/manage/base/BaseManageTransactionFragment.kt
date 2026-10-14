package com.example.mini_project.ui.manage.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mini_project.databinding.FragmentManageTransactionBinding

abstract class BaseManageTransactionFragment : Fragment() {
    protected abstract val viewModel: BaseManageTransactionViewModel
    protected lateinit var binding: FragmentManageTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener { findNavController().popBackStack() }
    }
}