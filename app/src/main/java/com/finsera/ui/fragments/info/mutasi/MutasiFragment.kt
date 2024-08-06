package com.finsera.ui.fragments.info.mutasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.finsera.R
import com.finsera.common.utils.dialog.DatePickerFragment
import com.finsera.databinding.FragmentMutasiBinding
import com.finsera.databinding.ViewMutasiFilterBinding
import com.finsera.ui.adapters.MutasiAdapter
import com.finsera.ui.fragments.info.mutasi.viewmodel.MutasiViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MutasiFragment : Fragment(), DatePickerFragment.DialogDateListener {

    private var _binding: FragmentMutasiBinding? = null
    private var _filterViewBinding : ViewMutasiFilterBinding? = null
    private val binding get() = _binding!!
    private val filterViewBinding get() = _filterViewBinding!!

    private val mutasiViewModel: MutasiViewModel by viewModel()

    private var startDate : String = ""
    private var endDate : String = ""

    private val mutasiAdapter = MutasiAdapter()
    private var isResultMutasiShowed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMutasiBinding.inflate(inflater, container, false)
        _filterViewBinding = binding.viewFilter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
        handleBackButton()
        filterButtonOnClick()

        binding.btnDownload.setOnClickListener {
            Snackbar.make(requireView(), "Fitur download mutasi belum tersedia.", Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mutasiViewModel.mutasiUiState.collectLatest { uiState ->
                    if (uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }

                    if (uiState.message != null) {
                        Snackbar.make(requireView(), uiState.message, Snackbar.LENGTH_SHORT).show()
                        mutasiViewModel.messageShown()
                    }

                    if (uiState.mutasi.isNotEmpty()) {
                        mutasiAdapter.submitList(uiState.mutasi)
                        mutasiViewModel.dataSubmittedToAdapter()
                    }
                }
            }
        }
    }

    private fun handleBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if(isResultMutasiShowed) {
                mutasiAdapter.submitList(emptyList())
                showFilter()
            } else {
                findNavController().popBackStack()
            }
        }


        binding.btnBack.setOnClickListener {
            if(isResultMutasiShowed) {
                mutasiAdapter.submitList(emptyList())
                showFilter()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun showResultOfFilter() {
        binding.rvMutasi.visibility = View.VISIBLE
        binding.btnDownload.visibility = View.VISIBLE
        binding.viewFilter.clMutasiFilter.visibility = View.INVISIBLE

        with(binding.rvMutasi) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mutasiAdapter
        }

        isResultMutasiShowed = true
    }

    private fun showFilter() {
        binding.viewFilter.clMutasiFilter.visibility = View.VISIBLE
        binding.rvMutasi.visibility = View.INVISIBLE
        binding.btnDownload.visibility = View.INVISIBLE
        isResultMutasiShowed = false
    }

    private fun filterButtonOnClick() {
        binding.viewFilter.btnToday.setOnClickListener {
            val date = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(System.currentTimeMillis())
            mutasiViewModel.getMutasi(date, date)
            showResultOfFilter()
        }

        binding.viewFilter.btnWeek.setOnClickListener {
            val calendar = Calendar.getInstance() // get current device date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // get start date to 7 days earlier from current date
            calendar.add(Calendar.DAY_OF_YEAR, -7) // get 7 days ago date (current date - 7 days)
            val startDate = dateFormat.format(calendar.time)

            calendar.add(Calendar.DAY_OF_YEAR, 7) // reset to today's date (current date + 7 days)
            val endDate = dateFormat.format(calendar.time)

            mutasiViewModel.getMutasi(startDate, endDate)
            showResultOfFilter()
        }

        binding.viewFilter.btnMonth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val firstDayOfMonth = dateFormat.format(calendar.time)

            calendar.set(
                Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            val lastDayOfMonth = dateFormat.format(calendar.time)

            mutasiViewModel.getMutasi(firstDayOfMonth, lastDayOfMonth)
            showResultOfFilter()
        }

        binding.viewFilter.clStartDate.setOnClickListener {
            showDatePicker("startDatePicker")
        }

        binding.viewFilter.cleEndDate.setOnClickListener {
            showDatePicker("endDatePicker")
        }

        filterViewBinding.btnNext.setOnClickListener {
            if (startDate.isEmpty() || endDate.isEmpty()) {
                Snackbar.make(
                    requireView(),
                    "Silahkan atur tanggal awal dan tanggal akhir terlebih dahulu",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (startDate > endDate) {
                Snackbar.make(
                    requireView(),
                    "Tanggal awal harus lebih awal dari tanggal akhir",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                mutasiViewModel.getMutasi(
                    formatDateString(startDate), formatDateString(endDate)
                )
                showResultOfFilter()
            } else {
                mutasiViewModel.getMutasi(
                    "", ""
                )
                showResultOfFilter()
            }
        }
    }

    private fun formatDateString(dateString: String): String {
        val inputFormatter = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        val outputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = inputFormatter.parse(dateString)
        return outputFormatter.format(date!!)
    }

    private fun showDatePicker(tag: String?) {
        val newFragment = DatePickerFragment()
        newFragment.setDialogDateListener(this)
        newFragment.show(parentFragmentManager, tag)
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, day: Int) {
        if (tag.equals("startDatePicker")) {
            binding.viewFilter.tvStartDateValue.text = "$day-${month + 1}-$year"
            startDate = "$year-${month + 1}-$day"
        } else {
            binding.viewFilter.tvEndDateValue.text = "$day-${month + 1}-$year"
            endDate = "$year-${month + 1}-$day"
        }
    }

}
