package com.finsera.presentation.fragments.topup.ewallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferEWalletHomeBinding

class TransferEWalletHomeFragment : Fragment() {
    private var _binding: FragmentTransferEWalletHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferEWalletHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
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
}
