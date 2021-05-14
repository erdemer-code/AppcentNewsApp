package com.example.appcentnewsapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.recycler_row.view.*


class DetailFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    val args: DetailFragmentArgs by navArgs()
    private var clicked = false

    private val rotateOpen:Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_open_anim) }
    private val rotateClose:Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_close_anim) }
    private val fromBottom:Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.from_bottom_anim) }
    private val toBottom:Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.to_bottom_anim) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = args.article
        val contextName = args.context

        if (contextName.equals("favorite")){
            fab.visibility = View.GONE
        }

        Glide.with(this).load(article.urlToImage).into(imageViewDetail)
        textViewArticleContent.text = article.content
        textViewTitleDetail.text = article.title
        textViewAuthor.text = article.author
        textViewDate.text = article.publishedAt

        buttonGoToDetail.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",article)
            }
            findNavController().navigate(
                R.id.action_detailFragment_to_articleWebFragment,
                bundle
            )
        }





        fab.setOnClickListener {
            onAddButtonClicked()
        }
        fabFav.setOnClickListener {
            viewModel.makeFavoriteArticle(article)
            Snackbar.make(view,"Article added favorites",Snackbar.LENGTH_SHORT).show()
        }
        fabShare.setOnClickListener {

            val sendIntent:Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT,"Shared News from Appcent News App")
                putExtra(Intent.EXTRA_TITLE,article.title)
                putExtra(Intent.EXTRA_TEXT,article.url)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent,null)
            startActivity(shareIntent)
        }

    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            fabFav.startAnimation(fromBottom)
            fabShare.startAnimation(fromBottom)
            fab.startAnimation(rotateOpen)
        } else {
            fabFav.startAnimation(toBottom)
            fabShare.startAnimation(toBottom)
            fab.startAnimation(rotateClose)
        }

    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked){
            fabFav.visibility=View.VISIBLE
            fabShare.visibility = View.VISIBLE
        } else {
            fabFav.visibility=View.INVISIBLE
            fabShare.visibility = View.INVISIBLE
        }
    }
}