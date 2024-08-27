package com.finsera.presentation.fragments.info.mutasi

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Resource
import com.finsera.common.utils.dialog.DatePickerFragment
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.common.utils.permission.HandlePermission.openAppPermissionSettings
import com.finsera.common.utils.permission.HandlePermission.openAppStoragePermissionR
import com.finsera.domain.model.Mutasi
import com.finsera.presentation.R
import com.finsera.presentation.adapters.MutasiAdapter
import com.finsera.presentation.databinding.FragmentMutasiBinding
import com.finsera.presentation.databinding.ViewMutasiFilterBinding
import com.finsera.presentation.fragments.info.mutasi.viewmodel.MutasiViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MutasiFragment() : Fragment(), DatePickerFragment.DialogDateListener {
    private var _binding: FragmentMutasiBinding? = null
    private var _filterViewBinding : ViewMutasiFilterBinding? = null
    private val binding get() = _binding!!
    private val filterViewBinding get() = _filterViewBinding!!

    private val mutasiViewModel: MutasiViewModel by viewModel()
    private val connectivityManager: ConnectivityManager by inject()

    private var startDate : String = ""
    private var endDate : String = ""

    private val mutasiAdapter = MutasiAdapter()
    private var isResultMutasiShowed = false

    private var PAGE_COUNT_REQUEST = 1

    private var isBackPressed = false

    private var hasAnnouncedScreen = false
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
        setUpRvMutasi()
        setUserInfo()
        handleScrollPagination()

        binding.btnDownload.setOnClickListener {
            requestStoragePermission()
        }

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_mutasi_transaksi))
            hasAnnouncedScreen = true
        }
    }

    private fun setUpRvMutasi() {
        binding.rvMutasi.adapter = mutasiAdapter
    }

    private fun handleScrollPagination() {
        binding.nestedRvMutasi.viewTreeObserver.addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                if (!isBackPressed) {
                    val diff: Int =
                        (binding.nestedRvMutasi.getChildAt(binding.nestedRvMutasi.getChildCount() - 1).bottom - (binding.nestedRvMutasi.getHeight() + binding.nestedRvMutasi
                            .getScrollY()))

                    if (diff == 0) {
                        PAGE_COUNT_REQUEST++
                        mutasiViewModel.getMutasi(startDate, endDate, PAGE_COUNT_REQUEST)
                    }
                }
            }
        })
    }

    private fun setUserInfo() {
        binding.tvAccountNumberValue.text = mutasiViewModel.userInfo?.second
        val accountNumber = mutasiViewModel.userInfo?.second ?: ""
        binding.tvAccountNumberValue.text = accountNumber
        val formattedAccountNumber = formatAccountNumberForTalkBack(accountNumber)
        binding.tvAccountNumberValue.contentDescription = getString(R.string.account_number_label) + " " + formattedAccountNumber
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mutasiViewModel.mutasiUiState.collectLatest { uiState ->
                    if(uiState.isLoading) {
                        binding.progressBarMutasiItem.visibility = View.VISIBLE
                    }


                    if(uiState.isError) {
                        when{
                            PAGE_COUNT_REQUEST > 1 && uiState.message == "Transaksi tidak ditemukan" -> {
                                binding.progressBarMutasiItem.visibility = View.GONE
                                Snackbar.make(requireView(), "Anda sudah mencapai mutasi terakhir di tanggal ini", Snackbar.LENGTH_SHORT).show()
                            }
                            else -> {
                                Snackbar.make(requireView(), uiState.message.toString(), Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        mutasiViewModel.resetUiState()
                    }

                    if(uiState.isSuccess && PAGE_COUNT_REQUEST == 1) {
                        mutasiAdapter.submitList(uiState.mutasi)
                        Snackbar.make(requireView(), uiState.message.toString(), Snackbar.LENGTH_SHORT).show()
                        mutasiViewModel.resetUiState()
                    }

                    if(uiState.isSuccess && PAGE_COUNT_REQUEST > 1) {
                        addNewDataToRv(uiState.mutasi)
                        mutasiViewModel.resetUiState()
                    }
                }
            }
        }
    }

    private fun addNewDataToRv(newUsers: List<Mutasi>?) {
        val currentList = mutasiAdapter.currentList.toMutableList() // copy current data from rv
        currentList.addAll(newUsers ?: emptyList()) // add data
        mutasiAdapter.submitList(currentList) // submit latest data
    }

    private fun handleBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if(isResultMutasiShowed) {
                mutasiAdapter.submitList(null)
                resetAllInput()
                showFilter()
            } else {
                findNavController().popBackStack()
            }
        }

        binding.btnBack.setOnClickListener {
            if(isResultMutasiShowed) {
                mutasiAdapter.submitList(null)
                resetAllInput()
                showFilter()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun resetAllInput() {
        startDate = "" // reset startdate and enddate
        endDate = ""
        PAGE_COUNT_REQUEST = 1
        binding.viewFilter.tvStartDateValue.text = "DD-MM-YYYY"
        binding.viewFilter.tvEndDateValue.text = "DD-MM-YYYY"
    }

    private fun showResultOfFilter() {
        isResultMutasiShowed = true
        isBackPressed = false

        binding.layoutRvMutasi.visibility = View.VISIBLE
        binding.btnDownload.visibility = View.VISIBLE
        binding.viewFilter.clMutasiFilter.visibility = View.INVISIBLE
    }

    private fun showFilter() {
        binding.viewFilter.clMutasiFilter.visibility = View.VISIBLE
        binding.layoutRvMutasi.visibility = View.INVISIBLE
        binding.btnDownload.visibility = View.INVISIBLE

        isResultMutasiShowed = false
        isBackPressed = true
    }

    private fun filterButtonOnClick() {
        binding.viewFilter.btnToday.setOnClickListener {
            if(connectivityManager.hasInternetConnection()) {
                val date = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(System.currentTimeMillis())

                startDate = date
                endDate = date
                mutasiViewModel.getMutasi(date, date, PAGE_COUNT_REQUEST)
                showResultOfFilter()
            } else {
                Snackbar.make(requireView(), "Tidak ada koneksi internet", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.viewFilter.btnWeek.setOnClickListener {
            if(connectivityManager.hasInternetConnection()) {
                val calendar = Calendar.getInstance() // get current device date
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                // get start date to 7 days earlier from current date
                calendar.add(Calendar.DAY_OF_YEAR, -7) // get 7 days ago date (current date - 7 days)
                startDate = dateFormat.format(calendar.time)

                calendar.add(Calendar.DAY_OF_YEAR, 7) // reset to today's date (current date + 7 days)
                endDate = dateFormat.format(calendar.time)

                mutasiViewModel.getMutasi(startDate, endDate, PAGE_COUNT_REQUEST)
                showResultOfFilter()
            } else {
                Snackbar.make(requireView(), "Tidak ada koneksi internet", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.viewFilter.btnMonth.setOnClickListener {
            if(connectivityManager.hasInternetConnection()) {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                calendar.set(Calendar.DAY_OF_MONTH, PAGE_COUNT_REQUEST)
                val firstDayOfMonth = dateFormat.format(calendar.time)

                calendar.set(
                    Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                )
                val lastDayOfMonth = dateFormat.format(calendar.time)

                startDate = firstDayOfMonth
                endDate = lastDayOfMonth

                mutasiViewModel.getMutasi(firstDayOfMonth, lastDayOfMonth, PAGE_COUNT_REQUEST)
                showResultOfFilter()
            } else {
                Snackbar.make(requireView(), "Tidak ada koneksi internet", Snackbar.LENGTH_SHORT).show()
            }
        }


        binding.viewFilter.clStartDate.setOnClickListener {
            showDatePicker("startDatePicker")
        }

        binding.viewFilter.cleEndDate.setOnClickListener {
            showDatePicker("endDatePicker")
        }

        filterViewBinding.btnNext.setOnClickListener {
            if(connectivityManager.hasInternetConnection()) {
                if (startDate.isEmpty() || endDate.isEmpty()) {
                    Snackbar.make(
                        requireView(),
                        "Silahkan atur tanggal awal dan tanggal akhir terlebih dahulu",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (formatDateString(startDate) > formatDateString(endDate)) {
                    Snackbar.make(
                        requireView(),
                        "Tanggal awal harus lebih awal dari tanggal akhir",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                    mutasiViewModel.getMutasi(
                        formatDateString(startDate), formatDateString(endDate), PAGE_COUNT_REQUEST
                    )
                    showResultOfFilter()
                } else {
                    mutasiViewModel.getMutasi(
                        "", "", PAGE_COUNT_REQUEST
                    )
                    showResultOfFilter()
                }
            } else {
                Snackbar.make(requireView(), "Tidak ada koneksi internet", Snackbar.LENGTH_SHORT).show()
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

    private fun requestStoragePermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    downloadFile()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    // Provide an additional rationale to the user if the permission was not granted
                    // and the user would benefit from additional context for the use of the permission.
                    permissionStorageDialog()
                }

                else -> {
                    // Request the permission
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        } else {
            if(!Environment.isExternalStorageManager()) {
                requireActivity().openAppStoragePermissionR()
                Toast.makeText(requireActivity(), "Mohon hidupkan akses penyimpanan pada aplikasi anda di halaman ini", Toast.LENGTH_LONG).show()
            } else {
                downloadFile()
            }
        }

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue with the operation
            downloadFile()
        } else {
            // Explain to the user that the feature is unavailable because the
            // features require a permission that the user has denied.
            permissionStorageDialog()
        }
    }

    private fun downloadFile() {
        mutasiViewModel.downloadMutasiFile(startDate, endDate)

        mutasiViewModel.downloadState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    saveFileToDisk(resource.data)
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), resource.message!!, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    private fun saveFileToDisk(body: ResponseBody?) {
        if (body == null) return

        val timestamp = System.currentTimeMillis()
        val values = ContentValues()
        var uri: Uri? = null
        val fileName = "mutasi_${startDate}-${endDate}.pdf"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Save to Download/Finsera directory for Android 11 and above
            values.put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            values.put(MediaStore.Downloads.DATE_ADDED, timestamp)
            values.put(MediaStore.Downloads.DATE_MODIFIED, timestamp)
            values.put(MediaStore.Downloads.RELATIVE_PATH, "Download/" + getString(R.string.app_name))
            values.put(MediaStore.Downloads.IS_PENDING, true)

            uri = requireActivity().contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                try {
                    val outputStream = requireActivity().contentResolver.openOutputStream(uri)
                    if (outputStream != null) {
                        try {
                            val inputStream: InputStream = body.byteStream()
                            val buffer = ByteArray(4096)
                            var bytesRead: Int

                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                outputStream.write(buffer, 0, bytesRead)
                            }

                            outputStream.flush()
                            inputStream.close()
                            outputStream.close()

                            values.put(MediaStore.Downloads.IS_PENDING, false)
                            requireActivity().contentResolver.update(uri, values, null, null)

                            Toast.makeText(requireContext(), "File berhasil disimpan di folder Download/${getString(R.string.app_name)}", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Log.e(TAG, "savePdfFile: ", e)
                            Toast.makeText(requireContext(), "Gagal menyimpan file", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "savePdfFile: ", e)
                    Toast.makeText(requireContext(), "Gagal menyimpan file", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // save directly to external storage for Android 10 and below
            val pdfFolder = File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name))
            if (!pdfFolder.exists()) {
                pdfFolder.mkdirs()
            }
            val file = File(pdfFolder, fileName)

            try {
                val outputStream: OutputStream = FileOutputStream(file)
                val inputStream: InputStream = body.byteStream()
                val buffer = ByteArray(4096)
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                outputStream.flush()
                inputStream.close()
                outputStream.close()

                // Notify media scanner about the new file
                values.put(MediaStore.MediaColumns.DATA, file.absolutePath)
                uri = requireActivity().contentResolver.insert(MediaStore.Files.getContentUri("external"), values)

                Toast.makeText(requireContext(), "File berhasil disimpan di folder ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "savePdfFile: ", e)
                Toast.makeText(requireContext(), "Gagal menyimpan file", Toast.LENGTH_SHORT).show()
            }
        }

        triggerShareDialog(uri)
    }

    private fun triggerShareDialog(uri: Uri?) {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            // Example: content://com.google.android.apps.photos.contentprovider/...
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "application/pdf"
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }

    private fun formatAccountNumberForTalkBack(accountNumber: String): String {
        return accountNumber.map { it.toString() }.joinToString(" ")
    }

    private fun permissionStorageDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Izin Aplikasi FinSera")
            .setMessage(resources.getString(R.string.izin_penyimpanan_aplikasi_finsera_desc))
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
                Snackbar.make(requireView(), "Fitur tidak dapat dijalankan karena izin penyimpanan file pada aplikasi FinSera tidak diizinkan", Snackbar.LENGTH_SHORT).show()
            }
            .setPositiveButton("Ya") { dialog, which ->
                requireActivity().openAppPermissionSettings()
            }
            .show()
    }

}
