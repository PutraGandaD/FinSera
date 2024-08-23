package com.finsera.presentation.fragments.topup.ewallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Constant
import com.finsera.domain.model.DaftarTersimpanEWallet
import com.finsera.domain.model.DaftarTersimpanVa
import com.finsera.presentation.R
import com.finsera.presentation.adapters.DaftarTersimpanEWalletAdapter
import com.finsera.presentation.adapters.OnSavedItemEWalletClickListener
import com.finsera.presentation.databinding.FragmentTransferEWalletHomeBinding
import com.finsera.presentation.fragments.topup.ewallet.bundle.CekEWalletBundle
import com.finsera.presentation.fragments.topup.ewallet.viewmodel.TransferEWalletViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferEWalletHomeFragment : Fragment(), OnSavedItemEWalletClickListener {
    private var _binding: FragmentTransferEWalletHomeBinding? = null
    private val binding get() = _binding!!

    private val daftarTersimpanEWalletAdapter = DaftarTersimpanEWalletAdapter(this)
    private val viewModel: TransferEWalletViewModel by viewModel()

    private var hasAnnouncedScreen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferEWalletHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()

        binding.rvDaftarTersimpan.adapter = daftarTersimpanEWalletAdapter

        observer()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_e_wallet))
            hasAnnouncedScreen = true
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transferEWalletHomeUiState.collectLatest { uiState ->
                    if (uiState.data.isNotEmpty()) {
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.GONE
                        daftarTersimpanEWalletAdapter.submitList(uiState.data)
                        uiState.data.forEach { item ->
                            updateAccessibilityInfo(item)
                        }
                    } else {
                        daftarTersimpanEWalletAdapter.submitList(emptyList())
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.VISIBLE
                    }
                }

            }
        }

        binding.etCariDaftarTersimpan.editText?.doOnTextChanged { inputText, _, _, _ ->
            viewModel.cariDaftarTersimpanEWallet(inputText.toString())
        }
    }

    private fun setupListeners() {
        binding.btnInputBaru.setOnClickListener {
            findNavController().navigate(R.id.action_transferEWalletHomeFragment_to_transferEWalletListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateAccessibilityInfo(daftarTersimpan: DaftarTersimpanEWallet) {
        val accountNumber = daftarTersimpan.nomorEWallet
        val accountNumberWithSpaces = accountNumber.replace("", " ").trim()
        val accessibilityText = getString(R.string.desc_daftar_tersimpan_ewallet, daftarTersimpan.namaEWallet, daftarTersimpan.namaAkunEWallet, accountNumberWithSpaces)
        daftarTersimpanEWalletAdapter.setAccessibilityText(daftarTersimpan, accessibilityText)
    }

    override fun onSavedItemEWalletClicked(daftarTersimpan: DaftarTersimpanEWallet) {
        val dataEWallet = CekEWalletBundle(
            namaEWallet = daftarTersimpan.namaEWallet,
            id = daftarTersimpan.idAkunEWallet,
            nomorEWallet = daftarTersimpan.nomorEWallet,
            namaAkunEWallet = daftarTersimpan.namaAkunEWallet
        )
        val bundle = Bundle().apply {
            putParcelable(Constant.DATA_CEK_EWALLET, dataEWallet)
            putBoolean(Constant.DAFTAR_TERSIMPAN_CHECKED_EXTRA, false)
        }
        findNavController().navigate(
            R.id.action_transferEWalletHomeFragment_to_transferEWalletFormKonfirmasiFragment,
            bundle
        )
    }
}
