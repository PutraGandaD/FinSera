package com.finsera.ui.fragments.info.mutasi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.finsera.R
import com.finsera.common.utils.Resource
import com.finsera.common.utils.dialog.DatePickerFragment
import com.finsera.databinding.FragmentMutasiBinding
import com.finsera.ui.adapters.MutasiAdapter
import com.finsera.ui.fragments.info.mutasi.viewmodel.MutasiViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MutasiFragment : Fragment(), DatePickerFragment.DialogDateListener {

    private var _binding: FragmentMutasiBinding? = null
    private val binding get() = _binding!!

    private val mutasiViewModel: MutasiViewModel by viewModel()


    private lateinit var startDate: String
    private lateinit var endDate: String

    private val mutasiAdapter = MutasiAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMutasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnFilter.setOnClickListener {
            showFilter()
        }

        binding.btnHistory.setOnClickListener {
            showHistory()
        }


        binding.viewFilter.btnToday.setOnClickListener {
            val date = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(System.currentTimeMillis())
            mutasiViewModel.getMutasi(date, date, 1, 5)
            showResultOfFilter()
        }

        binding.viewFilter.btnWeek.setOnClickListener {
            val currentDate = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val todayDate = dateFormat.format(currentDate.time)
            currentDate.add(Calendar.DAY_OF_YEAR, 7)
            val nextWeekDate = dateFormat.format(currentDate.time)

            mutasiViewModel.getMutasi(todayDate, nextWeekDate, 1, 5)
            showResultOfFilter()
        }


        binding.viewFilter.btnMonth.setOnClickListener {
            val currentDate = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            currentDate.set(Calendar.DAY_OF_MONTH, 1)
            val firstDayOfMonth = dateFormat.format(currentDate.time)

            currentDate.set(
                Calendar.DAY_OF_MONTH,
                currentDate.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            val lastDayOfMonth = dateFormat.format(currentDate.time)

            mutasiViewModel.getMutasi(firstDayOfMonth, lastDayOfMonth, 1, 5)
            showResultOfFilter()
        }



        binding.viewFilter.clStartDate.setOnClickListener {
            showDatePicker("starDatePicker")
        }

        binding.viewFilter.cleEndDate.setOnClickListener {
            showDatePicker("endDatePicker")
        }

        binding.viewFilter.btnNext.setOnClickListener {
            if (startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select start date and end date",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (startDate > endDate) {
                Toast.makeText(
                    requireContext(),
                    "Start date must be less than end date",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                mutasiViewModel.getMutasi(
                    formatDateString(startDate), formatDateString(endDate), 1, 5
                )
                showResultOfFilter()
            } else {
                mutasiViewModel.getMutasi(
                    "", "", 1, 5
                )
                showResultOfFilter()
            }
        }


        mutasiViewModel.mutasi.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    mutasiAdapter.submitList(resource.data)
                }

                is Resource.Error -> {
                    // Show the error message
                }
            }
        }


        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mutasiViewModel.mutasiUiState.collectLatest { uiState ->
                    if (uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }

                    if (uiState.message != null) {
                        Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()
                    }
                    mutasiAdapter.submitList(uiState.mutasi)
                }
            }
        }

    }

    private fun showResultOfFilter() {
        binding.rvMutasi.visibility = View.VISIBLE
        binding.btnDownload.visibility = View.VISIBLE
        binding.viewFilter.clMutasiFilter.visibility = View.GONE

        with(binding.rvMutasi) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mutasiAdapter
        }
    }

    private fun showDatePicker(tag: String?) {
        val newFragment = DatePickerFragment()
        newFragment.setDialogDateListener(this)
        newFragment.show(parentFragmentManager, tag)
    }

    private fun showHistory() {

        binding.viewFilter.clMutasiFilter.visibility = View.GONE
        binding.rvMutasi.visibility = View.VISIBLE
        binding.btnDownload.visibility = View.VISIBLE

        //set color
        binding.btnFilter.setBackgroundColor(resources.getColor(R.color.neutral_03))
        binding.btnHistory.setBackgroundColor(resources.getColor(R.color.primary_blue))
    }

    private fun showFilter() {
        binding.viewFilter.clMutasiFilter.visibility = View.VISIBLE
        binding.rvMutasi.visibility = View.GONE
        binding.btnDownload.visibility = View.GONE

        //set color
        binding.btnFilter.setBackgroundColor(resources.getColor(R.color.primary_blue))
        binding.btnHistory.setBackgroundColor(resources.getColor(R.color.neutral_03))
    }

    fun formatDateString(dateString: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val outputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = inputFormatter.parse(dateString)
        return outputFormatter.format(date!!)
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, day: Int) {
        if (tag.equals("starDatePicker")) {
            binding.viewFilter.tvStartDateValue.text = "$day-${month + 1}-$year"
            startDate = "$year-${month + 1}-$day"
        } else {
            binding.viewFilter.tvEndDateValue.text = "$day-${month + 1}-$year"
            endDate = "$year-${month + 1}-$day"
        }
    }

}
