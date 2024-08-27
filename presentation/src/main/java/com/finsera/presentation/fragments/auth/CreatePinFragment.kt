package com.finsera.presentation.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentCreatePinBinding
import com.finsera.presentation.fragments.auth.viewmodels.LoginPinViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel

class CreatePinFragment : Fragment() {
    private var _binding: FragmentCreatePinBinding? = null
    private val binding get() = _binding!!

    private val loginPinViewModel : LoginPinViewModel by inject()

    private var hasAnnouncedScreen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Snackbar.make(requireView(), "Silahkan buat PIN Aplikasi terlebih dahulu untuk melanjutkan!", Snackbar.LENGTH_SHORT).show()

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_create_pin))
            hasAnnouncedScreen = true
        }

        binding.btnBuatPin.setOnClickListener {
            val newPinEt = binding.etPinBaru
            val confirmNewPinEt = binding.etKonfirmasiPinBaru

            val newPin = newPinEt.editText?.text.toString()
            val confirmNewPin = confirmNewPinEt.editText?.text.toString()

            if(validateNoEmptyColumn(newPin, confirmNewPin)) {
                if(newPin.length == 6) {
                    if(loginPinViewModel.createAppPin(newPin, confirmNewPin)) {
                        Snackbar.make(requireView(), "PIN berhasil dibuat. Silakan login dengan PIN Anda untuk melanjutkan!", Snackbar.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_createPinFragment_to_loginPinFragment)
                    } else {
                        Snackbar.make(requireView(), "PIN Baru dengan Konfirmasi PIN tidak sama! Silahkan masukkan ulang!", Snackbar.LENGTH_SHORT).show()
                        newPinEt.editText?.text?.clear()
                        confirmNewPinEt.editText?.text?.clear()
                    }
                } else {
                    Snackbar.make(requireView(), "PIN harus berjumlah 6 digit! Silahkan masukkan PIN baru.", Snackbar.LENGTH_SHORT).show()
                    newPinEt.editText?.text?.clear()
                    confirmNewPinEt.editText?.text?.clear()
                }
            }
        }
    }

    private fun validateNoEmptyColumn(newPin: String, confirmNewPin: String) : Boolean {
        val result = if(newPin.isEmpty() || confirmNewPin.isEmpty() || newPin.isEmpty() && confirmNewPin.isEmpty()) {
            Snackbar.make(requireView(), "Semua kolom PIN wajib diisi untuk melanjutkan.", Snackbar.LENGTH_SHORT).show()
            false
        } else {
            true
        }

        return result
    }
}