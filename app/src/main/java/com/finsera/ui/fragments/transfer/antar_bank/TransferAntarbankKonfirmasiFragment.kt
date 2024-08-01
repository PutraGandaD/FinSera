package com.finsera.ui.fragments.transfer.antar_bank

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finsera.R
import com.finsera.databinding.FragmentTransferAntarBankFormKonfirmasiBinding
import com.finsera.databinding.FragmentTransferVirtualAccountFormKonfirmasiBinding

class TransferAntarbankKonfirmasiFragment : Fragment() {
    private var _binding: FragmentTransferAntarBankFormKonfirmasiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferAntarBankFormKonfirmasiBinding.inflate(inflater, container, false)
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

//    companion object {
//        fun newInstance() = TransferAntarbankKonfirmasiFragment()
//    }
//
//    private val viewModel: TransferAntarbankFormKonfirmasiViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // TODO: Use the ViewModel
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(
//            R.layout.fragment_transfer_antar_bank_form_konfirmasi,
//            container,
//            false
//        )
//    }
//}