package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.model.DaftarTersimpanVa
import com.finsera.presentation.R
import com.finsera.presentation.databinding.FavoritItemBinding

class FavoritVirtualAccountAdapter(val itemClickListener: OnFavoriteItemVaClickListener) : ListAdapter<DaftarTersimpanVa, FavoritVirtualAccountAdapter.FavoritVirtualAccountViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritVirtualAccountViewHolder {
        val binding = FavoritItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritVirtualAccountViewHolder(binding, itemClickListener)
    }

    class FavoritVirtualAccountViewHolder(
        private val binding: FavoritItemBinding,
        private val itemClickListener: OnFavoriteItemVaClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DaftarTersimpanVa) {
            binding.tvDaftartersimpanNamapemilik.text = data.namaPemilikRekening
            binding.tvDaftartersimpanNorekening.text = data.noRekening

            binding.btnDeleteFavorititem.setOnClickListener {
                itemClickListener.onDeleteItemVaClicked(data)
            }
            binding.imgItemLogo.setImageResource(R.drawable.ic_avatar)

            val accountNumberWithSpaces = data.noRekening.replace("", " ").trim()
            val accessibilityText = itemView.context.getString(
                R.string.desc_daftar_tersimpan_va,
                data.namaPemilikRekening,
                accountNumberWithSpaces
            )
            itemView.contentDescription = accessibilityText
        }
    }

    companion object{
        val DIFF_CALLBACK=object : DiffUtil.ItemCallback<DaftarTersimpanVa>(){
            override fun areItemsTheSame(oldItem: DaftarTersimpanVa, newItem: DaftarTersimpanVa): Boolean {
                return oldItem==newItem
            }
            override fun areContentsTheSame(oldItem: DaftarTersimpanVa, newItem: DaftarTersimpanVa): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onBindViewHolder(holder: FavoritVirtualAccountViewHolder, position: Int) {
        val data = getItem(position)

        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onFavoriteItemVaClicked(data)
        }
    }
}

interface OnFavoriteItemVaClickListener {
    fun onFavoriteItemVaClicked(daftarTersimpan: DaftarTersimpanVa)
    fun onDeleteItemVaClicked(daftarTersimpan: DaftarTersimpanVa)
}