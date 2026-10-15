package com.example.mini_project.ui.manage.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.mini_project.R
import com.example.mini_project.base.collectWhenStarted
import com.example.mini_project.core.Format
import com.example.mini_project.ui.manage.base.BaseManageTransactionFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditTransactionFragment : BaseManageTransactionFragment() {

    override val viewModel: EditTransactionViewModel by viewModels { EditTransactionViewModel.factory }

    private val transactionId: String
        get() = requireArguments().getString("transactionId").orEmpty()

    private var filled = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.setText(R.string.edit_title)
        binding.btnSave.setText(R.string.update)
        binding.btnDelete.isVisible = true

        binding.btnDelete.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_title)
                .setMessage(R.string.delete_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete) { _, _ -> viewModel.delete() }
                .show()
        }

        viewModel.load(transactionId)

        // Fill the text boxes once when the transaction arrives
        collectWhenStarted(viewModel.transaction) { tx ->
            if (tx == null || filled) return@collectWhenStarted
            binding.etAmount.setText(Format.plainAmount(tx.amount))
            binding.tilPaidWith.editText?.setText(tx.paidWith)
            binding.tilNote.editText?.setText(tx.note)
            filled = true
        }
    }
}