package com.finsera.ui.fragments.transfer.antar_bank

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finsera.R

class TransferAntarBankHome : Fragment() {

    companion object {
        fun newInstance() = TransferAntarBankHome()
    }

    private val viewModel: TransferAntarBankHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_transfer_antar_bank_home, container, false)
    }
}