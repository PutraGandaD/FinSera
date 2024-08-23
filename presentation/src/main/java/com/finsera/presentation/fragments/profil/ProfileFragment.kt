package com.finsera.presentation.fragments.profil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentProfileBinding
import com.finsera.presentation.fragments.profil.viewmodel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel : ProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.profileUiState.collectLatest { uiState ->
                    if(uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }

                    uiState.message?.let {
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                        profileViewModel.messageShown()
                    }

                    if(uiState.isSuccess) {
                        binding.nomorhandphoneEditText.setText(uiState.data?.noTelp)
                        binding.tvName.setText(uiState.data?.username)
                        binding.emailEditText.setText(uiState.data?.email)
                        binding.genderEditText.setText(uiState.data?.gender)
                    }
                }
            }
        }
    }
}