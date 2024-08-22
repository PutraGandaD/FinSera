package com.finsera.presentation.fragments.transfer.va

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Constant
import com.finsera.common.utils.DisableTouchEvent
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferVirtualAccountFormBinding
import com.finsera.presentation.fragments.transfer.va.bundle.CekVaBundle
import com.finsera.presentation.fragments.transfer.va.viewmodel.CheckVaViewModel
import com.finsera.presentation.fragments.transfer.va.uistate.CheckVirtualAccountUiState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TransferVirtualAccountForm : Fragment() {
    private var _binding: FragmentTransferVirtualAccountFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CheckVaViewModel by viewModel()

    private var addToDaftarTersimpan: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferVirtualAccountFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialState()
        setupAccountNumberAccessibility()
        setupListeners()
        observer()
    }

    private fun setupInitialState() {
        val savedItemMode = arguments?.getBoolean(Constant.DAFTAR_TERSIMPAN_SELECTED_MODE)
        val noVa = arguments?.getString(Constant.DATA_NO_VA_STRING)

        if (savedItemMode == true) {
            binding.llSave.visibility = View.GONE
        }

        if (noVa != null) {
            binding.nomorVAEditText.setText(noVa)
        }

        viewModel.resetState()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLanjut.setOnClickListener {
            val accountNum = binding.nomorVAEditText.text.toString()
            if (accountNum.isNotEmpty()) {
                viewModel.cekVirtualAccount(accountNum)
            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.alert_empty_va),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.cbSave.setOnCheckedChangeListener { _, isChecked ->
            addToDaftarTersimpan = isChecked
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cekVaUiState.collectLatest { uiState: CheckVirtualAccountUiState ->
                    uiState.message?.let { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                        viewModel.messageShown()
                    }

                    if (uiState.isLoading) {
                        binding.viewFlipper.displayedChild = 1
                        DisableTouchEvent.setInteractionDisabled(requireActivity(), true)
                    } else {
                        binding.viewFlipper.displayedChild = 0
                        DisableTouchEvent.setInteractionDisabled(requireActivity(), false)
                    }

                    if (uiState.isValid) {
                        handleValidVirtualAccount(uiState)
                    }
                }
            }
        }
    }

    private fun handleValidVirtualAccount(uiState: CheckVirtualAccountUiState) {
        Snackbar.make(
            requireView(),
            getString(R.string.success_va_account_message),
            Snackbar.LENGTH_SHORT
        ).show()
        if (findNavController().currentDestination?.id == R.id.transferVirtualAccountForm) {
            val dataVa = CekVaBundle(
                vaAccountNum = uiState.data?.accountNum!!,
                vaAccountName = uiState.data.accountName!!,
                vaTransferNominal = uiState.data.nominal!!
            )
            val bundle = Bundle().apply {
                putParcelable(Constant.DATA_VA_BUNDLE, dataVa)
                putBoolean(Constant.DAFTAR_TERSIMPAN_CHECKED_EXTRA, addToDaftarTersimpan)
            }
            findNavController().navigate(
                R.id.action_transferVirtualAccountForm_to_transferVirtualAccountFormKonfirmasi,
                bundle
            )
        }
    }

    private fun setupAccountNumberAccessibility() {
        binding.nomorVAEditText.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                val editText = host as? EditText
                editText?.text?.let { text ->
                    info.text = text.toString().map { it.toString() }.joinToString(" ")
                }
            }
        }

        binding.nomorVAEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    binding.nomorVAEditText.announceForAccessibility(it.toString().map { char -> char.toString() }.joinToString(" "))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}