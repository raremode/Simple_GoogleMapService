package com.raremode.gorodskoy.ui.fragments.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raremode.gorodskoy.databinding.NewsItemLayoutBinding
import com.raremode.gorodskoy.ui.fragments.news.viewholders.NewsItemsViewHolder
import com.raremode.gorodskoy.ui.models.NewsItemsModel

class NewsItemsAdapter(private val newsItemsList:MutableList<NewsItemsModel>)
    : RecyclerView.Adapter<NewsItemsViewHolder>() {

    var clickCallback: ((position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemsViewHolder {
        val binding = NewsItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return NewsItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsItemsViewHolder, position: Int) {
        val newsItems = newsItemsList[position]
        holder.bind(newsItems)
        holder.clickCallback = clickCallback
    }

    override fun getItemCount(): Int {
        return newsItemsList.size
    }
}