package com.raremode.gorodskoy.ui.fragments.newsopened

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raremode.gorodskoy.databinding.FragmentNewsOpenedBinding


class NewsOpenedFragment : Fragment() {
    private val TAG = "NewsOpenedFragment"
    private var _binding: FragmentNewsOpenedBinding? = null
    private val binding get() = _binding!!


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
        val position = arguments?.getInt(NEWS_ADAPTER_POSITION) ?: -1
        when (position) {
            0 -> binding.webPage.loadUrl("https://vk.com/audios387991974?section=general")
            1 -> binding.webPage.loadUrl("https://yandex.ru/news/rubric/index?from=story")
            2 -> binding.webPage.loadUrl("https://www.youtube.com/")
            3 -> binding.webPage.loadUrl("https://vk.com/audios387991974?section=general")
            4 -> binding.webPage.loadUrl("https://yandex.ru/news/rubric/index?from=story")
            5 -> binding.webPage.loadUrl("https://www.youtube.com/")
            else -> {
                Log.d(TAG, "Error with open page number $position")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val NEWS_ADAPTER_POSITION = "NEWS_ADAPTER_POSITION"
    }
}