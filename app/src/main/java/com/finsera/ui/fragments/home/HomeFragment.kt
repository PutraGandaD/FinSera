package com.finsera.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.finsera.R
import com.finsera.databinding.FragmentHomeBinding
import com.finsera.databinding.FragmentLoginPinBinding

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

        val infoSaldoButton = view.findViewById<ConstraintLayout>(R.id.btn_menu_infosaldo)
        infoSaldoButton?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_infoSaldoFragment)
        }
    }
}