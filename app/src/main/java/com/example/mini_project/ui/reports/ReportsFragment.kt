package com.example.mini_project.ui.reports

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.mini_project.R
import com.example.mini_project.base.BaseFragment
import com.example.mini_project.base.collectWhenStarted
import com.example.mini_project.core.Format
import com.example.mini_project.databinding.FragmentReportsBinding
import com.example.mini_project.ui.adapter.LegendAdapter

class ReportsFragment : BaseFragment<FragmentReportsBinding>(FragmentReportsBinding::inflate) {

    private val viewModel: ReportsViewModel by viewModels { ReportsViewModel.factory }
    private lateinit var legendAdapter: LegendAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        legendAdapter = LegendAdapter()
        binding.rvLegend.adapter = legendAdapter

        binding.togglePeriod.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            val period = when (checkedId) {
                R.id.btnWeek -> Period.WEEK
                R.id.btnYear -> Period.YEAR
                else -> Period.MONTH
            }
            viewModel.setPeriod(period)
        }

        collectWhenStarted(viewModel.report) { report ->
            binding.donutChart.setData(report.slices, Format.money(report.total))
            legendAdapter.submitList(report.slices)
            binding.tvNoData.isVisible = report.slices.isEmpty()
            binding.barChart.setData(report.dayLabels, report.dayTotals)
        }
    }
}