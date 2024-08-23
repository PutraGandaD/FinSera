package com.finsera.presentation.fragments.transfer.va

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Constant
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.model.DaftarTersimpanVa
import com.finsera.presentation.R
import com.finsera.presentation.adapters.DaftarTersimpanSesamaAdapter
import com.finsera.presentation.adapters.DaftarTersimpanVaAdapter
import com.finsera.presentation.adapters.OnSavedItemSesamaClickListener
import com.finsera.presentation.adapters.OnSavedItemVaClickListener
import com.finsera.presentation.databinding.FragmentTransferVirtualAccountHomeBinding
import com.finsera.presentation.fragments.transfer.va.bundle.CekVaBundle
import com.finsera.presentation.fragments.transfer.va.viewmodel.TransferVaViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferVirtualAccountHome : Fragment(), OnSavedItemVaClickListener {
    private var _binding: FragmentTransferVirtualAccountHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransferVaViewModel by viewModel()
    private val adapter = DaftarTersimpanVaAdapter(this)

    private var hasAnnouncedScreen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferVirtualAccountHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvDaftarTersimpan.adapter = adapter
        observer()
        setupListeners()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_virtual_account))
            hasAnnouncedScreen = true
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transferVaHomeUiState.collectLatest { uiState ->
                    if (uiState.data.isNotEmpty()) {
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.GONE
                        adapter.submitList(uiState.data)
                        uiState.data.forEach { item ->
                            updateAccessibilityInfo(item)
                        }
                    } else {
                        adapter.submitList(emptyList())
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.etCariDaftarTersimpan.editText?.doOnTextChanged { inputText, _, _, _ ->
            viewModel.cariDaftarTersimpanVa(inputText.toString())
        }
    }

    private fun setupListeners() {
        binding.btnInput.setOnClickListener {
            findNavController().navigate(R.id.action_transferVirtualAccountHome_to_transferVirtualAccountForm)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSavedItemVaClicked(daftarTersimpan: DaftarTersimpanVa) {
        val dataVa = daftarTersimpan.noRekening

        val bundle = Bundle().apply {
            putString(Constant.DATA_NO_VA_STRING,dataVa)
            putBoolean(Constant.DAFTAR_TERSIMPAN_SELECTED_MODE, true)
        }

        if (findNavController().currentDestination?.id == R.id.transferVirtualAccountHome) {
            findNavController().navigate(
                R.id.action_transferVirtualAccountHome_to_transferVirtualAccountForm,
                bundle
            )
        }
    }

    private fun updateAccessibilityInfo(daftarTersimpan: DaftarTersimpanVa) {
        val accountNumber = daftarTersimpan.noRekening
        val accountNumberWithSpaces = accountNumber.replace("", " ").trim()
        val accessibilityText = getString(R.string.desc_daftar_tersimpan_va, daftarTersimpan.namaPemilikRekening, accountNumberWithSpaces)
        adapter.setAccessibilityText(daftarTersimpan, accessibilityText)
    }

}