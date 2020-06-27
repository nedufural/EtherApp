package com.fastcon.etherapp.data.model.entity

import com.fastcon.etherapp.data.remote.model.NewsResponse

data class NewsEntity(

    val activityHotness: Double?,

    val coins: List<NewsResponse.NewsResponseItem.Coin?>?,

    val description: String?,

    val hotness: Double?,

    val id: String?,

    val originalImageUrl: String?,

    val primaryCategory: String?,

    val publishedAt: String?,

    val similarArticles: List<Any?>?,

    val source: NewsResponse.NewsResponseItem.Source?,

    val sourceDomain: String?,

    val thumbnail: String?,

    val title: String?,

    val url: String?,

    val words: Int?
)