package com.finsera.presentation.fragments.transfer.sesama_bank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Constant
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.presentation.R
import com.finsera.presentation.adapters.DaftarTersimpanSesamaAdapter
import com.finsera.presentation.adapters.OnSavedItemSesamaClickListener
import com.finsera.presentation.databinding.FragmentTransferSesamaBankHomeBinding
import com.finsera.presentation.fragments.transfer.sesama_bank.bundle.CekRekeningSesamaBundle
import com.finsera.presentation.fragments.transfer.sesama_bank.viewmodel.TransferSesamaBankHomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TransferSesamaBankHomeFragment : Fragment(), OnSavedItemSesamaClickListener {
    private var _binding: FragmentTransferSesamaBankHomeBinding? = null
    private val binding get() = _binding!!

    private val transferSesamaBankHomeViewModel : TransferSesamaBankHomeViewModel by inject()
    private val daftarTersimpanSesamaAdapter = DaftarTersimpanSesamaAdapter(this)

    private var hasAnnouncedScreen = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferSesamaBankHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewDaftarTersimpanEmpty.root.visibility = View.GONE
        binding.rvDaftartersimpan.adapter = daftarTersimpanSesamaAdapter

        observer()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnTransferBaru.setOnClickListener {
            findNavController().navigate(R.id.action_transferSesamaBankHome_to_cekRekeningSesamaBankFormFragment)
        }

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_transfer_sesama_bank))
            hasAnnouncedScreen = true
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transferSesamaBankHomeViewModel.transferSesamaHomeUiState.collectLatest { uiState ->
                    uiState.data?.let { daftarTersimpan ->
                        daftarTersimpanSesamaAdapter.submitList(daftarTersimpan)
                        daftarTersimpan.forEach { item ->
                            updateAccessibilityInfo(item)
                        }
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.GONE
                    }

                    if (uiState.data.isNullOrEmpty()) {
                        daftarTersimpanSesamaAdapter.submitList(null)
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.etCariDaftarTersimpan.editText?.doAfterTextChanged { inputText ->
            transferSesamaBankHomeViewModel.cariDaftarTersimpanSesama(inputText.toString())
        }
    }

    override fun onSavedItemSesamaClicked(daftarTersimpan: DaftarTersimpanSesama) {
        val dataRekening = CekRekeningSesamaBundle(daftarTersimpan.namaPemilikRekening, daftarTersimpan.noRekening)

        val bundle = Bundle().apply {
            putParcelable(Constant.DATA_REKENING_SESAMA_BUNDLE, dataRekening)
            putBoolean(Constant.DAFTAR_TERSIMPAN_SELECTED_MODE, true)
        }

        if(findNavController().currentDestination?.id == R.id.transferSesamaBankHome) {
            findNavController().navigate(R.id.action_transferSesamaBankHome_to_transferSesamaBankFormFragment, bundle)
        }
    }

    private fun updateAccessibilityInfo(daftarTersimpan: DaftarTersimpanSesama) {
        val accountNumber = daftarTersimpan.noRekening
        val accountNumberWithSpaces = accountNumber.replace("", " ").trim()
        val accessibilityText = getString(R.string.desc_daftar_tersimpan, daftarTersimpan.namaPemilikRekening, accountNumberWithSpaces)
        daftarTersimpanSesamaAdapter.setAccessibilityText(daftarTersimpan, accessibilityText)
    }
}