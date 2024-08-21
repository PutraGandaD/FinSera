package com.finsera.presentation.fragments.qris

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.databinding.FragmentQrisShareBinding
import com.finsera.presentation.fragments.qris.viewmodel.QrisShareViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class QrisShareFragment : Fragment() {
    private var _binding: FragmentQrisShareBinding? = null
    private val binding get() = _binding!!

    private val qrisShareViewModel : QrisShareViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrisShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        observer()
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                qrisShareViewModel.shareQrUiState.collectLatest { uiState ->
                    uiState.message?.let {
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT)
                            .setAction("Coba Lagi") {
                                qrisShareViewModel.getShareQR()
                            }
                            .show()
                        qrisShareViewModel.messageShown()
                    }

                    if(uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }

                    if(uiState.isSuccess) {
                        generateQRCode(uiState.data?.json.toString())
                    }
                }
            }
        }
    }

    private fun generateQRCode(text: String) {
        val barcodeEncoder = BarcodeEncoder()
        try {
            //Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
            val bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400)
            binding.ivQrcode.setImageBitmap(bitmap) // Sets the Bitmap to ImageView
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}