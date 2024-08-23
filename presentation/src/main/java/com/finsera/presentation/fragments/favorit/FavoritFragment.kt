package com.finsera.presentation.fragments.favorit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.finsera.common.utils.Constant
import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanEWallet
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.model.DaftarTersimpanVa
import com.finsera.presentation.R
import com.finsera.presentation.adapters.FavoritEWalletAdapter
import com.finsera.presentation.adapters.FavoritTransferAntarAdapter
import com.finsera.presentation.adapters.FavoritTransferSesamaAdapter
import com.finsera.presentation.adapters.FavoritVirtualAccountAdapter
import com.finsera.presentation.adapters.OnFavoriteItemAntarClickListener
import com.finsera.presentation.adapters.OnFavoriteItemEWalletClickListener
import com.finsera.presentation.adapters.OnFavoriteItemSesamaClickListener
import com.finsera.presentation.adapters.OnFavoriteItemVaClickListener
import com.finsera.presentation.databinding.FragmentFavoritBinding
import com.finsera.presentation.fragments.favorit.viewmodel.FavoritViewModel
import com.finsera.presentation.fragments.topup.ewallet.bundle.CekEWalletBundle
import com.finsera.presentation.fragments.transfer.antar_bank.bundle.CekRekeningAntarBundle
import com.finsera.presentation.fragments.transfer.sesama_bank.bundle.CekRekeningSesamaBundle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FavoritFragment : Fragment(), OnFavoriteItemSesamaClickListener, OnFavoriteItemAntarClickListener, OnFavoriteItemEWalletClickListener, OnFavoriteItemVaClickListener {
    private var _binding: FragmentFavoritBinding? = null
    private val binding get() = _binding!!

    private val favoritViewModel : FavoritViewModel by inject()

    private val favoritTransferSesamaAdapter = FavoritTransferSesamaAdapter(this)
    private val favoritTransferAntarAdapter = FavoritTransferAntarAdapter(this)
    private val favoritEWalletAdapter = FavoritEWalletAdapter(this)
    private val favoritVirtualAccountAdapter = FavoritVirtualAccountAdapter(this)

    private var hasAnnouncedScreen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBottomNavBar()
        setUpRv()
        observer()

        if (!hasAnnouncedScreen) {
            view.announceForAccessibility(getString(R.string.screen_favorit))
            hasAnnouncedScreen = true
        }

        binding.rvTersimpanTfSesama.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        binding.rvTersimpanTfAntar.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        binding.rvTersimpanEwallet.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        binding.rvTersimpanVa.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES


    }

    private fun setUpRv() {
        binding.rvTersimpanTfSesama.adapter = favoritTransferSesamaAdapter
        binding.rvTersimpanTfAntar.adapter = favoritTransferAntarAdapter
        binding.rvTersimpanEwallet.adapter = favoritEWalletAdapter
        binding.rvTersimpanVa.adapter = favoritVirtualAccountAdapter
    }

    private fun observer() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoritViewModel.favoritUiState.collectLatest { uiState ->
                    if(!uiState.dataFavoritSesama.isNullOrEmpty()) {
                        binding.rvTersimpanTfSesama.visibility = View.VISIBLE
                        binding.tvFavoritSesamaEmpty.visibility = View.GONE
                        favoritTransferSesamaAdapter.submitList(uiState.dataFavoritSesama)
                    } else {
                        binding.rvTersimpanTfSesama.visibility = View.GONE
                        binding.tvFavoritSesamaEmpty.visibility = View.VISIBLE
                        favoritTransferSesamaAdapter.submitList(null)
                    }

                    if(!uiState.dataFavoritAntar.isNullOrEmpty()) {
                        binding.rvTersimpanTfAntar.visibility = View.VISIBLE
                        binding.tvFavoritAntarEmpty.visibility = View.GONE
                        favoritTransferAntarAdapter.submitList(uiState.dataFavoritAntar)
                    } else {
                        binding.rvTersimpanTfAntar.visibility = View.GONE
                        binding.tvFavoritAntarEmpty.visibility = View.VISIBLE
                        favoritTransferAntarAdapter.submitList(null)
                    }

                    if(!uiState.dataFavoritEWallet.isNullOrEmpty()) {
                        binding.rvTersimpanEwallet.visibility = View.VISIBLE
                        binding.tvFavoritEwalletEmpty.visibility = View.GONE
                        favoritEWalletAdapter.submitList(uiState.dataFavoritEWallet)
                    } else {
                        binding.rvTersimpanEwallet.visibility = View.GONE
                        binding.tvFavoritEwalletEmpty.visibility = View.VISIBLE
                        favoritEWalletAdapter.submitList(null)
                    }

                    if(!uiState.dataFavoritVirtualAccount.isNullOrEmpty()) {
                        binding.rvTersimpanVa.visibility = View.VISIBLE
                        binding.tvFavoritVaEmpty.visibility = View.GONE
                        favoritVirtualAccountAdapter.submitList(uiState.dataFavoritVirtualAccount)
                    } else {
                        binding.rvTersimpanVa.visibility = View.GONE
                        binding.tvFavoritVaEmpty.visibility = View.VISIBLE
                        favoritVirtualAccountAdapter.submitList(null)
                    }
                }
            }
        }
    }

    private fun setUpBottomNavBar() {
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(0).isChecked = false
        binding.bottomNavigationView.menu.getItem(1).isCheckable = false
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        binding.bottomNavigationView.menu.getItem(3).isCheckable = true
        binding.bottomNavigationView.menu.getItem(4).isCheckable = false

        binding.bottomNavigationView.selectedItemId = R.id.menu_navbar_favorit

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
                    findNavController().navigate(R.id.action_favoritFragment_to_qrisScanQRFragment)
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
    }

    override fun onFavoriteItemSesamaClicked(daftarTersimpan: DaftarTersimpanSesama) {
        val dataRekening = CekRekeningSesamaBundle(daftarTersimpan.namaPemilikRekening, daftarTersimpan.noRekening)

        val bundle = Bundle().apply {
            putParcelable(Constant.DATA_REKENING_SESAMA_BUNDLE, dataRekening)
            putBoolean(Constant.DAFTAR_TERSIMPAN_SELECTED_MODE, true)
        }

        if(findNavController().currentDestination?.id == R.id.favoritFragment) {
            findNavController().navigate(R.id.action_favoritFragment_to_transferSesamaBankFormFragment, bundle)
        }
    }

    override fun onDeleteItemSesamaClicked(daftarTersimpan: DaftarTersimpanSesama) {
        favoritViewModel.deleteDaftarTersimpanSesama(daftarTersimpan)
        Toast.makeText(requireActivity(), "Daftar Favorit berhasil dihapus.", Toast.LENGTH_SHORT).show()
    }

    override fun onFavoriteItemAntarClicked(daftarTersimpan: DaftarTersimpanAntar) {
        val dataRekening = CekRekeningAntarBundle(daftarTersimpan.idBank, daftarTersimpan.namaBank, daftarTersimpan.namaPemilikRekening, daftarTersimpan.noRekening)

        val bundle = Bundle().apply {
            putParcelable(Constant.DATA_REKENING_ANTAR_BUNDLE, dataRekening)
            putBoolean(Constant.DAFTAR_TERSIMPAN_SELECTED_MODE, true)
        }

        if(findNavController().currentDestination?.id == R.id.favoritFragment) {
            findNavController().navigate(R.id.action_favoritFragment_to_transferAntarBankForm, bundle)
        }
    }

    override fun onDeleteItemAntarClicked(daftarTersimpan: DaftarTersimpanAntar) {
        favoritViewModel.deleteDaftarTersimpanAntar(daftarTersimpan)
        Toast.makeText(requireActivity(), "Daftar Favorit berhasil dihapus.", Toast.LENGTH_SHORT).show()
    }

    override fun onFavoriteItemEWalletClicked(daftarTersimpan: DaftarTersimpanEWallet) {
        val dataEWallet = CekEWalletBundle(
            namaEWallet = daftarTersimpan.namaEWallet,
            id = daftarTersimpan.idAkunEWallet,
            nomorEWallet = daftarTersimpan.nomorEWallet,
            namaAkunEWallet = daftarTersimpan.namaAkunEWallet
        )
        val bundle = Bundle().apply {
            putParcelable(Constant.DATA_CEK_EWALLET, dataEWallet)
            putBoolean(Constant.DAFTAR_TERSIMPAN_CHECKED_EXTRA, false)
        }
        findNavController().navigate(
            R.id.action_favoritFragment_to_transferEWalletForm,
            bundle
        )
    }

    override fun onDeleteItemEWalletClicked(daftarTersimpan: DaftarTersimpanEWallet) {
        favoritViewModel.deleteDaftarTersimpanEWallet(daftarTersimpan)
        Toast.makeText(requireActivity(), "Daftar Favorit berhasil dihapus.", Toast.LENGTH_SHORT).show()
    }

    override fun onFavoriteItemVaClicked(daftarTersimpan: DaftarTersimpanVa) {
        val dataVa = daftarTersimpan.noRekening

        val bundle = Bundle().apply {
            putString(Constant.DATA_NO_VA_STRING,dataVa)
            putBoolean(Constant.DAFTAR_TERSIMPAN_SELECTED_MODE, true)
        }

        if (findNavController().currentDestination?.id == R.id.favoritFragment) {
            findNavController().navigate(
                R.id.action_favoritFragment_to_transferVirtualAccountFormKonfirmasi,
                bundle
            )
        }
    }

    override fun onDeleteItemVaClicked(daftarTersimpan: DaftarTersimpanVa) {
        favoritViewModel.deleteDaftarTersimpanVirtualAccount(daftarTersimpan)
        Toast.makeText(requireActivity(), "Daftar Favorit berhasil dihapus.", Toast.LENGTH_SHORT).show()
    }
}