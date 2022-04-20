package com.raremode.gorodskoy.ui.fragments.news.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.raremode.gorodskoy.databinding.NewsItemLayoutBinding
import com.raremode.gorodskoy.ui.models.NewsItemsModel

class NewsItemsViewHolder(private val binding: NewsItemLayoutBinding)
    : RecyclerView.ViewHolder(binding.root) {

    var clickCallback: ((position: Int) -> Unit)? = null

    fun bind(newsItems: NewsItemsModel) {
        binding.newsHeader.text = newsItems.title
        itemView.setOnClickListener {
            clickCallback?.invoke(adapterPosition)
        }
    }

}