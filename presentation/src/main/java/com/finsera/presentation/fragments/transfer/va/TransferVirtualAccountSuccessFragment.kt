package com.finsera.presentation.fragments.transfer.va

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferVirtualAccountSuccessBinding


class TransferVirtualAccountSuccessFragment : Fragment() {

    private var _binding: FragmentTransferVirtualAccountSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferVirtualAccountSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardTransaksiBerhasil
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}