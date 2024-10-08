package com.finsera.presentation.fragments.topup.ewallet

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
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferEWalletFormKonfirmasiBinding
import com.finsera.presentation.fragments.topup.ewallet.bundle.CekEWalletBundle
import com.finsera.presentation.fragments.topup.ewallet.bundle.SuccessEWalletBundle
import com.finsera.presentation.fragments.topup.ewallet.viewmodel.TransferEWalletViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferEWalletFormKonfirmasiFragment : Fragment() {
    private var _binding: FragmentTransferEWalletFormKonfirmasiBinding? = null
    private val binding get() = _binding!!

    private var addToDaftarTersimpan: Boolean = false
    private var ewalletId: Int = 0
    private lateinit var ewalletName: String
    private lateinit var ewalletAccountNum: String
    private lateinit var ewalletAccountName: String

    private val viewModel: TransferEWalletViewModel by viewModel()

    private var hasAnnouncedScreen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferEWalletFormKonfirmasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_confirm_transaction))
            hasAnnouncedScreen = true
        }

        viewModel.resetState()

        val bundle = arguments?.getParcelable<CekEWalletBundle>(Constant.DATA_CEK_EWALLET)
        val savedItemMode = arguments?.getBoolean(Constant.DAFTAR_TERSIMPAN_CHECKED_EXTRA)

        if (bundle != null) {
            ewalletId = bundle.id
            ewalletName = bundle.namaEWallet
            ewalletAccountNum = bundle.nomorEWallet
            ewalletAccountName = bundle.namaAkunEWallet
        }


        if (savedItemMode == true) {
            addToDaftarTersimpan = true
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvJenisEWallet.text = ewalletName
        binding.tvNomorEWallet.text = ewalletAccountNum
        binding.tvNamaPengguna.text = ewalletAccountName

        binding.btnNext.setOnClickListener {
            if (binding.etPinTransaksi.editText?.text.toString()
                    .isNotEmpty() && binding.etNominal.editText?.text.toString().isNotEmpty()
            ) {
                viewModel.transferEWallet(
                    eWalletId = ewalletId,
                    note = getString(R.string.transfer_e_wallet_text, ewalletName),
                    pin = binding.etPinTransaksi.editText?.text.toString(),
                    nominal = binding.etNominal.editText?.text.toString().toDouble(),
                    eWalletAccountNum = ewalletAccountNum
                ).also {
                    observer()
                }
            } else {
                if (binding.etPinTransaksi.editText?.text.toString().isEmpty()) {
                    binding.etPinTransaksi.error = "PIN Transaksi harus diisi!"
                } else if (binding.etNominal.editText?.text.toString().isEmpty()) {
                    binding.etNominal.error = "Nominal harus diisi!"
                } else {
                    binding.etPinTransaksi.error = "PIN Transaksi harus diisi!"
                    binding.etNominal.error = "Nominal harus diisi!"
                }
            }
        }

        setAccessibilityDescriptions()

    }

    private fun formatAccountNumberForAccessibility(accountNumber: String): String {
        return accountNumber.map { it.toString() }.joinToString(" ")
    }
    private fun setAccessibilityDescriptions() {
        binding.apply {
            val formattedAccountNumber = formatAccountNumberForAccessibility(tvNomorEWallet.text.toString())
            layoutjenisewallet.contentDescription = getString(R.string.tipe_e_wallet_desc, tvJenisEWallet.text)
            layoutnamapengguna.contentDescription = getString(R.string.nama_pengguna_desc, tvNamaPengguna.text)
            layoutnomorewallet.contentDescription = getString(R.string.nomor_e_wallet_desc, formattedAccountNumber)
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transferEWalletUiState.collectLatest { uiState ->
                    uiState.message?.let { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                        viewModel.messageShown()
                    }

                    if (uiState.isLoading) {
                        binding.viewFlipper.displayedChild = 1
                    } else {
                        binding.viewFlipper.displayedChild = 0
                    }

                    if (uiState.isSuccess) {
                        if (findNavController().currentDestination?.id == R.id.transferEWalletFormKonfirmasiFragment) {
                            val dataEWallet = SuccessEWalletBundle(
                                accountSender = uiState.data?.accountSender!!,
                                ewalletName = uiState.data.ewalletName!!,
                                note = uiState.data.note!!,
                                feeAdmin = uiState.data.feeAdmin!!,
                                nominal = uiState.data.nominal!!,
                                transactionDate = uiState.data.transactionDate!!,
                                transactionNum = uiState.data.transactionNum!!,
                                nameSender = uiState.data.nameSender!!,
                                ewalletAccountName = ewalletAccountName,
                                ewalletAccount = ewalletAccountNum
                            )

                            val bundle = Bundle().apply {
                                putParcelable(Constant.DATA_TF_EWALLET_BUNDLE, dataEWallet)
                            }

                            findNavController().navigate(
                                R.id.action_transferEWalletFormKonfirmasiFragment_to_transferEWalletSuccessFragment,
                                bundle
                            ).apply {
                                if (addToDaftarTersimpan) {
                                    viewModel.simpanKeDaftarTersimpanEWallet(
                                        eWalletId = ewalletId,
                                        noAkunEWallet = ewalletAccountNum,
                                        namaPemilik = ewalletAccountName,
                                        eWalletName = ewalletName
                                    )
                                }
                            }

                            Snackbar.make(
                                requireView(),
                                "Transfer E-Wallet $ewalletName berhasil ke $ewalletAccountNum",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        // TODO
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}