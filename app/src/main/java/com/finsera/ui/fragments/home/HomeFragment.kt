package com.finsera.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_navbar_beranda -> {
                    Toast.makeText(requireActivity(), "Beranda", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_navbar_mutasi -> {
                    Toast.makeText(requireActivity(), "Mutasi", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_navbar_qris -> {
                    Toast.makeText(requireActivity(), "QRIS", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_navbar_favorit -> {
                    Toast.makeText(requireActivity(), "Favorit", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_navbar_akun -> {
                    Toast.makeText(requireActivity(), "Akun", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}