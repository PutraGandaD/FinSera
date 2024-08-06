package com.finsera.ui.fragments.transfer.va

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.finsera.R
import com.finsera.databinding.FragmentTransferSesamaBankHomeBinding
import com.finsera.databinding.FragmentTransferVirtualAccountHomeBinding

class TransferVirtualAccountHome : Fragment() {
    private var _binding: FragmentTransferVirtualAccountHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferVirtualAccountHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
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
}