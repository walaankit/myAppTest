package com.ankitwala.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ankitwala.myapplication.databinding.ItemTransactionBinding

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapter.TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionAdapter.TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TransactionViewHolder( private val transactionBinding: ItemTransactionBinding): RecyclerView.ViewHolder(transactionBinding.root) {
        fun bind(transaction: Transaction){
            transactionBinding.hashTextView.text = transaction.hash
            transactionBinding.dateTextView.text = transaction.dateTime
            transactionBinding.valueTextView.text = transaction.valueInUsd.toString()

        }
    }
}

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.hash == newItem.hash
    }

}