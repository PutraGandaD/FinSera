package com.finsera.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.finsera.R
import com.finsera.databinding.FragmentLoginBinding
import com.finsera.ui.fragments.auth.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel : AuthViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_loginPinFragment)

            val username = binding.etUsername.editText?.text.toString()
            val password = binding.etPassword.editText?.text.toString()

            authViewModel.userLogin(username, password)
        }
    }
}