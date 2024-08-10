package com.finsera.presentation.fragments.transfer.sesama_bank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Constant
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferSesamaBankFormKonfirmasiBinding
import com.finsera.presentation.fragments.transfer.sesama_bank.bundle.CekRekeningSesama
import com.finsera.presentation.fragments.transfer.sesama_bank.viewmodel.TransferSesamaBankViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TransferSesamaBankFormKonfirmasiFragment : Fragment() {
    private var _binding: FragmentTransferSesamaBankFormKonfirmasiBinding? = null
    private val binding get() = _binding!!

    private val transferSesamaBankViewModel : TransferSesamaBankViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferSesamaBankFormKonfirmasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBackButton()
        observer()

        val bundle = arguments?.getParcelable<CekRekeningSesama>(Constant.TRANSFER_SESAMA_BUNDLE)
        if(bundle != null) {
            val noRekening = bundle.noRekening
            val namaPemilikRekening = bundle.namaPemilikRekening


            binding.btnNext.setOnClickListener {
                if(binding.etPin != null) {
                    //transferSesamaBankViewModel.transferSesama(noRekening, nominal, catatan!!, binding.etPinTransaksi.editText?.text.toString())
                }
            }
        }
    }

    private fun handleBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda ingin keluar dari halaman ini? Apabila anda keluar dari halaman ini, anda harus mengulang input data transaksi dari awal.")
                .setNeutralButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Ya") { dialog, _ ->
                    //findNavController().navigate(R.id.action_transferSesamaBankFormKonfirmasi_to_homeFragment)
                }
                .show()
        }


        binding.btnBack.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda ingin keluar dari halaman ini? Apabila anda keluar dari halaman ini, anda harus mengulang input data transaksi dari awal.")
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Ya") { dialog, _ ->
                    //findNavController().navigate(R.id.action_transferSesamaBankFormKonfirmasi_to_homeFragment)
                }
                .show()
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transferSesamaBankViewModel.transferSesamaUiState.collectLatest { uiState ->
                    uiState.message?.let { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                        transferSesamaBankViewModel.messageShown()
                    }

                    if (uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }

                    if (uiState.isSuccess) {
                        if (findNavController().currentDestination?.id == R.id.transferSesamaBankFormKonfirmasiFragment) {
                            Snackbar.make(requireView(), "Transfer berhasil", Snackbar.LENGTH_SHORT)
                                .show()

                            //findNavController().navigate(R.id.action_transferSesamaBankFormKonfirmasi_to_homeFragment)
                        }
                    }
                }
            }
        }
    }
}