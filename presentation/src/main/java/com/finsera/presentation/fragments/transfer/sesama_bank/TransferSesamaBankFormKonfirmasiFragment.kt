package com.finsera.presentation.fragments.transfer.sesama_bank

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
import com.finsera.common.utils.Constant
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferSesamaBankFormKonfirmasiBinding
import com.finsera.presentation.fragments.transfer.sesama_bank.bundle.CekRekeningSesamaBundle
import com.finsera.presentation.fragments.transfer.sesama_bank.viewmodel.TransferSesamaBankViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TransferSesamaBankFormKonfirmasiFragment : Fragment() {
    private var _binding: FragmentTransferSesamaBankFormKonfirmasiBinding? = null
    private val binding get() = _binding!!

    private val transferSesamaBankViewModel : TransferSesamaBankViewModel by inject()

    private var namaPemilikRekening: String? = null
    private var nomorRekening: String? = null
    private var addToDaftarTersimpan: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferSesamaBankFormKonfirmasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        observer()

        val dataRekeningBundle = arguments?.getParcelable<CekRekeningSesamaBundle>(Constant.DATA_REKENING_SESAMA_BUNDLE)
        val nominalTransfer = arguments?.getString(Constant.NOMINAL_TRANSFER_EXTRA)
        val catatanTransfer = arguments?.getString(Constant.CATATAN_TRANSFER_EXTRA)
        addToDaftarTersimpan = requireArguments().getBoolean(Constant.DAFTAR_TERSIMPAN_CHECKED_EXTRA)

        Toast.makeText(requireActivity(), addToDaftarTersimpan.toString(), Toast.LENGTH_SHORT).show()

        if(dataRekeningBundle != null && nominalTransfer != null) {
            namaPemilikRekening = dataRekeningBundle.namaPemilikRekening
            nomorRekening = dataRekeningBundle.noRekening

            binding.tvNamaPemilikRekeningTujuan.setText(namaPemilikRekening)
            binding.tvRekeningTujuan.setText(nomorRekening)
            binding.tvCatatanTf.setText(catatanTransfer ?: "-")
            binding.tvNominalAwal.setText("Rp $nominalTransfer")
            binding.tvBiayaAdmin.setText("Gratis")
            binding.tvNominalTotal.setText("Rp $nominalTransfer")

            binding.btnNext.setOnClickListener {
                if(binding.etPin != null) {
                    transferSesamaBankViewModel.transferSesama(nomorRekening!!, nominalTransfer.toDouble()!!, catatanTransfer!!, binding.etPinTransaksi.editText?.text.toString())
                }
            }
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transferSesamaBankViewModel.transferSesamaFormKonfirmasiUiState.collectLatest { uiState ->
                    uiState.message?.let { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                        transferSesamaBankViewModel.messageFormKonfirmasiShown()
                    }

                    if (uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }

                    if (uiState.isSuccess) {
                        if(addToDaftarTersimpan) {
                            transferSesamaBankViewModel.simpanKeDaftarTersimpanSesama(namaPemilikRekening!!, nomorRekening!!)
                        }

                        if (findNavController().currentDestination?.id == R.id.transferSesamaBankFormKonfirmasiFragment) {
                            val bundle = Bundle().apply {
                                putParcelable(Constant.TRANSFER_SESAMA_BERHASIL_BUNDLE, uiState.data)
                                Snackbar.make(requireView(), "Rekening berhasil disimpan ke Daftar Tersimpan!", Snackbar.LENGTH_SHORT).show()
                            }
                            Snackbar.make(requireView(), "Transfer Berhasil", Snackbar.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_transferSesamaBankFormKonfirmasiFragment_to_transferSesamaBankSuksesFragment, bundle)
                            transferSesamaBankViewModel.transferSesamaBerhasil()
                        }
                    }
                }
            }
        }
    }
}