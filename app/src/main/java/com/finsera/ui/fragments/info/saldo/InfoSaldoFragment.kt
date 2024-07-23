package com.finsera.ui.fragments.info.saldo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finsera.databinding.FragmentInfoSaldoBinding


class InfoSaldoFragment : Fragment() {

    private var _binding: FragmentInfoSaldoBinding? = null
    private val binding get() = _binding!!


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

}