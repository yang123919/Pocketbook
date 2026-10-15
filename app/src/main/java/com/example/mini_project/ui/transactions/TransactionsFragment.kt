package com.example.mini_project.ui.transactions

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.mini_project.R
import com.example.mini_project.base.BaseFragment
import com.example.mini_project.base.collectWhenStarted
import com.example.mini_project.base.openEditTransaction
import com.example.mini_project.databinding.FragmentTransactionsBinding
import com.example.mini_project.ui.adapter.TransactionAdapter

class TransactionsFragment : BaseFragment<FragmentTransactionsBinding>(FragmentTransactionsBinding::inflate) {

    private val viewModel: TransactionsViewModel by viewModels { TransactionsViewModel.factory }
    private lateinit var adapter: TransactionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TransactionAdapter { tx -> tx.id?.let { openEditTransaction(it) } }
        binding.rvTransactions.adapter = adapter

        binding.etSearch.doAfterTextChanged { viewModel.setQuery(it?.toString().orEmpty()) }

        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val filter = when (checkedIds.firstOrNull()) {
                R.id.chipExpenses -> TxFilter.EXPENSES
                R.id.chipIncome -> TxFilter.INCOME
                else -> TxFilter.ALL
            }
            viewModel.setFilter(filter)
        }

        binding.btnSort.setOnClickListener { viewModel.toggleSort() }

        collectWhenStarted(viewModel.newestFirst) { newest ->
            binding.btnSort.rotation = if (newest) 0f else 180f
        }

        collectWhenStarted(viewModel.transactions) { list ->
            adapter.submitList(list)
            binding.tvEmpty.isVisible = list.isEmpty()
            binding.rvTransactions.isVisible = list.isNotEmpty()
        }
    }
}