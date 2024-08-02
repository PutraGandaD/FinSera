package com.finsera.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.databinding.MutasiItemBinding
import com.finsera.domain.model.Mutasi
import java.text.SimpleDateFormat
import java.util.Locale

class MutasiAdapter : ListAdapter<Mutasi, MutasiAdapter.MutasiViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MutasiViewHolder {
        val binding = MutasiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MutasiViewHolder(binding)
    }

    class MutasiViewHolder(private val binding: MutasiItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Mutasi) {
            binding.tvAmount.text = data.amount.toString()
            binding.tvDate.text = formatDateString(data.transactionDate)
            binding.tvDateTime.text =formatTimeString(data.transactionDate)
            binding.tvDescription.text =data.noTransaction
        }

        private fun formatTimeString(dateString: String): String {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            val outputFormatter = SimpleDateFormat("HH:mm:ss 'WIB'", Locale.getDefault())

            val date = inputFormatter.parse(dateString)
            return outputFormatter.format(date!!)
        }

        private fun formatDateString(dateString: String): String {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            val outputFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val date = inputFormatter.parse(dateString)
            return outputFormatter.format(date!!)
        }
    }


    override fun onBindViewHolder(holder: MutasiAdapter.MutasiViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }


    companion object{
        val DIFF_CALLBACK=object : DiffUtil.ItemCallback<Mutasi>(){
            override fun areItemsTheSame(oldItem: Mutasi, newItem: Mutasi): Boolean {
                return oldItem==newItem
            }
            override fun areContentsTheSame(oldItem: Mutasi, newItem: Mutasi): Boolean {
                return oldItem==newItem
            }

        }
    }



}