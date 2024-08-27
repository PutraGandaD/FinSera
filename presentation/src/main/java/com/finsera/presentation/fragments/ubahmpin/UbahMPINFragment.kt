package com.finsera.presentation.fragments.ubahmpin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentUbahMPINBinding
import com.finsera.presentation.fragments.ubahmpin.viewmodel.UbahMPINViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class UbahMPINFragment : Fragment() {
    private var _binding : FragmentUbahMPINBinding? = null
    private val binding get() = _binding!!

    private val ubahMPINViewModel : UbahMPINViewModel by inject()

    private var pinLama = ""
    private var pinBaru = ""
    private var konfirmasiPinBaru = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUbahMPINBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnKonfirmasi.setOnClickListener {
            pinLama = binding.etPinLama.editableText.toString()
            pinBaru = binding.pinbaruEditText.editableText.toString()
            konfirmasiPinBaru = binding.konfirmasipinbaruEditText.editableText.toString()
            ubahMPINViewModel.changePin(pinLama, pinBaru, konfirmasiPinBaru)
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                ubahMPINViewModel.changePinUiState.collectLatest { uiState ->
                    uiState.message?.let {
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                        ubahMPINViewModel.messageShown()
                    }

                    if(uiState.isSuccess) {
                        findNavController().popBackStack()
                    }

                    if(uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }
}