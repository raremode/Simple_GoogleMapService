package com.raremode.gorodskoy.ui.fragments.map.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raremode.gorodskoy.databinding.ItemFilterButtonBinding
import com.raremode.gorodskoy.ui.fragments.map.viewholders.FilterButtonViewHolder
import com.raremode.gorodskoy.ui.models.FilterButtonModel

class FilterButtonsAdapter(private val items: List<FilterButtonModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        }
    }

    override fun getItemCount(): Int =
        items.size

}