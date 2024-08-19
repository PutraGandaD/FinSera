package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.presentation.databinding.DaftarTersimpanItemBinding

class DaftarTersimpanSesamaAdapter(val itemClickListener: OnSavedItemSesamaClickListener) : ListAdapter<DaftarTersimpanSesama, DaftarTersimpanSesamaAdapter.DaftarTersimpanSesamaViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarTersimpanSesamaViewHolder {
        val binding = DaftarTersimpanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaftarTersimpanSesamaViewHolder(binding)
    }

    class DaftarTersimpanSesamaViewHolder(private val binding: DaftarTersimpanItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DaftarTersimpanSesama) {
            binding.tvDaftartersimpanNamapemilik.text = data.namaPemilikRekening
            binding.tvDaftartersimpanNorekening.text = data.noRekening
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

    override fun onBindViewHolder(holder: DaftarTersimpanSesamaViewHolder, position: Int) {
        val data = getItem(position)

        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onSavedItemSesamaClicked(data)
        }
    }
}

interface OnSavedItemSesamaClickListener {
    fun onSavedItemSesamaClicked(daftarTersimpan: DaftarTersimpanSesama)
}
