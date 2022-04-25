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
            0 -> binding.webPage.loadUrl("https://bloknot-taganrog.ru/news/vtoruyu-ploshchadku-dlya-razdelnogo-sbora-musora-u")
            1 -> binding.webPage.loadUrl("https://mytaganrog.com/bloknot/010615/programma-razdelnogo-sbora-musora-v-gorode-taganrog")
            2 -> binding.webPage.loadUrl("https://www.ruffnews.ru/taganrog/V-Taganroge-poyavilis-spetsialnye-konteynery-dlya-plastika_30542")
            3 -> binding.webPage.loadUrl("https://rcycle.net/musor/razdelnyj-sbor/kak-pravilno-sortirovat-othody-dlya-pererabotki")
            4 -> binding.webPage.loadUrl("https://j.etagi.com/stati/kak-organizovat-razdelnyy-sbor-mus/#:~:text=Сортировка%20мусора%20основана%20на%20одном,мусора%20без%20предварительного%20разделения%20невозможно")
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