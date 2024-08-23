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
