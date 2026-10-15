package com.example.mini_project.ui.manage.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mini_project.R
import com.example.mini_project.base.collectWhenStarted
import com.example.mini_project.core.Format
import com.example.mini_project.data.model.TxType
import com.example.mini_project.databinding.FragmentManageTransactionBinding
import com.example.mini_project.ui.adapter.CategoryAdapter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.time.Instant
import java.time.ZoneOffset

abstract class BaseManageTransactionFragment : Fragment() {
    protected abstract val viewModel: BaseManageTransactionViewModel
    protected lateinit var binding: FragmentManageTransactionBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryAdapter = CategoryAdapter { viewModel.setCategory(it) }
        binding.rvCategories.adapter = categoryAdapter

        binding.btnClose.setOnClickListener { findNavController().popBackStack() }

        binding.toggleType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                viewModel.setType(if (checkedId == R.id.btnIncome) TxType.INCOME else TxType.EXPENSE)
            }
        }

        binding.btnDate.setOnClickListener { showDatePicker() }

        binding.btnSave.setOnClickListener {
            viewModel.submit(
                amountText = binding.etAmount.text.toString(),
                paidWith = binding.tilPaidWith.editText?.text?.toString().orEmpty().trim(),
                note = binding.tilNote.editText?.text?.toString().orEmpty().trim()
            )
        }

        // Category grid: live from Firestore
        collectWhenStarted(viewModel.categoryItems) { items ->
            categoryAdapter.submitList(items)
        }

        collectWhenStarted(viewModel.form) { form ->
            binding.tvDate.text = Format.fullDate(form.date)
            val checked = if (form.type == TxType.INCOME) R.id.btnIncome else R.id.btnExpense
            if (binding.toggleType.checkedButtonId != checked) binding.toggleType.check(checked)
        }

        collectWhenStarted(viewModel.amountError) { binding.etAmount.error = it }

        collectWhenStarted(viewModel.error) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }

        // Saved / updated / deleted: close the screen
        collectWhenStarted(viewModel.finish) { findNavController().popBackStack() }
    }

    private fun showDatePicker() {
        val current = viewModel.form.value.date
        val picker = MaterialDatePicker.Builder.datePicker()
            .setSelection(current.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
            .build()
        picker.addOnPositiveButtonClickListener { millis ->
            viewModel.setDate(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
        }
        picker.show(childFragmentManager, "datePicker")
    }
}