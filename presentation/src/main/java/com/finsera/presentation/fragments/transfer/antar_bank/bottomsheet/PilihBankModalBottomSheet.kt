package com.finsera.presentation.fragments.transfer.antar_bank.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finsera.presentation.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PilihBankModalBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pilih_bank_modal_bottom_sheet, container, false)
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

}