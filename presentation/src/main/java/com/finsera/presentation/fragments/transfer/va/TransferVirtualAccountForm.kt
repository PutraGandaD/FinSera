package com.finsera.presentation.fragments.transfer.va

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferVirtualAccountFormBinding

class TransferVirtualAccountForm : Fragment() {
    private var _binding: FragmentTransferVirtualAccountFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferVirtualAccountFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLanjut.setOnClickListener {
            findNavController().navigate(R.id.action_transferVirtualAccountForm_to_transferVirtualAccountFormKonfirmasi)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}