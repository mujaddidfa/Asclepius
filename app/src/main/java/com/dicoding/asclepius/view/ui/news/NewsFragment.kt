package com.dicoding.asclepius.view.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.NewsRepository
import com.dicoding.asclepius.data.local.room.NewsDatabase
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.dicoding.asclepius.view.ViewModelFactory

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("View binding is only valid between onCreateView and onDestroyView")

    private val newsViewModel: NewsViewModel by viewModels {
        ViewModelFactory(NewsRepository.getInstance(
            ApiConfig.getApiService(),
            NewsDatabase.getInstance(requireContext()).newsDao()
        ))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        super.onCreate(savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvNews.layoutManager = layoutManager

        newsViewModel.getHeadlineNews().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    val newsList = result.data.map { newsEntity ->
                        ArticlesItem(
                            publishedAt = newsEntity.publishedAt,
                            urlToImage = newsEntity.urlToImage.toString(),
                            description = newsEntity.description,
                            title = newsEntity.title,
                            url = newsEntity.url.toString()
                        )
                    }.filter { it.title != "[Removed]" }
                    setNewsData(newsList)
                }
                is Result.Error -> {
                    showLoading(false)
                    showError(result.error)
                }
            }
        }

        return root
    }

    private fun setNewsData(newsList: List<ArticlesItem>) {
        val adapter = NewsAdapter()
        adapter.submitList(newsList)
        binding.rvNews.adapter = adapter

        adapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(news: ArticlesItem) {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                startActivity(webIntent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) = binding.progressBar.isVisible == isLoading

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}