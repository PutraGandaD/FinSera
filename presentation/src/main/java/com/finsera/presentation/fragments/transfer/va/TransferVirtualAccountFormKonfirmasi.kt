package com.finsera.presentation.fragments.transfer.va

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finsera.common.utils.Constant
import com.finsera.common.utils.format.CurrencyFormatter
import com.finsera.presentation.databinding.FragmentTransferVirtualAccountFormKonfirmasiBinding
import com.finsera.presentation.fragments.transfer.va.bundle.CekVaBundle

class TransferVirtualAccountFormKonfirmasi : Fragment() {
    private var _binding: FragmentTransferVirtualAccountFormKonfirmasiBinding? = null
    private val binding get() = _binding!!

    private lateinit var accountNum :String
    private lateinit var accountName :String
    private var nominal : Int =0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferVirtualAccountFormKonfirmasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()




        //bundle
        val bundle = arguments?.getParcelable<CekVaBundle>(Constant.DATA_VA_BUNDLE)
        if (bundle != null) {
            accountNum = bundle.vaAccountNum
            accountName = bundle.vaAccountName
            nominal = bundle.vaTransferNominal
        }

        binding.tvNoVA.text = accountNum
        binding.tvNamaVA.text = accountName
        binding.tvNominalAwal.text = StringBuilder().append("Rp ").append(CurrencyFormatter.formatCurrency(nominal.toDouble()))

    }



    private fun setupListeners() {
        // TODO
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}