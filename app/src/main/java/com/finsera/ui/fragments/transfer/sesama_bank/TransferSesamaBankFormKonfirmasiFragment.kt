package com.finsera.ui.fragments.transfer.sesama_bank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finsera.R

class TransferSesamaBankFormKonfirmasiFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_transfer_sesama_bank_form_konfirmasi,
            container,
            false
        )
    }
}