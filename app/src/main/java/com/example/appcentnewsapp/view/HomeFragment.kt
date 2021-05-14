package com.example.appcentnewsapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.adapter.RecyclerViewAdapter
import com.example.appcentnewsapp.util.ConstantParams.Companion.QUERY_PAGE_SIZE
import com.example.appcentnewsapp.util.Resource
import com.example.appcentnewsapp.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var viewModel:NewsViewModel
    lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        recyclerViewAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
                putString("context","home")
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFragment,
                bundle,
            )

        }

        var job: Job? = null
        searchText.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(600L)
                it?.let {
                    if(it.toString().isNotEmpty()) {
                        viewModel.getAllNewArticles(it.toString())
                    }
                }
            }
        }

        viewModel.articles.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {newsResponse->
                        recyclerViewAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.articlesPage == totalPages
                        if(isLastPage){
                            newsRecycler.setPadding(0,0,0,0)
                        }

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{msg->
                        Log.e("HomeFragment", "Data can't loaded -> $msg")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun clearData() {

        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun hideProgressBar() {
        newsIsLoading.visibility = View.INVISIBLE
        isLoading = false

    }
    private fun showProgressBar(){
        newsIsLoading.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginnig = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginnig
                    && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getAllArticles(q = searchText.text.toString())
                isScrolling = false
            }

        }
    }

    private fun setupRecyclerView(){
        recyclerViewAdapter = RecyclerViewAdapter()
        newsRecycler.apply {
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeFragment.scrollListener)
        }
    }


}



