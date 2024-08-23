package com.finsera.presentation.fragments.transfer.merchant_qris

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Constant
import com.finsera.common.utils.DisableTouchEvent
import com.finsera.common.utils.format.CurrencyFormatter
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferQrisMerchantFormKonfirmasiBinding
import com.finsera.presentation.fragments.transfer.merchant_qris.viewmodel.TransferQrisMerchantViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TransferQrisMerchantFormKonfirmasiFragment : Fragment() {
    private var _binding : FragmentTransferQrisMerchantFormKonfirmasiBinding? = null
    private val binding get() = _binding!!

    private val transferQrisMerchantViewModel : TransferQrisMerchantViewModel by inject()

    private var namaMerchant: String? = null
    private var namaKotaMerchant: String? = null
    private var noTrxMerchant: String? = null
    private var nominalTransfer: String? = null

    private var hasAnnouncedScreen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransferQrisMerchantFormKonfirmasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        observer()
        getBundle()
        handleNextBtn()

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_confirm_transaction))
            hasAnnouncedScreen = true
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transferQrisMerchantViewModel.transferQrisMerchantKonfirmasiUiState.collectLatest { uiState ->
                    uiState.message?.let { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                        transferQrisMerchantViewModel.messageFormKonfirmasiShown()
                    }

                    if (uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }

                    if (uiState.isSuccess) {
                        if (findNavController().currentDestination?.id == R.id.transferQrisMerchantFormKonfirmasiFragment) {
                            val bundle = Bundle().apply {
                                putString(Constant.NAMA_NASABAH, transferQrisMerchantViewModel.userInfo?.first)
                                putString(Constant.NOMOR_REKENING_NASABAH, transferQrisMerchantViewModel.userInfo?.second)
                                putString(Constant.NOMOR_TRX_MERCHANT_QRIS, noTrxMerchant)
                                putParcelable(Constant.TRANSFER_QRIS_MERCHANT_BERHASIL_BUNDLE, uiState.data)
                            }

                            Snackbar.make(requireView(), "Transfer Berhasil.", Snackbar.LENGTH_SHORT).show()

                            findNavController().navigate(R.id.action_transferQrisMerchantFormKonfirmasiFragment_to_transferQrisMerchantSuksesFragment, bundle)
                            transferQrisMerchantViewModel.transferQrisMerchantBerhasil()
                        }
                    }
                }
            }
        }
    }

    private fun getBundle() {
        namaMerchant = arguments?.getString(Constant.NAMA_MERCHANT_QRIS) ?: " "
        namaKotaMerchant = arguments?.getString(Constant.NAMA_KOTA_MERCHANT_QRIS) ?: " "
        noTrxMerchant = arguments?.getString(Constant.NOMOR_TRX_MERCHANT_QRIS) ?: " "
        nominalTransfer = arguments?.getString(Constant.NOMINAL_TRANSFER_EXTRA) ?: " "

        binding.tvNamaMerchant.text = namaMerchant
        binding.tvKotaMerchant.text = namaKotaMerchant
        binding.tvNominalAwal.text = "Rp ${CurrencyFormatter.formatCurrency(nominalTransfer!!.toInt().toDouble())}"
        binding.tvBiayaAdmin.text = "Gratis"
        binding.tvNominalTotal.text = "Rp ${CurrencyFormatter.formatCurrency(nominalTransfer!!.toInt().toDouble())}"

        setAccessibilityDescriptions()
    }

    private fun handleNextBtn() {
        binding.btnNext.setOnClickListener {
            if(binding.etPinTransaksi.editText?.text.toString().isNotEmpty()) {
                transferQrisMerchantViewModel.transferQrisMerchant(namaMerchant!!, namaMerchant!!, nominalTransfer!!.toDouble(), binding.etPinTransaksi.editText?.text.toString())
            } else {
                Snackbar.make(requireView(), "PIN Transaksi harus diisi!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAccessibilityDescriptions() {
        binding.apply {
            layoutnamamerchant.contentDescription = getString(R.string.merchant_tujuan_desc, tvNamaMerchant.text)
            layoutkotamerchant.contentDescription = getString(R.string.kota_tujuan_desc, tvKotaMerchant.text)
            layoutnominal.contentDescription = getString(R.string.nominal_transfer_desc, tvNominalAwal.text)
            layoutbiayaadmin.contentDescription = getString(R.string.biaya_admin_desc, tvBiayaAdmin.text)
            layoutnominaltotal.contentDescription = getString(R.string.nominal_total_desc, tvNominalTotal.text)
        }
    }
}