package com.finsera.presentation.fragments.forgetpinapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentForgetAppPinBinding
import com.finsera.presentation.fragments.forgetpinapp.viewmodel.ForgetPINAppViewModel
import com.finsera.presentation.fragments.ubahmpin.viewmodel.UbahMPINViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ForgetAppPinFragment : Fragment() {
    private var _binding: FragmentForgetAppPinBinding? = null
    private val binding get() = _binding!!

    private val forgetPINAppViewModel : ForgetPINAppViewModel by inject()

    private var pinBaru = ""
    private var konfirmasiPinBaru = ""
    private var passwordAkun = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgetAppPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        observer()

        binding.btnKonfirmasi.setOnClickListener {
            pinBaru = binding.pinbaruEditText.editText?.text.toString()
            konfirmasiPinBaru = binding.konfirmasipinbaruEditText.editText?.text.toString()
            passwordAkun = binding.passwordAkunEditText.editText?.text.toString()
            if(pinBaru.isNotEmpty() && konfirmasiPinBaru.isNotEmpty() && passwordAkun.isNotEmpty()) {
                konfirmasi()
            } else {
                Toast.makeText(requireActivity(), "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                forgetPINAppViewModel.forgetPINAppUiState.collectLatest { uiState ->
                    uiState.message?.let {
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
                        forgetPINAppViewModel.messageShown()
                    }

                    if(uiState.isSuccess) {
                        findNavController().popBackStack(R.id.loginPinFragment, false)
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

    private fun konfirmasi() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Konfirmasi PIN Aplikasi Baru")
            .setMessage("Dengan anda menekan 'Ya', PIN Aplikasi lama anda akan direset dan akan diganti dengan PIN Aplikasi yang baru anda buat ini.\n\nJika anda menekan 'Tidak', anda akan diarahkan ke halaman login akun untuk melanjutkan proses login dengan PIN Aplikasi lama anda.")
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(requireActivity(), "Anda membatalkan untuk membuat PIN Aplikasi Baru.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack(R.id.loginPinFragment, false)
            }
            .setPositiveButton("Ya") { dialog, which ->
                forgetPINAppViewModel.changePin(pinBaru, konfirmasiPinBaru, passwordAkun)
            }
            .show()
    }
}