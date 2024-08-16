package com.finsera.presentation.fragments.transfer.antar_bank

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentCekRekeningAntarBankFormBinding
import com.finsera.presentation.fragments.transfer.antar_bank.bottomsheet.PilihBankModalBottomSheet
import com.finsera.presentation.fragments.transfer.antar_bank.viewmodel.CekRekeningAntarViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HALF_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel

class CekRekeningAntarBankFormFragment : Fragment() {
    private var _binding: FragmentCekRekeningAntarBankFormBinding? = null
    private val binding get() = _binding!!

    private val cekRekeningAntarViewModel by koinNavGraphViewModel<CekRekeningAntarViewModel>(R.id.finsera_app_navgraph)

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

        initPilihBankBottomSheet()
        handleBackButton()
        observer()

        binding.btnLanjut.setOnClickListener {
            val idBank = cekRekeningAntarViewModel.idBankSelected
            val noRekening = binding.etNomorRekening.editableText.toString()

            if(noRekening.isNotEmpty()) {
                Toast.makeText(requireActivity(), "$idBank $noRekening", Toast.LENGTH_SHORT).show()
            } else {
                Snackbar.make(requireView(), "Kolom Nomor Rekening wajib diisi untuk melanjutkan", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun initPilihBankBottomSheet() {
        val pilihBankModalBottomSheet = PilihBankModalBottomSheet()
        pilihBankModalBottomSheet.isCancelable = false

        binding.etPilihBank.inputType = InputType.TYPE_NULL
        binding.etPilihBank.isFocusable = false
        binding.etPilihBank.isCursorVisible = false
        binding.etPilihBank.isClickable = true

        binding.etPilihBank.setOnClickListener {
            pilihBankModalBottomSheet.show(childFragmentManager, PilihBankModalBottomSheet.TAG)
        }
    }

    private fun handleBackButton() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
            cekRekeningAntarViewModel.resetBankDetails()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                cekRekeningAntarViewModel.resetBankDetails()
            }
        })
    }

    private fun observer() {
        cekRekeningAntarViewModel.namaBankSelected.observe(viewLifecycleOwner) { data ->
            data?.let {
                binding.etPilihBank.setText(it)
                binding.layoutnomorrekening.visibility = View.VISIBLE
            } ?: run {
                binding.etPilihBank.setText("Pilih Bank")
                binding.layoutnomorrekening.visibility = View.INVISIBLE
            }
        }
    }


}