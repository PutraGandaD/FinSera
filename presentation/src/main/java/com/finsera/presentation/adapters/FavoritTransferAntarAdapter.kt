package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.presentation.databinding.FavoritItemBinding

class FavoritTransferAntarAdapter(val itemClickListener: OnFavoriteItemAntarClickListener) : ListAdapter<DaftarTersimpanAntar, FavoritTransferAntarAdapter.FavoritTransferAntarViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritTransferAntarViewHolder {
        val binding = FavoritItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritTransferAntarViewHolder(binding)
    }

    class FavoritTransferAntarViewHolder(private val binding: FavoritItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DaftarTersimpanAntar) {
            binding.tvDaftartersimpanNamapemilik.text = data.namaPemilikRekening
            binding.tvDaftartersimpanNorekening.text = data.noRekening
        }
    }

    companion object{
        val DIFF_CALLBACK=object : DiffUtil.ItemCallback<DaftarTersimpanAntar>(){
            override fun areItemsTheSame(oldItem: DaftarTersimpanAntar, newItem: DaftarTersimpanAntar): Boolean {
                return oldItem==newItem
            }
            override fun areContentsTheSame(oldItem: DaftarTersimpanAntar, newItem: DaftarTersimpanAntar): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onBindViewHolder(holder: FavoritTransferAntarViewHolder, position: Int) {
        val data = getItem(position)

        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onFavoriteItemAntarClicked(data)
        }
    }
}

interface OnFavoriteItemAntarClickListener {
    fun onFavoriteItemAntarClicked(daftarTersimpan: DaftarTersimpanAntar)
    fun onDeleteItemAntarClicked(daftarTersimpan: DaftarTersimpanAntar)
}