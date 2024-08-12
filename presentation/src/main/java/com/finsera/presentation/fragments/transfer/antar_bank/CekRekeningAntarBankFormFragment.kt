package com.finsera.presentation.fragments.transfer.antar_bank

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentCekRekeningAntarBankFormBinding
import com.finsera.presentation.fragments.transfer.antar_bank.bottomsheet.PilihBankModalBottomSheet

class CekRekeningAntarBankFormFragment : Fragment() {
    private var _binding: FragmentCekRekeningAntarBankFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCekRekeningAntarBankFormBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pilihBankModalBottomSheet = PilihBankModalBottomSheet()

        binding.etPilihBank.inputType = InputType.TYPE_NULL
        binding.etPilihBank.isFocusable = false
        binding.etPilihBank.isCursorVisible = false
        binding.etPilihBank.isClickable = true

        binding.etPilihBank.setOnClickListener {
            pilihBankModalBottomSheet.show(childFragmentManager, PilihBankModalBottomSheet.TAG)
        }
    }
}