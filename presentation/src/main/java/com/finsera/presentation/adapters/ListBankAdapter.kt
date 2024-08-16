package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.domain.model.Bank
import com.finsera.presentation.databinding.ListBankItemBinding

class ListBankAdapter(val itemClickListener: OnBankItemClickListener) : ListAdapter<Bank, ListBankAdapter.ListBankViewHolder>(DIFF_CALLBACK) {
    class ListBankViewHolder(private val binding: ListBankItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Bank) {
            binding.tvNamaBank.text = data.namaBank
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBankViewHolder {
        val binding = ListBankItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListBankViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListBankViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onBankItemClicked(data)
        }
    }

    companion object{
        val DIFF_CALLBACK=object : DiffUtil.ItemCallback<Bank>(){
            override fun areItemsTheSame(oldItem: Bank, newItem: Bank): Boolean {
                return oldItem==newItem
            }
            override fun areContentsTheSame(oldItem: Bank, newItem: Bank): Boolean {
                return oldItem==newItem
            }
        }
    }
}

interface OnBankItemClickListener {
    fun onBankItemClicked(bank: Bank)
}