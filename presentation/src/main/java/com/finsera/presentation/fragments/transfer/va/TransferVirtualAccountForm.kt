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
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferVirtualAccountFormBinding
import com.finsera.presentation.fragments.transfer.va.bundle.CekVaBundle
import com.finsera.presentation.fragments.transfer.va.viewmodel.CheckVaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferVirtualAccountForm : Fragment() {
    private var _binding: FragmentTransferVirtualAccountFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckVaViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferVirtualAccountFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.resetState()

        binding.btnLanjut.setOnClickListener {
            val accountNum = binding.banktujuanEditText.text.toString()
            if (accountNum.isNotEmpty()) {
                viewModel.cekVirtualAccount(accountNum).also {
                    observer()
                }
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.alert_empty_va),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cekVaUiState.collectLatest { uiState ->

                    uiState.message?.let { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                        viewModel.messageShown()
                    }

                    if (uiState.isLoading) {
                        binding.viewFlipper.displayedChild = 1
                    } else {
                        binding.viewFlipper.displayedChild = 0
                    }

                    if (uiState.isValid) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.success_va_account_message), Toast.LENGTH_SHORT
                        ).show()
                        if (findNavController().currentDestination?.id == R.id.transferVirtualAccountForm) {

                            val dataVa = CekVaBundle(
                                vaAccountNum = uiState.data?.accountNum!!,
                                vaAccountName = uiState.data.accountName!!,
                                vaTransferNominal = uiState.data.nominal!!
                            )
                            val bundle = Bundle().apply {
                                putParcelable(Constant.DATA_VA_BUNDLE, dataVa)
                            }
                            findNavController().navigate(R.id.action_transferVirtualAccountForm_to_transferVirtualAccountFormKonfirmasi,bundle)
                        }
                    }

                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}