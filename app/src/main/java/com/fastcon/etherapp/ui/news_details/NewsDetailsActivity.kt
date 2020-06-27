package com.fastcon.etherapp.ui.news_details

import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseActivity
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.local.PrefUtils.getNewsList
import com.fastcon.etherapp.data.model.entity.NewsDetailsEntity
import com.fastcon.etherapp.databinding.ActivityNewsDetailsBinding

import com.fastcon.etherapp.util.DividerItemDecorator
import kotlinx.android.synthetic.main.activity_news_details.*
import timber.log.Timber

class NewsDetailsActivity : BaseActivity<ActivityNewsDetailsBinding>(), ItemClickListener<NewsDetailsEntity> {


    private lateinit var title: String
    private lateinit var description: String
    private lateinit var originalImageUrl: String
    private lateinit var newsDate: String
    private lateinit var sourceDomain: String
    private lateinit var url: String
    private lateinit var sourceIcon: String
    private lateinit var sourceName: String
    private lateinit var sourceUrl: String
    private lateinit var similarArticles: ArrayList<String>


    private lateinit var adapter:NewsDetailsAdapter
    override fun getLayoutId(): Int {
        return R.layout.activity_news_details
    }

    override fun initData() {

        val extras = intent.extras
            news_view.settings.javaScriptEnabled = true
        if (extras != null) {
            title = extras.getString("title").toString()
            description = extras.getString("description").toString()
            originalImageUrl = extras.getString("originalImageUrl").toString()
            newsDate = extras.getString("date").toString()
            sourceDomain = extras.getString("sourceDomain").toString()
            url = extras.getString("url").toString()
            sourceIcon = extras.getString("source_icon").toString()
            sourceName = extras.getString("source_name").toString()
            sourceUrl = extras.getString("source_url").toString()

            var data = ArrayList<NewsDetailsEntity>()
            for (similarArticle in getNewsList()) {
                data.add(NewsDetailsEntity(similarArticle.toString()))
            }
           adapter= NewsDetailsAdapter(this)
            adapter.setData(data)
            news_detail_similar_articles.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
            val dividerItemDecoration =
                DividerItemDecorator(this.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.divider_sample
                    )
                }!!)
            news_detail_similar_articles.addItemDecoration(dividerItemDecoration)
            news_detail_similar_articles.adapter = adapter

        }
        else{
            Timber.i("error displaying")
        }
    }

    override fun initEvent() {
        news_detail_title.text = title
        news_detail_date.text = newsDate
        news_detail_description.text = description
        news_detail_original_img.text = originalImageUrl
        news_detail_source_domain.text = sourceDomain
        news_detail_source_icon.text = sourceIcon
        news_detail_source_name.text = sourceName
        news_detail_source_url.text = sourceUrl
        news_detail_url.text = url


            news_view.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError
            ) {
                handler.proceed()
            }
        }
        news_view.loadUrl(sourceUrl)


    }

    override fun onItemClick(data: NewsDetailsEntity?, position: Int, typeClick: Int) {

    }
}
