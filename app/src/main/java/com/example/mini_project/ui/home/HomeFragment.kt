package com.example.mini_project.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mini_project.R
import com.example.mini_project.base.BaseFragment
import com.example.mini_project.base.collectWhenStarted
import com.example.mini_project.base.openEditTransaction
import com.example.mini_project.core.Format
import com.example.mini_project.databinding.FragmentHomeBinding
import com.example.mini_project.ui.adapter.TransactionAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels { HomeViewModel.factory }
    private lateinit var adapter: TransactionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TransactionAdapter { tx -> tx.id?.let { openEditTransaction(it) } }
        binding.rvRecent.adapter = adapter

        binding.tvGreeting.text = viewModel.greeting
        binding.tvName.text = viewModel.userName
        binding.tvAvatar.text = viewModel.userName.take(1).uppercase()

        binding.tvAvatar.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout") { _, _ ->
                    viewModel.logout()
                    findNavController().navigate(R.id.action_global_login)
                }
                .show()
        }

        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.transactionsFragment)
        }

        collectWhenStarted(viewModel.summary) { s ->
            binding.tvBalanceLabel.text = getString(R.string.balance_in, s.monthName)
            binding.tvBalance.text = Format.money(s.balance)
            binding.tvSpent.text = getString(R.string.spent_amount, Format.money(s.spent))
            binding.tvOfIncome.text = getString(R.string.of_income, Format.money(s.income))
            binding.tvIncome.text = "+" + Format.money(s.income)
            binding.tvExpenses.text = "-" + Format.money(s.spent)
            binding.progressSpent.progress = s.progressPercent
        }

        collectWhenStarted(viewModel.recent) { list ->
            adapter.submitList(list)
            binding.tvEmpty.isVisible = list.isEmpty()
            binding.rvRecent.isVisible = list.isNotEmpty()
        }
    }
}