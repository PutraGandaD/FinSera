package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.domain.model.DaftarTersimpan
import com.finsera.presentation.databinding.DaftarTersimpanItemBinding

class DaftarTersimpanSesamaAdapter(val itemClickListener: OnSavedItemClickListener) : ListAdapter<DaftarTersimpan, DaftarTersimpanSesamaAdapter.DaftarTersimpanSesamaViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarTersimpanSesamaViewHolder {
        val binding = DaftarTersimpanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaftarTersimpanSesamaViewHolder(binding)
    }

    class DaftarTersimpanSesamaViewHolder(private val binding: DaftarTersimpanItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DaftarTersimpan) {
            binding.tvDaftartersimpanNamapemilik.text = data.namaPemilikRekening
            binding.tvDaftartersimpanNorekening.text = data.noRekening
        }
    }

    companion object{
        val DIFF_CALLBACK=object : DiffUtil.ItemCallback<DaftarTersimpan>(){
            override fun areItemsTheSame(oldItem: DaftarTersimpan, newItem: DaftarTersimpan): Boolean {
                return oldItem==newItem
            }
            override fun areContentsTheSame(oldItem: DaftarTersimpan, newItem: DaftarTersimpan): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onBindViewHolder(holder: DaftarTersimpanSesamaViewHolder, position: Int) {
        val data = getItem(position)

        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onSavedItemClicked(data)
        }
    }
}

interface OnSavedItemClickListener {
    fun onSavedItemClicked(daftarTersimpan: DaftarTersimpan)
}
