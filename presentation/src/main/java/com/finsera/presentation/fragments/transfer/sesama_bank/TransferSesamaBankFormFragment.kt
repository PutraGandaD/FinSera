package com.finsera.presentation.fragments.transfer.sesama_bank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Constant
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferSesamaBankFormBinding
import com.finsera.presentation.fragments.transfer.sesama_bank.bundle.CekRekeningSesamaBundle

class TransferSesamaBankFormFragment : Fragment() {
    private var _binding :  FragmentTransferSesamaBankFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var noRekening : String
    private lateinit var namaPemilikRekening : String
    private var addToDaftarTersimpan : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTransferSesamaBankFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.checkboxDaftarTersimpanSesama.setOnCheckedChangeListener { _, isChecked ->
            addToDaftarTersimpan = isChecked
        }

        val bundle = arguments?.getParcelable<CekRekeningSesamaBundle>(Constant.DATA_REKENING_SESAMA_BUNDLE)
        val savedItemMode = arguments?.getBoolean(Constant.DAFTAR_TERSIMPAN_SELECTED_MODE)

        if(bundle != null) {
            noRekening = bundle.noRekening
            namaPemilikRekening = bundle.namaPemilikRekening
            binding.cardInfoRekening.tvNoRekening.text = noRekening
            binding.cardInfoRekening.tvNamaPenerima.text = namaPemilikRekening
        }

        if(savedItemMode == true) {
//            binding.checkboxDaftarTersimpanSesama.apply {
//                isEnabled = false
//                isChecked = false
//            }
            binding.layoutdaftartersimpan.visibility = View.GONE
        }

        val dataRekening = CekRekeningSesamaBundle(namaPemilikRekening, noRekening)
        binding.btnNext.setOnClickListener {
            if(binding.etNominal.text.toString().isNotEmpty()) {
                if(findNavController().currentDestination?.id == R.id.transferSesamaBankFormFragment) {
                    val nominalTransfer = binding.etNominal.text.toString()
                    val catatanTransfer = if(binding.etCatatan.text.toString().isEmpty()) "-" else binding.etCatatan.text.toString()

                    val bundle = Bundle().apply {
                        putParcelable(Constant.DATA_REKENING_SESAMA_BUNDLE, dataRekening)
                        putString(Constant.NOMINAL_TRANSFER_EXTRA, nominalTransfer)
                        putString(Constant.CATATAN_TRANSFER_EXTRA, catatanTransfer)
                        putBoolean(Constant.DAFTAR_TERSIMPAN_CHECKED_EXTRA, addToDaftarTersimpan)
                    }

                    findNavController().navigate(R.id.action_transferSesamaBankFormFragment_to_transferSesamaBankFormKonfirmasiFragment, bundle)
                }
            } else {
                Toast.makeText(requireActivity(), "Isi Kolom Nominal Transfer Terlebih Dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}