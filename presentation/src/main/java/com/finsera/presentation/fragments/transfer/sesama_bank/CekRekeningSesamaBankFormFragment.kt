package com.finsera.presentation.fragments.transfer.sesama_bank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentCekRekeningSesamaBankBinding
import com.finsera.presentation.fragments.transfer.sesama_bank.viewmodel.CekRekeningSesamaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CekRekeningSesamaBankFormFragment : Fragment() {
    private var _binding: FragmentCekRekeningSesamaBankBinding? = null
    private val binding get() = _binding!!

    private val cekRekeningSesamaViewModel : CekRekeningSesamaViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCekRekeningSesamaBankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLanjut.setOnClickListener {
            val noRekeningEt = binding.nomorEditText.text.toString()

            if(noRekeningEt.isNotEmpty()) {
                cekRekeningSesamaViewModel.cekRekeningSesama(noRekeningEt)
            } else {
                Snackbar.make(requireView(), "Mohon isi kolom No Rekening terlebih dahulu!", Snackbar.LENGTH_SHORT).show()
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
                        if(findNavController().currentDestination?.id == R.id.cekRekeningSesamaBankFormFragment) {
                            val bundle = Bundle().apply {
                                putString("recipientName", uiState.data?.recipientName)
                                putString("accountnumRecipient", uiState.data?.accountnumRecipient)
                            }

                            findNavController().navigate(R.id.action_cekRekeningSesamaBankFormFragment_to_transferSesamaBankFormFragment, bundle)

                            cekRekeningSesamaViewModel.redirectedToKonfirmasiForm()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}