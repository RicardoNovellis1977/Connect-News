package com.dino.connectnews.view.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dino.connectnews.R
import com.dino.connectnews.view.adapter.NewsAdapter
import com.dino.connectnews.data.model.Article
import com.dino.connectnews.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterNews: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var refreshLayout: SwipeRefreshLayout
    private var article: ArrayList<Article>? = ArrayList()
    private lateinit var preference: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        preference = context!!.getSharedPreferences("article", 0)
        progressBar = root.findViewById(R.id.progressBarRecycler)
        refreshLayout =root.findViewById(R.id.refreshLayout)

        adapterNews = NewsAdapter(article)
        recyclerView = root.findViewById(R.id.recycler_news)
        recyclerView.adapter = adapterNews
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.hasFixedSize()

        homeViewModel.getNews(adapterNews,this,context!!)
        if (!homeViewModel.isOnline(context!!) && homeViewModel.loadSharedPreferencesNews(context!!) != null){
            adapterNews.adicioarNews(homeViewModel.loadSharedPreferencesNews(context!!))
            Toast.makeText(context,"Você esta offline",Toast.LENGTH_LONG).show()
        }

        homeViewModel.getRefresh(adapterNews,refreshLayout,this,context!!)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                homeViewModel.getNextPage(adapterNews,this@HomeFragment,context!!,recyclerView)
            }
        })
        return root
    }
}