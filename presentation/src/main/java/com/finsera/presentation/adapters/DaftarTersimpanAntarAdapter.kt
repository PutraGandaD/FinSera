package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.domain.model.DaftarTersimpanAntar
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.presentation.databinding.DaftarTersimpanItemBinding

class DaftarTersimpanAntarAdapter(val itemClickListener: OnSavedItemAntarClickListener) : ListAdapter<DaftarTersimpanAntar, DaftarTersimpanAntarAdapter.DaftarTersimpanAntarViewHolder>(DIFF_CALLBACK) {
    class DaftarTersimpanAntarViewHolder(private val binding: DaftarTersimpanItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DaftarTersimpanAntar) {
            binding.tvDaftartersimpanNamapemilik.text = data.namaPemilikRekening
            binding.tvDaftartersimpanNorekening.text = data.noRekening
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarTersimpanAntarAdapter.DaftarTersimpanAntarViewHolder {
        val binding = DaftarTersimpanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaftarTersimpanAntarViewHolder(binding)
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

    override fun onBindViewHolder(
        holder: DaftarTersimpanAntarAdapter.DaftarTersimpanAntarViewHolder,
        position: Int
    ) {
        val data = getItem(position)

        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onSavedItemAntarClicked(data)
        }
    }
}

interface OnSavedItemAntarClickListener {
    fun onSavedItemAntarClicked(daftarTersimpan: DaftarTersimpanAntar)
}
