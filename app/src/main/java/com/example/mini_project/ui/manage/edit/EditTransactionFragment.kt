package com.example.mini_project.ui.manage.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.mini_project.R
import com.example.mini_project.ui.manage.base.BaseManageTransactionFragment

class EditTransactionFragment : BaseManageTransactionFragment() {

    override val viewModel: EditTransactionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.setText(R.string.edit_title)
        binding.btnSave.setText(R.string.update)
        binding.btnDelete.isVisible = true
    }
}