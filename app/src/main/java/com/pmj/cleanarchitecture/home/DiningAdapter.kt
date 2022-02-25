package com.pmj.cleanarchitecture.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pmj.cleanarchitecture.R
import com.pmj.cleanarchitecture.databinding.ListItemDiningBinding
import com.pmj.domain.model.Dining

/**
 * Adapter to populate dining list
 */
class DiningAdapter(private val onItemClick: (data: Dining, view: View) -> Unit) :
    ListAdapter<Dining, DiningAdapter.DiningViewHolder>(DiffCallback()) {

    inner class DiningViewHolder(private var binding: ListItemDiningBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Dining) {
            binding.item = model
            binding.cvDining.setOnClickListener {
                onItemClick.invoke(model, binding.ivDining)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiningViewHolder {
        return DiningViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_dining,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DiningViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private class DiffCallback : DiffUtil.ItemCallback<Dining>() {
        override fun areItemsTheSame(
            oldItem: Dining,
            newItem: Dining
        ): Boolean {
            // check if titles are the same
            return (oldItem.title == newItem.title)
        }

        override fun areContentsTheSame(
            oldItem: Dining,
            newItem: Dining
        ): Boolean {
            // check if content is the same
            // equals using data class
            return oldItem == newItem
        }
    }

}