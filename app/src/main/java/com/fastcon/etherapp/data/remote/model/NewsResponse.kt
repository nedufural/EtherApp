package com.fastcon.etherapp.data.remote.model
import com.google.gson.annotations.SerializedName


class NewsResponse : ArrayList<NewsResponse.NewsResponseItem>(){
    data class NewsResponseItem(
        @SerializedName("activityHotness")
        val activityHotness: Double,
        @SerializedName("coins")
        val coins: List<Coin>,
        @SerializedName("description")
        val description: String,
        @SerializedName("hotness")
        val hotness: Double,
        @SerializedName("_id")
        val id: String,
        @SerializedName("originalImageUrl")
        val originalImageUrl: String,
        @SerializedName("primaryCategory")
        val primaryCategory: String,
        @SerializedName("publishedAt")
        val publishedAt: String,
        @SerializedName("similarArticles")
        val similarArticles: List<Any>,
        @SerializedName("source")
        val source: Source,
        @SerializedName("sourceDomain")
        val sourceDomain: String,
        @SerializedName("thumbnail")
        val thumbnail: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("words")
        val words: Int
    ) {
        data class Coin(
            @SerializedName("_id")
            val id: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("slug")
            val slug: String,
            @SerializedName("tradingSymbol")
            val tradingSymbol: String
        )

        data class Source(
            @SerializedName("favicon")
            val favicon: String,
            @SerializedName("_id")
            val id: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }
}