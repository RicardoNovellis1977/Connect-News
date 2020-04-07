package com.dino.connectnews.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dino.connectnews.R
import com.dino.connectnews.view.adapter.NewsAdapter
import com.dino.connectnews.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterNews: NewsAdapter
    private lateinit var refreshLayout: SwipeRefreshLayout
    var page = 1

    fun newInstance(categoria: String?): HomeFragment {
        val args = Bundle()
        val fragment = HomeFragment()
        args.putString("categoria", categoria)
        fragment.arguments = args
        return fragment
    }
    var category: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        refreshLayout = root.findViewById(R.id.refreshLayout)
        recyclerView = root.findViewById(R.id.recycler_news)
        category = this.arguments?.getString("categoria")

        homeViewModel.getNews(category!!, page).observe(this, Observer { news ->

            if (news == null) {
                //Toast.makeText(context, " Verifique sua conexão :( !", Toast.LENGTH_LONG).show()
            } else {
                adapterNews = NewsAdapter(news.articles)
                initRecyclerView()
                homeViewModel.saveSharedPreferencesNews(context!!, news.articles)
            }
        })
        if (!homeViewModel.isOnline(context!!)){
            homeViewModel.loadSharedPreferencesNews(context!!)?.observe(this, Observer {
                if (it != null){
                    adapterNews = NewsAdapter(it)
                    adapterNews.adicioarNews(it)
                    initRecyclerView()
                    Toast.makeText(context, " Verifique sua conexão : (  !", Toast.LENGTH_LONG).show()
                }
            })
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val totalItemCount = layoutManager!!.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisible + 5 >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached && page < 2) {
                    page++
                    homeViewModel.getNews(category!!, page)
                        .observe(this@HomeFragment, Observer { news ->
                            if (news == null) {
                                Toast.makeText(context, "Você esta off line", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                adapterNews.adicioarNews(news.articles)
                                //Toast.makeText(context, "pag 2", Toast.LENGTH_LONG).show()
                            }
                        })
                }
            }
        })
        refreshLayout.setOnRefreshListener {
            page = 1
            homeViewModel.getNews(category!!, page)
                .observe(this@HomeFragment, Observer { news ->
                    if (news == null) {
                        Toast.makeText(context, "Você esta off line", Toast.LENGTH_LONG).show()
                    } else {
                        adapterNews = NewsAdapter(news.articles)
                       initRecyclerView()
                    }
                    refreshLayout.isRefreshing = false
                })
        }
        return root
    }

    private fun initRecyclerView(){
        recyclerView.adapter = adapterNews
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.hasFixedSize()
    }
}