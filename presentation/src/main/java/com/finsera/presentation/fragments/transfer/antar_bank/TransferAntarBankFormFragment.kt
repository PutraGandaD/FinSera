package com.finsera.presentation.fragments.transfer.antar_bank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferAntarBankFormBinding

class TransferAntarBankFormFragment : Fragment() {
    private var _binding: FragmentTransferAntarBankFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferAntarBankFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLanjut.setOnClickListener {
            findNavController().navigate(R.id.action_transferAntarBankForm_to_transferAntarBankFormKonfirmasi)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}