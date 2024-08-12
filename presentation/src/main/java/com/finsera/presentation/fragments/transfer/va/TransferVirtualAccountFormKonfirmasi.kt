package com.finsera.presentation.fragments.transfer.va

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finsera.presentation.databinding.FragmentTransferVirtualAccountFormKonfirmasiBinding

class TransferVirtualAccountFormKonfirmasi : Fragment() {
    private var _binding: FragmentTransferVirtualAccountFormKonfirmasiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferVirtualAccountFormKonfirmasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        // TODO
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}