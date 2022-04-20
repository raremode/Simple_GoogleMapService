package com.raremode.gorodskoy.ui.fragments.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.raremode.gorodskoy.R
import com.raremode.gorodskoy.databinding.FragmentNewsBinding
import com.raremode.gorodskoy.ui.fragments.news.adapters.NewsItemsAdapter
import com.raremode.gorodskoy.ui.fragments.newsopened.NewsOpenedFragment
import com.raremode.gorodskoy.ui.models.NewsItemsModel


class NewsFragment : Fragment() {

    private val titles = arrayOf(
        "  О погоде",
        "  Проблемы экологии",
        "  Ситуация в России",
        "  Ситуация в мире",
        "  Ситуация по области",
        "  Ситуация в городе"
    )

    private var _binding: FragmentNewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: NewsItemsAdapter
    private val newsItemsList: MutableList<NewsItemsModel> = mutableListOf()
    var clickedPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titles.forEach { title ->
            newsItemsList.add(
                NewsItemsModel(title)
            )
        }
        adapter = NewsItemsAdapter(newsItemsList)
        adapter.clickCallback = { position ->
            val bundle = Bundle()
            bundle.putInt(NewsOpenedFragment.NEWS_ADAPTER_POSITION, position)
            findNavController().navigate(
                R.id.action_navigation_news_to_newsOpenedFragment,
                args = bundle
            )
        }
        binding.newsItemsRV.adapter = adapter
        binding.newsItemsRV.layoutManager = LinearLayoutManager(context)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}