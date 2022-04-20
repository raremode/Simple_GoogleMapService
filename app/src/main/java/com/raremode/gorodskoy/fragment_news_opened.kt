package com.raremode.gorodskoy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raremode.gorodskoy.databinding.FragmentNewsOpenedBinding
import com.raremode.gorodskoy.ui.fragments.news.NewsFragment


class fragment_news_opened : Fragment() {
    private val TAG = "NewsOpenedFragment"
    private var _binding: FragmentNewsOpenedBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private lateinit var newsFragment: NewsFragment
    var pos: Int = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewsOpenedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pos = newsFragment.clickedPosition

        when (pos) {
            0 -> binding.webPage.loadUrl("https://vk.com/audios387991974?section=general")
            1 -> binding.webPage.loadUrl("https://yandex.ru/news/rubric/index?from=story")
            2 -> binding.webPage.loadUrl("https://www.youtube.com/")
            3 -> binding.webPage.loadUrl("https://vk.com/audios387991974?section=general")
            4 -> binding.webPage.loadUrl("https://yandex.ru/news/rubric/index?from=story")
            5 -> binding.webPage.loadUrl("https://www.youtube.com/")
            else -> {
                Log.d(TAG, "Error with open page number $pos")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}