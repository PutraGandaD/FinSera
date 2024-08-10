package com.finsera.presentation.fragments.topup.ewallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferEWalletListBinding

class TransferEWalletListFragment : Fragment() {
    private var _binding: FragmentTransferEWalletListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferEWalletListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

//        binding.btnEWalletGopay.root.setOnClickListener {
//            navigateToEWalletForm()
//        }
//
//        binding.btnEWalletDana.root.setOnClickListener {
//            navigateToEWalletForm()
//        }
//
//        binding.btnEWalletOvo.root.setOnClickListener {
//            navigateToEWalletForm()
//        }
//
//        binding.btnEWalletShopeePay.root.setOnClickListener {
//            navigateToEWalletForm()
//        }
//
//        binding.btnEWalletPaypal.root.setOnClickListener {
//            navigateToEWalletForm()
//        }
    }

    private fun navigateToEWalletForm() {
        findNavController().navigate(R.id.action_transferEWalletListFragment_to_transferEWalletForm)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}