package com.finsera.ui.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.finsera.R
import com.finsera.databinding.FragmentHomeBinding
import com.finsera.databinding.FragmentLoginBinding
import com.finsera.ui.fragments.home.viewmodel.HomeViewModel
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.shape.MaterialShapeDrawable
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.finsera.common.utils.format.CurrencyFormatter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBottomNavBar()

        val btnInfoSaldo = view.findViewById<ConstraintLayout>(R.id.btn_menu_infosaldo)
        btnInfoSaldo?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_infoSaldoFragment)
        }


        getInfoSaldo()
        showLoadingInfoSaldo()
        visibiliySaldo()
    }

    private fun getInfoSaldo() {
        homeViewModel.saldo.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvTopbgAccountName.text = it.username
                binding.cardNasabahInfo.tvNamaNasabah.text = it.username
                binding.cardNasabahInfo.tvNoRekeningCard.text = it.accountNumber
                if (homeViewModel.isSaldoVisible.value == true) {
                    binding.cardNasabahInfo.tvSaldoRekeningCard.text = StringBuilder().append("Rp ").append(CurrencyFormatter.formatCurrency(it.amount))
                } else {
                    binding.cardNasabahInfo.tvSaldoRekeningCard.text = this.getString(R.string.tv_saldo_card_rekening_home)
                }
            } else {
                binding.tvTopbgAccountName.text = "Rama"
                binding.cardNasabahInfo.tvNamaNasabah.text = "Rama"
                binding.cardNasabahInfo.tvNoRekeningCard.text = "0859313131732"
                binding.cardNasabahInfo.tvSaldoRekeningCard.text = getString(R.string.tv_saldo_card_rekening_home)
            }

        }
    }

    private fun showLoadingInfoSaldo() {
        homeViewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBarCard.visibility = View.VISIBLE
                binding.progressBarTopName.visibility = View.VISIBLE
                binding.cardNasabahInfo.btnNorekCopy.visibility = View.GONE
                binding.cardNasabahInfo.btnSaldoVisibility.visibility = View.GONE
                binding.cardNasabahInfo.tvSaldoRekeningCard.visibility = View.GONE
            } else {
                binding.progressBarCard.visibility = View.GONE
                binding.progressBarTopName.visibility = View.GONE
                binding.cardNasabahInfo.btnNorekCopy.visibility = View.VISIBLE
                binding.cardNasabahInfo.btnSaldoVisibility.visibility = View.VISIBLE
                binding.cardNasabahInfo.tvSaldoRekeningCard.visibility = View.VISIBLE
            }
        }
    }

    private fun visibiliySaldo() {
        binding.cardNasabahInfo.btnSaldoVisibility.setOnClickListener {
            homeViewModel.toggleSaldoVisibility()
        }

        homeViewModel.isSaldoVisible.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) {
                homeViewModel.saldo.value?.let {
                    binding.cardNasabahInfo.tvSaldoRekeningCard.text = StringBuilder().append("Rp ").append(CurrencyFormatter.formatCurrency(it.amount))
                }
                binding.cardNasabahInfo.btnSaldoVisibility.setImageResource(R.drawable.ic_rekening_no_visibility)
            } else {
                binding.cardNasabahInfo.tvSaldoRekeningCard.text = getString(R.string.tv_saldo_card_rekening_home)
                binding.cardNasabahInfo.btnSaldoVisibility.setImageResource(R.drawable.ic_rekening_visibility)
            }
        }
    }


    private fun setUpBottomNavBar() {
        // set background for bottomNavigationView to null
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(0).isChecked = true
        binding.bottomNavigationView.menu.getItem(1).isCheckable = false
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.menu.getItem(3).isCheckable = false
        binding.bottomNavigationView.menu.getItem(4).isCheckable = false

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_navbar_beranda -> {
                    true
                }

                R.id.menu_navbar_mutasi -> {
                    findNavController().navigate(R.id.action_homeFragment_to_mutasiFragment)
                    false
                }

                R.id.menu_navbar_qris -> {

                    false
                }

                R.id.menu_navbar_favorit -> {
                    false
                }

                R.id.menu_navbar_akun -> {
                    false
                }

                else -> {
                    false
                }
            }
        }
    }
}