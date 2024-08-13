package com.finsera.presentation.fragments.info.saldo

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.extension.copyToClipboard
import com.finsera.common.utils.format.CurrencyFormatter
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentInfoSaldoBinding
import com.finsera.presentation.fragments.info.saldo.viewmodel.InfoSaldoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class InfoSaldoFragment : Fragment() {

    private var _binding: FragmentInfoSaldoBinding? = null
    private val binding get() = _binding!!

    private val infoSaldoViewModel: InfoSaldoViewModel by viewModel()

    private lateinit var tts: TextToSpeech

    private lateinit var accountName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.getDefault()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoSaldoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getInfoSaldo()

        binding.accountInfoContainer.setOnClickListener {
            speakAccountInfo()
        }
        buttonBack()
        clipBoardCardNumber()
    }


    private fun buttonBack(){
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showLoadingInfoSaldo() {
        binding.pbBalanceValue.visibility = View.VISIBLE
        binding.pbAccountNumberValue.visibility = View.VISIBLE
        binding.ibClipboard.visibility = View.GONE
    }


    private fun hideLoadingInfoSaldo() {
        binding.pbBalanceValue.visibility = View.GONE
        binding.pbAccountNumberValue.visibility = View.GONE
        binding.ibClipboard.visibility = View.VISIBLE
    }

    private fun getInfoSaldo() {

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                infoSaldoViewModel.saldoUiState.collectLatest { uiState ->
                    if (uiState.isLoading) {
                        showLoadingInfoSaldo()
                    } else {
                        hideLoadingInfoSaldo()
                        uiState.data?.let { saldo ->
                            binding.tvAccountNumberValue.text = saldo.accountNumber
                            binding.tvBalanceValue.text = StringBuilder().append("Rp ")
                                .append(CurrencyFormatter.formatCurrency(saldo.amount))
                            accountName = saldo.username

                        } ?: run {
                            showLoadingInfoSaldo()
                        }
                        uiState.message?.let {message->
                            Log.d("InfoSaldoFragment", message)
                        }
                    }
                }
            }
        }
    }

    private fun clipBoardCardNumber(){
        binding.ibClipboard.setOnClickListener {
            val cardNumber = binding.tvAccountNumberValue.text.toString()
            val cardNumberLabel = accountName

            requireContext().copyToClipboard(
                getString(
                    R.string.copy_to_clipboard,
                    cardNumberLabel,
                    cardNumber
                ))
            Snackbar.make(requireView(),
                getString(R.string.succes_clipboard_card_number), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun speakAccountInfo() {
        val balance = binding.tvBalanceValue.text.toString()
        val accountNumber = binding.tvAccountNumberValue.text.toString()

        val formattedBalance = balance.removePrefix("Rp ")

        val fullText = getString(R.string.account_info_speech, formattedBalance, accountNumber)
        tts.speak(fullText, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }

}