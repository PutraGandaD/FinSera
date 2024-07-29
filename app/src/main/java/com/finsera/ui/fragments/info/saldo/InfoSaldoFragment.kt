package com.finsera.ui.fragments.info.saldo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finsera.R
import com.finsera.common.utils.format.CurrencyFormatter
import com.finsera.databinding.FragmentInfoSaldoBinding
import com.finsera.ui.fragments.info.saldo.viewmodel.InfoSaldoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class InfoSaldoFragment : Fragment() {

    private var _binding: FragmentInfoSaldoBinding? = null
    private val binding get() = _binding!!

    private val infoSaldoViewModel: InfoSaldoViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        showLoadingInfoSaldo()

    }

    private fun showLoadingInfoSaldo() {
        infoSaldoViewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbBalanceValue.visibility = View.VISIBLE
                binding.pbAccountNumberValue.visibility = View.VISIBLE
                binding.ibClipboard.visibility = View.GONE
            } else {
                binding.pbBalanceValue.visibility = View.GONE
                binding.pbAccountNumberValue.visibility = View.GONE
                binding.ibClipboard.visibility = View.VISIBLE
            }
        }
    }

    private fun getInfoSaldo() {
        infoSaldoViewModel.saldo.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvAccountNumberValue.text = it.accountNumber
                binding.tvBalanceValue.text = StringBuilder().append("Rp ")
                    .append(CurrencyFormatter.formatCurrency(it.amount))
            } else {
                //body empty
                binding.tvAccountNumberValue.text = getString(R.string.tv_rekening_placeholder)
                binding.tvBalanceValue.text = getString(R.string.amount_example)
            }

        }
    }

}