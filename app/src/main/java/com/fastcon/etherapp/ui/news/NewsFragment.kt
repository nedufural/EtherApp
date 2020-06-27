package com.fastcon.etherapp.ui.news

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.FacebookSdk
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseFragment
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.local.PrefUtils
import com.fastcon.etherapp.data.model.entity.NewsEntity
import com.fastcon.etherapp.ui.news_details.NewsDetailsActivity
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : BaseFragment(), ItemClickListener<NewsEntity> {
    private lateinit var adapter: NewsAdapter
    private lateinit var newsViewModel: NewsViewModel
    override fun getLayoutId(): Int = R.layout.fragment_news
    override fun initData() {
        adapter = NewsAdapter(this)
        news_recycler.layoutManager = LinearLayoutManager(context)

        val dividerItemDecoration =
            DividerItemDecoration(
                news_recycler.context,
                (news_recycler.layoutManager as LinearLayoutManager).orientation
            )

        news_recycler.addItemDecoration(dividerItemDecoration)
        news_recycler.adapter = adapter

        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        newsViewModel.getNews("https://cryptocontrol.io/api/v1/public/news")
    }

    override fun initEvent() {

        newsViewModel.newsMutableLiveData.observe(this, Observer { newsData ->
            adapter.setData(newsData)
        })
    }

    override fun onItemClick(data: NewsEntity?, position: Int, typeClick: Int) {
        val id =
            Intent(FacebookSdk.getApplicationContext(), NewsDetailsActivity::class.java)

        id.putExtra("description", data?.description)
        id.putExtra("originalImageUrl", data?.originalImageUrl)
        id.putExtra("date", data?.publishedAt)
        id.putExtra("title", data?.title)
        id.putExtra("sourceDomain", data?.sourceDomain)
        id.putExtra("url", data?.url)
        id.putExtra("source_icon", data?.source?.favicon)
        id.putExtra("source_name", data?.source?.name)
        id.putExtra("source_url", data?.source?.url)
        PrefUtils.storeNewsList(data?.similarArticles!!)
        id.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        FacebookSdk.getApplicationContext().startActivity(id)
    }

}