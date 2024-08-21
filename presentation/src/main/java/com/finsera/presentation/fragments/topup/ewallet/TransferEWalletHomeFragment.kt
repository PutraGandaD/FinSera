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

    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transferEWalletHomeUiState.collectLatest { uiState ->
                    if (uiState.data.isNotEmpty()) {
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.GONE
                        daftarTersimpanEWalletAdapter.submitList(uiState.data)
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

//    private fun setupSavedEWalletList() {
//        // TODO
//    }
//
//    private fun loadSavedEWallets(): List<SavedEWallet> {
//        // TODO
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSavedItemVaClicked(daftarTersimpan: DaftarTersimpanEWallet) {
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
