package com.example.appcentnewsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.model.ArticleModel
import kotlinx.android.synthetic.main.recycler_row.view.*

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.ArticleViewHolder>() {


    class ArticleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    private val differCallback = object: DiffUtil.ItemCallback<ArticleModel>(){
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source?.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt
            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener:((ArticleModel) ->Unit)? = null

    fun setOnItemClickListener(listener: (ArticleModel) -> Unit){
        onItemClickListener = listener
    }

}