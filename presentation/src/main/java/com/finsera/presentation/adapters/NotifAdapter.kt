package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.finsera.domain.model.Notifikasi
import com.finsera.presentation.databinding.NotificationItemBinding

class NotifAdapter : ListAdapter<Notifikasi, NotifAdapter.NotifViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class NotifViewHolder(private val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Notifikasi) {
            binding.tvNotifType.text = data.typeNotification
            binding.tvPromoDate.text = data.createdDate
            binding.tvPromoTitle.text = data.tittle
            binding.tvPromoDescription.text = data.description

            val contentDescription = buildContentDescription(data)
            itemView.contentDescription = contentDescription
            itemView.importantForAccessibility = ViewGroup.IMPORTANT_FOR_ACCESSIBILITY_YES
        }

        private fun buildContentDescription(data: Notifikasi): String {
            val amount = data.description?.let { extractAmount(it) }
            val accountNumber = data.description?.let { extractAccountNumber(it) }
            val numberSpelled = accountNumber?.map { it.toString() }?.joinToString(" ")

            return "${data.typeNotification}, tanggal ${data.createdDate}, ${data.tittle}. " +
                    "Transfer senilai $amount Rupiah ke nomor $numberSpelled"
        }

        private fun extractAmount(description: String): String {
            return description.substringAfter("senilai ").substringBefore(" ke")
        }

        private fun extractAccountNumber(description: String): String {
            return description.substringAfterLast(" ")
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Notifikasi>() {
            override fun areItemsTheSame(oldItem: Notifikasi, newItem: Notifikasi): Boolean {
                return oldItem.typeNotification == newItem.typeNotification &&
                        oldItem.createdDate == newItem.createdDate
            }

            override fun areContentsTheSame(oldItem: Notifikasi, newItem: Notifikasi): Boolean {
                return oldItem == newItem
            }
        }
    }
}
