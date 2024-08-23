package com.finsera.presentation.fragments.transfer.antar_bank

import androidx.fragment.app.viewModels
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
import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.presentation.R
import com.finsera.presentation.adapters.DaftarTersimpanAntarAdapter
import com.finsera.presentation.adapters.OnSavedItemAntarClickListener
import com.finsera.presentation.databinding.FragmentTransferAntarBankHomeBinding
import com.finsera.presentation.fragments.transfer.antar_bank.bundle.CekRekeningAntarBundle
import com.finsera.presentation.fragments.transfer.antar_bank.viewmodel.TransferAntarBankHomeViewModel
import com.finsera.presentation.fragments.transfer.sesama_bank.bundle.CekRekeningSesamaBundle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TransferAntarBankHomeFragment : Fragment(), OnSavedItemAntarClickListener {
    private var _binding: FragmentTransferAntarBankHomeBinding? = null
    private val binding get() = _binding!!

    private val transferAntarBankHomeViewModel: TransferAntarBankHomeViewModel by inject()
    private val daftarTersimpanAntarAdapter = DaftarTersimpanAntarAdapter(this)

    private var hasAnnouncedScreen = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferAntarBankHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewDaftarTersimpanEmpty.root.visibility = View.GONE
        binding.rvDaftartersimpan.adapter = daftarTersimpanAntarAdapter

        setupListeners()
        observer()

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_transfer_antar_bank))
            hasAnnouncedScreen = true
        }
    }

    private fun setupListeners() {
        binding.btnInput.setOnClickListener {
            findNavController().navigate(R.id.action_transferAntarBankHome_to_cekRekeningAntarBankFormFragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transferAntarBankHomeViewModel.transferAntarHomeUiState.collectLatest { uiState ->
                    uiState.data?.let { daftarTersimpan ->
                        daftarTersimpanAntarAdapter.submitList(daftarTersimpan)
                        daftarTersimpan.forEach { item ->
                            updateAccessibilityInfo(item)
                        }
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.GONE
                    }

                    if (uiState.data.isNullOrEmpty()) {
                        daftarTersimpanAntarAdapter.submitList(null)
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.etCariDaftarTersimpan.editText?.doAfterTextChanged { inputText ->
            transferAntarBankHomeViewModel.cariDaftarTersimpanAntar(inputText.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSavedItemAntarClicked(daftarTersimpan: DaftarTersimpanAntar) {
        val dataRekening = CekRekeningAntarBundle(daftarTersimpan.idBank, daftarTersimpan.namaBank, daftarTersimpan.namaPemilikRekening, daftarTersimpan.noRekening)

        val bundle = Bundle().apply {
            putParcelable(Constant.DATA_REKENING_ANTAR_BUNDLE, dataRekening)
            putBoolean(Constant.DAFTAR_TERSIMPAN_SELECTED_MODE, true)
        }

        if(findNavController().currentDestination?.id == R.id.transferAntarBankHome) {
            findNavController().navigate(R.id.action_transferAntarBankHome_to_transferAntarBankForm, bundle)
        }
    }

    private fun updateAccessibilityInfo(daftarTersimpan: DaftarTersimpanAntar) {
        val accountNumber = daftarTersimpan.noRekening
        val accountNumberWithSpaces = accountNumber.replace("", " ").trim()
        val accessibilityText = getString(R.string.desc_daftar_tersimpan, daftarTersimpan.namaPemilikRekening, accountNumberWithSpaces)
        daftarTersimpanAntarAdapter.setAccessibilityText(daftarTersimpan, accessibilityText)
    }
}