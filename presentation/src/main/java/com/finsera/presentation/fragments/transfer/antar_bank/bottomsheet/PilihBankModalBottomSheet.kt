package com.finsera.presentation.fragments.transfer.antar_bank.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.finsera.domain.model.Bank
import com.finsera.presentation.R
import com.finsera.presentation.adapters.ListBankAdapter
import com.finsera.presentation.adapters.OnBankItemClickListener
import com.finsera.presentation.databinding.FragmentCekRekeningAntarBankFormBinding
import com.finsera.presentation.databinding.FragmentPilihBankModalBottomSheetBinding
import com.finsera.presentation.fragments.transfer.antar_bank.viewmodel.CekRekeningAntarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel

class PilihBankModalBottomSheet : BottomSheetDialogFragment(), OnBankItemClickListener {
    private var _binding: FragmentPilihBankModalBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val cekRekeningAntarViewModel by koinNavGraphViewModel<CekRekeningAntarViewModel>(R.id.finsera_app_navgraph)
    private val listBankRVAdapter = ListBankAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPilihBankModalBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = binding.rvListbank.layoutManager as LinearLayoutManager
        binding.rvListbank.layoutManager = layoutManager
        binding.rvListbank.adapter = listBankRVAdapter
        cekRekeningAntarViewModel.getListBank()

        dialog?.setCanceledOnTouchOutside(false)
        binding.btnCloseBottomSheet.setOnClickListener {
            dismiss()
        }

        observer()
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cekRekeningAntarViewModel.listBankModalSheetUiState.collectLatest { uiState ->
                    if(uiState.isSuccess) {
                        listBankRVAdapter.submitList(uiState.data)
                    }
                }
            }
        }
    }

    override fun onBankItemClicked(bank: Bank) {
        cekRekeningAntarViewModel.setBankDetails(bank.idBank, bank.namaBank)
        dismiss()
    }

}