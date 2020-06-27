package com.fastcon.etherapp.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk
import com.fastcon.etherapp.R
import com.fastcon.etherapp.base.BaseRecycleViewAdapter
import com.fastcon.etherapp.base.BaseViewHolder
import com.fastcon.etherapp.base.ItemClickListener
import com.fastcon.etherapp.data.model.entity.NewsEntity

class NewsAdapter(private val itemClickListener: ItemClickListener<NewsEntity>) :
    BaseRecycleViewAdapter<NewsEntity>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<NewsEntity> {
        val convertView =
            LayoutInflater.from(parent.context).inflate(R.layout.items_news, parent, false)

        return NewsViewHolder(convertView, itemClickListener = itemClickListener)
    }

    class NewsViewHolder(
        convertView: View,
        itemClickListener: ItemClickListener<NewsEntity>
    ) :
        BaseViewHolder<NewsEntity>(itemClickListener, convertView) {
        private var conView: View = convertView
        private var newsDesc: TextView = convertView.findViewById(R.id.news_description)
        private var newsTitle: TextView = convertView.findViewById(R.id.news_title)
        private var newsDate: TextView = convertView.findViewById(R.id.news_date)
        private var newsImage: ImageView = convertView.findViewById(R.id.news_image)

        override fun bindingData(data: NewsEntity?) {
            setData(data)
            newsDesc.text = data?.description
            newsTitle.text = data?.title
            newsDate.text = data?.publishedAt

            Glide
                .with(FacebookSdk.getApplicationContext())
                .load(data?.thumbnail)
                .centerCrop()
                .placeholder(R.drawable.ic_no_photo)
                .into(newsImage)

        }

    }
}