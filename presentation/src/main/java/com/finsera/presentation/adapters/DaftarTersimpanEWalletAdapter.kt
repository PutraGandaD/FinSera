package com.finsera.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finsera.domain.model.DaftarTersimpanEWallet
import com.finsera.presentation.R
import com.finsera.presentation.databinding.DaftarTersimpanEwalletItemBinding

class DaftarTersimpanEWalletAdapter(val itemClickListener: OnSavedItemEWalletClickListener) : ListAdapter<DaftarTersimpanEWallet, DaftarTersimpanEWalletAdapter.DaftarTersimpanEWalletViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DaftarTersimpanEWalletViewHolder {

        val binding =DaftarTersimpanEwalletItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaftarTersimpanEWalletViewHolder(binding)
    }

    class DaftarTersimpanEWalletViewHolder(private val binding: DaftarTersimpanEwalletItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DaftarTersimpanEWallet) {
            binding.tvItemName.text = data.namaAkunEWallet
            binding.tvItemDescription.text = data.nomorEWallet
            Glide.with(binding.root)
                .load(
                    when (data.namaEWallet) {
                        "OVO" -> R.drawable.ic_ovo
                        "Dana" -> R.drawable.ic_dana
                        "Paypal" -> R.drawable.ic_paypal
                        "GoPay" -> R.drawable.ic_gopay
                        "ShopeePay" -> R.drawable.ic_shopeepay
                        else -> {
                            R.drawable.ic_avatar
                        }
                    }
                )
                .into(binding.imgItemLogo)
        }
    }

    companion object{
        val DIFF_CALLBACK=object : DiffUtil.ItemCallback<DaftarTersimpanEWallet>(){
            override fun areItemsTheSame(oldItem: DaftarTersimpanEWallet, newItem: DaftarTersimpanEWallet): Boolean {
                return oldItem==newItem
            }
            override fun areContentsTheSame(oldItem: DaftarTersimpanEWallet, newItem: DaftarTersimpanEWallet): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onBindViewHolder(holder: DaftarTersimpanEWalletViewHolder, position: Int) {
        val data = getItem(position)

        holder.bind(data)

        holder.itemView.setOnClickListener {
            itemClickListener.onSavedItemVaClicked(data)
        }
    }
}

interface OnSavedItemEWalletClickListener {
    fun onSavedItemVaClicked(daftarTersimpan: DaftarTersimpanEWallet)
}