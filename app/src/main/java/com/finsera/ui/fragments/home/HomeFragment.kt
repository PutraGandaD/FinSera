package com.finsera.ui.fragments.home

import android.os.Bundle
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
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.shape.MaterialShapeDrawable

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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