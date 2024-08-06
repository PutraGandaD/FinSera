package com.finsera.ui.fragments.transfer.sesama_bank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.finsera.R
import com.finsera.databinding.FragmentHomeBinding
import com.finsera.databinding.FragmentTransferSesamaBankHomeBinding

class TransferSesamaBankHomeFragment : Fragment() {
    private var _binding: FragmentTransferSesamaBankHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferSesamaBankHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnTransferBaru.setOnClickListener {
            findNavController().navigate(R.id.action_transferSesamaBankHome_to_transferSesamaBankForm)
        }
    }
}