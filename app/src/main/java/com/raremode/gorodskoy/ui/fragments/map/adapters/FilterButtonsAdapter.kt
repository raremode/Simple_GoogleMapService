package com.raremode.gorodskoy.ui.fragments.map.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.ItemFilterButtonBinding
import com.raremode.gorodskoy.ui.fragments.map.viewholders.FilterButtonViewHolder
import com.raremode.gorodskoy.ui.models.FilterButtonModel

class FilterButtonsAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var items: List<FilterButtonModel>
    private var rowIndex: Int? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<FilterButtonModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    var clickCallback: ((filterButtonModel: FilterButtonModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilterButtonViewHolder(
            ItemFilterButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilterButtonViewHolder) {
            holder.bind(items[position], items.size)
            holder.clickCallback = clickCallback
            rowIndex = holder.adapterPosition
            //holder.itemView.setBackgroundColor(Color.parseColor("#FF5BA501"))
        }

    }

    override fun getItemCount(): Int =
        items.size

}