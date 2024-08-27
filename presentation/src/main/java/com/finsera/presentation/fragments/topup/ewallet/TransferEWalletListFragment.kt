package com.finsera.presentation.fragments.topup.ewallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.Constant
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentTransferEWalletListBinding
import com.finsera.presentation.fragments.topup.ewallet.bundle.ChooseEWalletBundle
import com.google.android.material.snackbar.Snackbar

class TransferEWalletListFragment : Fragment() {
    private var _binding: FragmentTransferEWalletListBinding? = null
    private val binding get() = _binding!!

    private var hasAnnouncedScreen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferEWalletListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_e_wallet_list))
            hasAnnouncedScreen = true
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnEWalletDana.root.setOnClickListener {
            val data = ChooseEWalletBundle(1, binding.btnEWalletDana.tvDana.text.toString())
            navigateToEWalletForm(data)
        }

        binding.btnEWalletOvo.root.setOnClickListener {
            val data = ChooseEWalletBundle(2, binding.btnEWalletOvo.tvOvo.text.toString())
            navigateToEWalletForm(data)
        }

        binding.btnEWalletShopeePay.root.setOnClickListener {
            val data =
                ChooseEWalletBundle(3, binding.btnEWalletShopeePay.tvShopeepay.text.toString())
            navigateToEWalletForm(data)
        }

        binding.btnEWalletGopay.root.setOnClickListener {
            val data = ChooseEWalletBundle(4, binding.btnEWalletGopay.tvGopay.text.toString())
            navigateToEWalletForm(data)
        }

        binding.btnEWalletPaypal.root.setOnClickListener {
            val data = ChooseEWalletBundle(5, "Paypal")
            navigateToEWalletForm(data)
        }
    }

    private fun navigateToEWalletForm(data: ChooseEWalletBundle) {
        if (findNavController().currentDestination?.id == R.id.transferEWalletListFragment) {
            val bundle = Bundle().apply {
                putParcelable(Constant.DATA_ID_EWALLET, data)
            }

            Snackbar.make(requireView(), "Memilih E-Wallet ${data.ewalletName}", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(
                R.id.action_transferEWalletListFragment_to_transferEWalletForm,
                bundle
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}