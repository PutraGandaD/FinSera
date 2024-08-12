package com.finsera.presentation.fragments.transfer.sesama_bank

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.finsera.common.utils.Constant
import com.finsera.domain.model.DaftarTersimpan
import com.finsera.presentation.R
import com.finsera.presentation.adapters.DaftarTersimpanSesamaAdapter
import com.finsera.presentation.adapters.OnItemClickListener
import com.finsera.presentation.databinding.FragmentTransferSesamaBankHomeBinding
import com.finsera.presentation.fragments.transfer.sesama_bank.bundle.CekRekeningSesamaBundle
import com.finsera.presentation.fragments.transfer.sesama_bank.viewmodel.TransferSesamaBankViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TransferSesamaBankHomeFragment : Fragment(), OnItemClickListener {
    private var _binding: FragmentTransferSesamaBankHomeBinding? = null
    private val binding get() = _binding!!

    private val transferSesamaBankViewModel : TransferSesamaBankViewModel by inject()
    private val daftarTersimpanSesamaAdapter = DaftarTersimpanSesamaAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransferSesamaBankHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDaftartersimpan.adapter = daftarTersimpanSesamaAdapter

        observer()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnTransferBaru.setOnClickListener {
            findNavController().navigate(R.id.action_transferSesamaBankHome_to_cekRekeningSesamaBankFormFragment)
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                transferSesamaBankViewModel.transferSesamaHomeUiState.collectLatest { uiState ->
                    if(uiState.data.isNotEmpty()) {
                        daftarTersimpanSesamaAdapter.submitList(uiState.data)
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.GONE
                        Toast.makeText(requireActivity(), "Daftar tersimpan ditemukan", Toast.LENGTH_SHORT).show()
                    } else {
                        daftarTersimpanSesamaAdapter.submitList(emptyList())
                        binding.viewDaftarTersimpanEmpty.root.visibility = View.VISIBLE
                        Toast.makeText(requireActivity(), "Daftar tersimpan tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.etCariDaftarTersimpan.editText?.doOnTextChanged { inputText, _, _, _ ->
             transferSesamaBankViewModel.cariDaftarTersimpanSesama(inputText.toString())
        }
    }

    override fun onSavedItemClicked(daftarTersimpan: DaftarTersimpan) {
        val dataRekening = CekRekeningSesamaBundle(daftarTersimpan.namaPemilikRekening, daftarTersimpan.noRekening)

        val bundle = Bundle().apply {
            putParcelable(Constant.DATA_REKENING_SESAMA_BUNDLE, dataRekening)
            putBoolean(Constant.DAFTAR_TERSIMPAN_SELECTED_MODE, true)
        }

        if(findNavController().currentDestination?.id == R.id.transferSesamaBankHome) {
            findNavController().navigate(R.id.action_transferSesamaBankHome_to_transferSesamaBankFormFragment, bundle)
        }
    }
}