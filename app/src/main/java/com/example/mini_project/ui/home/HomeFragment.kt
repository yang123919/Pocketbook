package com.example.mini_project.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mini_project.R
import com.example.mini_project.base.BaseFragment
import com.example.mini_project.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.transactionsFragment)
        }
    }
}