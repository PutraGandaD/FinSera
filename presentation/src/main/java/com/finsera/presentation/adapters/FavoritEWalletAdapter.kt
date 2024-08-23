package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.domain.model.DaftarTersimpanEWallet
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.presentation.databinding.FavoritItemBinding

class FavoritEWalletAdapter(val itemClickListener: OnFavoriteItemEWalletClickListener) : ListAdapter<DaftarTersimpanEWallet, FavoritEWalletAdapter.FavoritEWalletViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritEWalletViewHolder {
        val binding = FavoritItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritEWalletViewHolder(binding, itemClickListener)
    }

    class FavoritEWalletViewHolder(
        private val binding: FavoritItemBinding,
        private val itemClickListener: OnFavoriteItemEWalletClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DaftarTersimpanEWallet) {
            binding.tvDaftartersimpanNamapemilik.text = data.namaAkunEWallet
            binding.tvDaftartersimpanNorekening.text = data.nomorEWallet

            binding.btnDeleteFavorititem.setOnClickListener {
                itemClickListener.onDeleteItemEWalletClicked(data)
            }
        }
    }

    companion object{
        val DIFF_CALLBACK=object : DiffUtil.ItemCallback<DaftarTersimpanEWallet>(){
            override fun areItemsTheSame(oldItem: DaftarTersimpanEWallet, newItem: DaftarTersimpanEWallet): Boolean {
                return oldItem==newItem
            }
            override fun areContentsTheSame(oldItem: DaftarTersimpanEWallet, newItem: DaftarTersimpanEWallet): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onBindViewHolder(holder: FavoritEWalletViewHolder, position: Int) {
        val data = getItem(position)

        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onFavoriteItemEWalletClicked(data)
        }
    }
}

interface OnFavoriteItemEWalletClickListener {
    fun onFavoriteItemEWalletClicked(daftarTersimpan: DaftarTersimpanEWallet)
    fun onDeleteItemEWalletClicked(daftarTersimpan: DaftarTersimpanEWallet)
}