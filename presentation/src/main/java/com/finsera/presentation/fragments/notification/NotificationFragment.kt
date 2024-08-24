package com.finsera.presentation.fragments.notification

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.finsera.presentation.R
import com.finsera.presentation.adapters.MutasiAdapter
import com.finsera.presentation.adapters.NotifAdapter
import com.finsera.presentation.databinding.FragmentNotificationBinding
import com.finsera.presentation.fragments.notification.viewmodel.NotificationViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val notifViewModel: NotificationViewModel by viewModel()

    private val notifAdapter = NotifAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        notifViewModel.getNotifikasi()
        observer()
        setUpRvMutasi()

        binding.rvNotification.isClickable = false
    }

    private fun setUpRvMutasi() {
        binding.rvNotification.adapter = notifAdapter
        binding.rvNotification.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD)
                info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD)
            }
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                notifViewModel.notifUiState.collectLatest { uiState ->

                    if (uiState.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }

                    uiState.message?.let {
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                        notifViewModel.messageShown()
                    }

                    if (uiState.isSuccess) {
                        if (uiState.notif != null) {
                            binding.rvNotification.visibility = View.VISIBLE
                            notifAdapter.submitList(uiState.notif)
                        } else {
                            binding.ivNoData.visibility = View.VISIBLE
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                    }

                    if (uiState.isError) {
                        binding.ivNoData.visibility = View.VISIBLE
                        binding.tvNoData.visibility = View.VISIBLE
                        binding.tvNoData.text = getString(R.string.terjadi_kesalahan_pada_server)
                    }
                }
            }
        }
    }

}