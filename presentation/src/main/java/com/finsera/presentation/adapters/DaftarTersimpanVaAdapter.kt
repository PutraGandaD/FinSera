package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.domain.model.DaftarTersimpanSesama
import com.finsera.domain.model.DaftarTersimpanVa
import com.finsera.presentation.adapters.DaftarTersimpanEWalletAdapter.DaftarTersimpanEWalletViewHolder
import com.finsera.presentation.databinding.DaftarTersimpanEwalletItemBinding
import com.finsera.presentation.databinding.DaftarTersimpanItemBinding

class DaftarTersimpanVaAdapter(val itemClickListener: OnSavedItemVaClickListener) : ListAdapter<DaftarTersimpanVa, DaftarTersimpanVaAdapter.DaftarTersimpanVaViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarTersimpanVaViewHolder {
        val binding = DaftarTersimpanEwalletItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaftarTersimpanVaViewHolder(binding)
    }

    class DaftarTersimpanVaViewHolder(private val binding: DaftarTersimpanEwalletItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DaftarTersimpanVa) {
            binding.tvItemName.text = data.namaPemilikRekening
            binding.tvItemDescription.text = data.noRekening
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

    override fun onBindViewHolder(holder: DaftarTersimpanVaViewHolder, position: Int) {
        val data = getItem(position)

        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onSavedItemVaClicked(data)
        }
    }
}

interface OnSavedItemVaClickListener {
    fun onSavedItemVaClicked(daftarTersimpan: DaftarTersimpanVa)
}