package com.finsera.presentation.fragments.akun

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.finsera.common.utils.extension.copyToClipboard
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FragmentAccountBinding
import com.finsera.presentation.fragments.akun.viewmodel.AccountViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val accountViewModel: AccountViewModel by inject()

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
        setUpProfileInfo()
        clipBoardCardNumber()

        val cardAkun = view.findViewById<MaterialCardView>(R.id.card_akun)
        cardAkun.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_profileFragment)
        }

        val logout = view.findViewById<LinearLayout>(R.id.logout)
        logout.setOnClickListener {
            logoutUser()
        }

        val notifikasi = view.findViewById<LinearLayout>(R.id.notifikasi)
        notifikasi.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_notificationFragment)
        }

        val gantiPin = view.findViewById<LinearLayout>(R.id.ubah_pin_app)
        gantiPin.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_ubahMPINFragment)
        }

        val gantiPinTransaksi = view.findViewById<LinearLayout>(R.id.ubah_pin_transaksi)
        gantiPinTransaksi.setOnClickListener {
            gantiPinTransaksiAlert()
        }

        val gantiPasswordAkun = view.findViewById<LinearLayout>(R.id.ubah_password_akun)
        gantiPasswordAkun.setOnClickListener {
            gantiPasswordAkunAlert()
        }
    }

    private fun clipBoardCardNumber(){
        binding.cardAkun.btnCopy.setOnClickListener {
            val cardNumber = binding.cardAkun.tvRekening.text.toString()

            requireContext().copyToClipboard(cardNumber)
            Toast.makeText(requireActivity(),
                getString(R.string.succes_clipboard_card_number), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpProfileInfo() {
        binding.cardAkun.tvNama.text = accountViewModel.userInfo?.first
        binding.cardAkun.tvRekening.text = accountViewModel.userInfo?.second
    }

    private fun setUpBottomNavBar() {
        binding.bottomNavigationView.background = null
        binding.bottomAppBar.background.setTint(Color.WHITE)
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
                    findNavController().navigate(R.id.action_accountFragment_to_mutasiFragment)
                    false
                }

                R.id.menu_navbar_qris -> {
                    false
                }

                R.id.menu_navbar_favorit -> {
                    findNavController().navigate(R.id.action_accountFragment_to_favoritFragment)
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

    private fun logoutUser() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Logout dari Akun")
            .setMessage(getString(R.string.finsera_app_logout_desc))
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
                Toast.makeText(requireActivity(), "Anda membatalkan untuk logout dari akun.", Toast.LENGTH_SHORT).show()
            }
            .setPositiveButton("Ya") { dialog, which ->
                accountViewModel.logout()
                Toast.makeText(requireActivity(), "Logout berhasil", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
            }
            .show()
    }

    private fun gantiPinTransaksiAlert() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Ganti PIN Transaksi")
            .setMessage(getString(R.string.finsera_ganti_pin_transaksi_desc))
            .setPositiveButton("Ya") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun gantiPasswordAkunAlert() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Ganti Password Akun")
            .setMessage(getString(R.string.finsera_lupa_password_desc))
            .setPositiveButton("Ya") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}