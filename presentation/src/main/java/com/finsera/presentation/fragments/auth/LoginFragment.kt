package com.finsera.presentation.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentLoginBinding
import com.finsera.presentation.fragments.auth.viewmodels.LoginViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel : LoginViewModel by inject()

    private var hasAnnouncedScreen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoggedInStatus()

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.editText?.text.toString()
            val password = binding.etPassword.editText?.text.toString()

            if(username.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.userLogin(username, password)
            } else {
                Snackbar.make(requireView(), "Mohon untuk mengisi kolom Username dan Password sebelum melanjutkan.", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnForgotPassword.setOnClickListener{
            forgetPasswordAlert()
        }

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_login))
            hasAnnouncedScreen = true
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginScreenUIState.collectLatest { uiState ->
                    uiState.message?.let { message ->
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                        loginViewModel.userMessageShown()
                    }

                    if(uiState.isUserLoggedIn) {
                        if (findNavController().currentDestination?.id == R.id.loginFragment) {
                            handleAppPinCreated()
                        }
                    }

                    if(uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun handleAppPinCreated() {
        val userCreatedAppPinStatus = loginViewModel.userCreatedAppPin
        //Toast.makeText(requireActivity(), userCreatedAppPinStatus.toString(), Toast.LENGTH_SHORT).show()
        if(userCreatedAppPinStatus) {
            // user login and already created pin, straight to login pin screen
            findNavController().navigate(R.id.action_loginFragment_to_loginPinFragment)
        } else {
            // otherwise create pin first
            findNavController().navigate(R.id.action_loginFragment_to_createPinFragment)
        }
    }

    private fun observeLoggedInStatus() {
        loginViewModel.userLoggedInStatus.observe(viewLifecycleOwner) {
            if(it) {
                handleAppPinCreated()
            }
        }
    }

    private fun forgetPasswordAlert() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Lupa Password")
            .setMessage(getString(R.string.finsera_lupa_password_desc))
            .setPositiveButton("Ya") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}