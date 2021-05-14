package com.example.appcentnewsapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.adapter.RecyclerViewAdapter
import com.example.appcentnewsapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favorite.*


class FavoriteFragment : Fragment() {
    lateinit var viewModel:NewsViewModel
    lateinit var recyclerViewAdapter:RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()


        recyclerViewAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
                putString("context","favorite")
            }
            findNavController().navigate(
                R.id.action_favoriteFragment_to_detailFragment,
                bundle
            )
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = recyclerViewAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Article deleted succesfully",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.makeFavoriteArticle(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(recyclerViewFav)
        }

        viewModel.getFavoriteNews().observe(viewLifecycleOwner, Observer { articles ->
            recyclerViewAdapter.differ.submitList(articles)

        })

    }


    private fun setupRecyclerView(){
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerViewFav.apply {
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}