package com.example.mini_project.ui.transactions

import androidx.fragment.app.viewModels
import com.example.mini_project.base.BaseFragment
import com.example.mini_project.databinding.FragmentTransactionsBinding

class TransactionsFragment : BaseFragment<FragmentTransactionsBinding>(FragmentTransactionsBinding::inflate) {

    private val viewModel: TransactionsViewModel by viewModels()
}