package com.finsera.presentation.fragments.transfer.merchant_qris

import android.graphics.Color
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
import com.finsera.common.utils.DisableTouchEvent
import com.finsera.common.utils.format.CurrencyFormatter
import com.finsera.common.utils.network.ConnectivityManager
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferQrisMerchantFormBinding
import com.finsera.presentation.fragments.transfer.merchant_qris.viewmodel.TransferQrisMerchantFormViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TransferQrisMerchantFormFragment : Fragment() {
    private var _binding : FragmentTransferQrisMerchantFormBinding? = null
    private val binding get() = _binding!!

    private var namaMerchant: String? = null
    private var namaKotaMerchant: String? = null
    private var noTrxMerchant: String? = null
    private var saldoRekening: Double? = null

    private val transferQrisMerchantFormViewModel : TransferQrisMerchantFormViewModel by inject()
    private val connectivityManager : ConnectivityManager by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransferQrisMerchantFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transferQrisMerchantFormViewModel.getInfoSaldoSaya() // init get info saldo
        getBundle()
        handleNextBtn()
        observeDataSaldo()
    }

    private fun handleNextBtn() {
        binding.btnNext.setOnClickListener {
            if(validateQrisInfo()) {
                if(connectivityManager.hasInternetConnection()) {
                    if(binding.etNominal.text.toString().isNotEmpty()) {
                        navigateToFormKonfirmasi()
                    } else {
                        Toast.makeText(requireActivity(), "Isi Kolom Nominal Transfer Terlebih Dahulu!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Snackbar.make(requireView(), "Tidak ada koneksi internet.", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireActivity(), "QRIS Tidak Valid atau Tidak Didukung!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateQrisInfo() : Boolean {
        val result = if(namaMerchant.isNullOrEmpty() || noTrxMerchant.isNullOrEmpty() || namaKotaMerchant.isNullOrEmpty()) {
            false
        } else {
            true
        }

        return result
    }

    private fun navigateToFormKonfirmasi() {
        transferQrisMerchantFormViewModel.getInfoSaldoSaya() // update saldo
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transferQrisMerchantFormViewModel.transferQrisMerchantFormUiState.collectLatest { uiState ->
                    if(uiState.isDataSaldoReady) {
                        if(findNavController().currentDestination?.id == R.id.transferQrisMerchantFormFragment) {
                            val nominalTransfer = binding.etNominal.text.toString()

                            val bundle = Bundle().apply {
                                putString(Constant.NOMINAL_TRANSFER_EXTRA, nominalTransfer)
                                putString(Constant.NAMA_MERCHANT_QRIS, namaMerchant)
                                putString(Constant.NAMA_KOTA_MERCHANT_QRIS, namaKotaMerchant)
                                putString(Constant.NOMOR_TRX_MERCHANT_QRIS, noTrxMerchant)
                            }

                            try {
                                if(nominalTransfer.toInt() > 0) {
                                    if(nominalTransfer.toInt().toDouble() < saldoRekening!!) {
                                        findNavController().navigate(R.id.action_transferQrisMerchantFormFragment_to_transferQrisMerchantFormKonfirmasiFragment, bundle)
                                    } else {
                                        Snackbar.make(requireView(), "Saldo anda tidak mencukupi. Nominal harus lebih kecil atau sama dengan saldo di rekening anda.", Snackbar.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Snackbar.make(requireView(), "Nominal transfer harus lebih dari Rp0", Snackbar.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Snackbar.make(requireView(), "Maksimal transaksi dibatasi yaitu Rp1.000.000.000 per transaksinya", Snackbar.LENGTH_SHORT).show()
                                binding.etNominal.editableText.clear()
                            }

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

        binding.cardInfoMerchant.tvKotaMerchant.text = namaKotaMerchant
        binding.cardInfoMerchant.tvNamaMerchant.text = namaMerchant
    }

    private fun observeDataSaldo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transferQrisMerchantFormViewModel.transferQrisMerchantFormUiState.collectLatest { uiState ->
                    uiState.message?.let {
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                        transferQrisMerchantFormViewModel.messageFormShown()
                    }

                    if(uiState.isDataSaldoLoading) {
                        binding.cardInfoSaldo.tvSaldoRekening.visibility = View.INVISIBLE
                        binding.cardInfoSaldo.tvSaldoRekeningLoading.visibility = View.VISIBLE
                        binding.cardInfoSaldo.tvSaldoRekeningLoading.startShimmer()
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.cardInfoSaldo.tvSaldoRekeningLoading.visibility = View.INVISIBLE
                        binding.cardInfoSaldo.tvSaldoRekening.visibility = View.VISIBLE
                        binding.cardInfoSaldo.tvSaldoRekeningLoading.stopShimmer()
                        binding.progressBar.visibility = View.GONE
                    }

                    if(uiState.isDataSaldoReady) {
                        saldoRekening = uiState.dataSaldo?.amount
                        val formattedSaldo = "Rp" + CurrencyFormatter.formatCurrency(saldoRekening!!)
                        binding.cardInfoSaldo.tvSaldoRekening.text = formattedSaldo
                    }

                    if(!uiState.hasInternet) {
                        binding.cardInfoSaldo.tvSaldoRekening.text = "Tidak Ada Koneksi Internet. Klik untuk coba lagi."
                        binding.cardInfoSaldo.tvSaldoRekening.setTextColor(Color.RED)
                        binding.cardInfoSaldo.tvSaldoRekening.setOnClickListener {
                            transferQrisMerchantFormViewModel.getInfoSaldoSaya()
                        }
                    } else {
                        binding.cardInfoSaldo.tvSaldoRekening.setTextColor(Color.BLACK)
                        binding.cardInfoSaldo.tvSaldoRekening.setOnClickListener { null }
                    }
                }
            }
        }
    }
}