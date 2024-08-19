package com.finsera.presentation.fragments.transfer.antar_bank

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferAntarBankHomeBinding

class TransferAntarBankHomeFragment : Fragment() {
    private var _binding: FragmentTransferAntarBankHomeBinding? = null
    private val binding get() = _binding!!

    private var hasAnnouncedScreen = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferAntarBankHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_transfer_antar_bank))
            hasAnnouncedScreen = true
        }
    }

    private fun setupListeners() {
        binding.btnInput.setOnClickListener {
            findNavController().navigate(R.id.action_transferAntarBankHome_to_cekRekeningAntarBankFormFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}