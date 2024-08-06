package com.finsera.ui.fragments.transfer.sesama_bank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.R
import com.finsera.common.utils.Constant
import com.finsera.databinding.FragmentTransferSesamaBankFormBinding
import com.finsera.ui.fragments.transfer.sesama_bank.data.CekRekening
import com.finsera.ui.fragments.transfer.sesama_bank.viewmodel.CekRekeningSesamaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TransferSesamaBankFormFragment : Fragment() {
    private var _binding: FragmentTransferSesamaBankFormBinding? = null
    private val binding get() = _binding!!

    private val cekRekeningSesamaViewModel : CekRekeningSesamaViewModel by inject()

    private lateinit var dataTransferBundle : CekRekening

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferSesamaBankFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.checkboxSimpanKeDaftarTersimpan.isEnabled = false

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLanjut.setOnClickListener {
            val noRekening = binding.nomorEditText.text.toString()
            val nominal = binding.nominalEditText.text.toString().toDoubleOrNull()
            val catatan = binding.catatanEditText.text.toString()

            if(noRekening != null && nominal != null) {
                dataTransferBundle = CekRekening(noRekening, nominal, catatan)
                cekRekeningSesamaViewModel.cekRekeningSesama(noRekening)
            } else {
                Snackbar.make(requireView(), "Mohon isi kolom No Rekening dan nominal terlebih dahulu!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cekRekeningSesamaViewModel.cekRekeningSesamaUiState.collectLatest { uiState ->
                    uiState.message?.let { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                        cekRekeningSesamaViewModel.messageShown()
                    }

                    if(uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }

                    if(uiState.isValid) {
                        if(findNavController().currentDestination?.id == R.id.transferSesamaBankForm) {
                            val bundle = Bundle().apply {
                                putParcelable(Constant.TRANSFER_SESAMA_BUNDLE, dataTransferBundle)
                            }

                            findNavController().navigate(R.id.action_transferSesamaBankForm_to_transferSesamaBankFormKonfirmasi, bundle)
                            cekRekeningSesamaViewModel.redirectedToKonfirmasiForm()
                        }
                    }
                }
            }
        }
    }
}