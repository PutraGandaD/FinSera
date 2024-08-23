package com.finsera.presentation.fragments.akun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBottomNavBar()
    }

    private fun setUpBottomNavBar() {
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(0).isChecked = false
        binding.bottomNavigationView.menu.getItem(1).isCheckable = false
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.menu.getItem(3).isCheckable = false
        binding.bottomNavigationView.menu.getItem(4).isCheckable = true

        binding.bottomNavigationView.selectedItemId = R.id.menu_navbar_akun

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_navbar_beranda -> {
                    findNavController().popBackStack(R.id.homeFragment, false)
                    false
                }

                R.id.menu_navbar_mutasi -> {
                    findNavController().navigate(R.id.action_favoritFragment_to_mutasiFragment)
                    false
                }

                R.id.menu_navbar_qris -> {
                    false
                }

                R.id.menu_navbar_favorit -> {
                    true
                }

                R.id.menu_navbar_akun -> {
                    false
                }

                else -> {
                    false
                }
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_favoritFragment_to_qrisScanQRFragment)
        }
    }
}