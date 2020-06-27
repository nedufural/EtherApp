package com.fastcon.etherapp.ui.news_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseRecycleViewAdapter
import com.fastcon.etherapp.base.BaseViewHolder
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.model.entity.NewsDetailsEntity

class NewsDetailsAdapter(private val itemClickListener: ItemClickListener<NewsDetailsEntity>) :
    BaseRecycleViewAdapter<NewsDetailsEntity>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<NewsDetailsEntity> {
        val convertView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_similar_article, parent, false)

        return NewsDetailViewHolder(convertView, itemClickListener = itemClickListener)
    }

    class NewsDetailViewHolder(
        convertView: View,
        itemClickListener: ItemClickListener<NewsDetailsEntity>
    ) :
        BaseViewHolder<NewsDetailsEntity>(itemClickListener, convertView) {

        var article: TextView = convertView.findViewById(R.id.similar_article_list)


        override fun bindingData(data: NewsDetailsEntity?) {

            article.text = data?.similarArticle
        }

    }
}