package com.raremode.gorodskoy.ui.fragments.map.viewholders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raremode.gorodskoy.databinding.ItemFilterButtonBinding
import com.raremode.gorodskoy.extensions.dpsToIntPixels
import com.raremode.gorodskoy.ui.models.FilterButtonModel

class FilterButtonViewHolder(private val binding: ItemFilterButtonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var clickCallback: ((filterButtonModel: FilterButtonModel) -> Unit)? = null

        fun bind(button: FilterButtonModel, size: Int) {
            binding.ifbTextView.text = button.text
            when (adapterPosition) {
                0 -> (binding.ifbCardViewText.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = itemView.context.dpsToIntPixels(16F)
                    marginEnd = itemView.context.dpsToIntPixels(6F)
                }
                size - 1 -> (binding.ifbCardViewText.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginEnd = itemView.context.dpsToIntPixels(16F)
                    marginStart = itemView.context.dpsToIntPixels(6F)
                }
            }
            itemView.setOnClickListener {
                clickCallback?.invoke(button)
            }
        }
}