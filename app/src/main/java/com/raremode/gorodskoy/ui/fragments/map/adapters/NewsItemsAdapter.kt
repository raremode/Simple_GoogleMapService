package com.raremode.gorodskoy.ui.fragments.map.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raremode.gorodskoy.databinding.NewsItemLayoutBinding
import com.raremode.gorodskoy.ui.fragments.news.NewsFragment
import com.raremode.gorodskoy.ui.models.NewsItemsModel

class NewsItemsAdapter(private val context: NewsFragment, private val newsItemsList:MutableList<NewsItemsModel>, private val c: Context)
    : RecyclerView.Adapter<NewsItemsAdapter.NewsItemsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemsViewHolder {
        val binding = NewsItemLayoutBinding.inflate(LayoutInflater.from(c), parent,false)
        return NewsItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsItemsViewHolder, position: Int) {
        val newsItems = newsItemsList[position]
        holder.bind(newsItems)
    }

    override fun getItemCount(): Int {
        return newsItemsList.size
    }


    class NewsItemsViewHolder(newsItemLayoutBinding: NewsItemLayoutBinding)
        : RecyclerView.ViewHolder(newsItemLayoutBinding.root){

        private val binding = newsItemLayoutBinding

        fun bind(newsItems: NewsItemsModel){
            binding.newsHeader.text = newsItems.title
        }

    }
}