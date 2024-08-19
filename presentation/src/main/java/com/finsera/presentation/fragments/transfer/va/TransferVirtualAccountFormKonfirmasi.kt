package com.finsera.presentation.fragments.transfer.va

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
import com.finsera.common.utils.format.CurrencyFormatter
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferVirtualAccountFormKonfirmasiBinding
import com.finsera.presentation.fragments.transfer.va.bundle.CekVaBundle
import com.finsera.presentation.fragments.transfer.va.bundle.SuccesVaBundle
import com.finsera.presentation.fragments.transfer.va.viewmodel.TransferVaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferVirtualAccountFormKonfirmasi : Fragment() {
    private var _binding: FragmentTransferVirtualAccountFormKonfirmasiBinding? = null
    private val binding get() = _binding!!

    private lateinit var accountNum: String
    private lateinit var accountName: String
    private var nominal: Int = 0
    private var addToDaftarTersimpan: Boolean = false

    private val viewModel: TransferVaViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentTransferVirtualAccountFormKonfirmasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()


        viewModel.resetState()


        //bundle
        val bundle = arguments?.getParcelable<CekVaBundle>(Constant.DATA_VA_BUNDLE)
        addToDaftarTersimpan =
            requireArguments().getBoolean(Constant.DAFTAR_TERSIMPAN_CHECKED_EXTRA)
        if (bundle != null) {
            accountNum = bundle.vaAccountNum
            accountName = bundle.vaAccountName
            nominal = bundle.vaTransferNominal
        }

        binding.tvNoVA.text = accountNum
        binding.tvNamaVA.text = accountName
        binding.tvNominalAwal.text = StringBuilder().append("Rp ")
            .append(CurrencyFormatter.formatCurrency(nominal.toDouble()))


        binding.btnLanjut.setOnClickListener {
            val mPin = binding.tiePin.text.toString()
            if (mPin.isNotEmpty()) {
                viewModel.transferVa(accountNum, mPin).also {
                    observer()
                }
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.pin_empty),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    }


    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transferVaUiState.collectLatest { uiState ->

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
                        if (addToDaftarTersimpan) {
                            viewModel.simpanKeDaftarTersimpanVa(
                                namaPemilik = accountName,
                                noRek = accountNum,
                                nominal = nominal
                            )
                        }
                        if (findNavController().currentDestination?.id == R.id.transferVirtualAccountFormKonfirmasi) {

                            val dataTransferVa = SuccesVaBundle(
                                transactionDate = uiState.data?.transactionDate!!,
                                recipientVirtualAccountNum = uiState.data.recipientVirtualAccountNum!!,
                                type = uiState.data.type!!,
                                nominal = uiState.data.nominal!!,
                                transactionNum = uiState.data.transactionNum!!,
                                recipientName = uiState.data.recipientName!!
                            )
                            val bundle = Bundle().apply {
                                putParcelable(Constant.DATA_TF_VA_BUNDLE, dataTransferVa)
                            }
                            findNavController().navigate(
                                R.id.action_transferVirtualAccountFormKonfirmasi_to_transferVirtualAccountSuccessFragment,
                                bundle
                            )
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