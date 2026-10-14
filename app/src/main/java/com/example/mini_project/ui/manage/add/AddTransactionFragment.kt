package com.example.mini_project.ui.manage.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.mini_project.R
import com.example.mini_project.ui.manage.base.BaseManageTransactionFragment

class AddTransactionFragment : BaseManageTransactionFragment() {

    override val viewModel: AddTransactionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.setText(R.string.add_title)
        binding.btnSave.setText(R.string.save)
    }
}