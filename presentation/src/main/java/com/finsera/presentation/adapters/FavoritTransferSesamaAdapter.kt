package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.presentation.databinding.DaftarTersimpanItemBinding
import com.finsera.presentation.databinding.FavoritItemBinding

class FavoritTransferSesamaAdapter(val itemClickListener: OnFavoriteItemSesamaClickListener) : ListAdapter<DaftarTersimpanSesama, FavoritTransferSesamaAdapter.FavoritTransferSesamaViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritTransferSesamaViewHolder {
        val binding = FavoritItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritTransferSesamaViewHolder(binding, itemClickListener)
    }

    class FavoritTransferSesamaViewHolder(
        private val binding: FavoritItemBinding,
        private val itemClickListener: OnFavoriteItemSesamaClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DaftarTersimpanSesama) {
            binding.tvDaftartersimpanNamapemilik.text = data.namaPemilikRekening
            binding.tvDaftartersimpanNorekening.text = data.noRekening

            binding.btnDeleteFavorititem.setOnClickListener {
                itemClickListener.onDeleteItemSesamaClicked(data)
            }
        }
    }

    companion object{
        val DIFF_CALLBACK=object : DiffUtil.ItemCallback<DaftarTersimpanSesama>(){
            override fun areItemsTheSame(oldItem: DaftarTersimpanSesama, newItem: DaftarTersimpanSesama): Boolean {
                return oldItem==newItem
            }
            override fun areContentsTheSame(oldItem: DaftarTersimpanSesama, newItem: DaftarTersimpanSesama): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onBindViewHolder(holder: FavoritTransferSesamaViewHolder, position: Int) {
        val data = getItem(position)

        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onFavoriteItemSesamaClicked(data)
        }
    }
}

interface OnFavoriteItemSesamaClickListener {
    fun onFavoriteItemSesamaClicked(daftarTersimpan: DaftarTersimpanSesama)
    fun onDeleteItemSesamaClicked(daftarTersimpan: DaftarTersimpanSesama)
}