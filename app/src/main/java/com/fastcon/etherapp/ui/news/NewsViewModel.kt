package com.fastcon.etherapp.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fastcon.etherapp.data.DataManager
import com.fastcon.etherapp.data.remote.entity.NewsEntity
import com.fastcon.etherapp.data.remote.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    val newsMutableLiveData: MutableLiveData<ArrayList<NewsEntity>> =
        MutableLiveData()
    val newsErrorMsg: MutableLiveData<String> = MutableLiveData()
    var errorMsg: String = "Error News"
    var data = ArrayList<NewsEntity>()

    fun getNews(url: String) {

        val call: Call<NewsResponse> = DataManager.getApiService().getNews(url)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                newsErrorMsg.value = errorMsg
            }

            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
              data.clear()
                for (i in response.body()!!) {

                    data.add(
                        NewsEntity(
                            i.activityHotness,
                            i.coins,
                            i.description,
                            i.hotness,
                            i.id,
                            i.originalImageUrl,
                            i.primaryCategory,
                            i.publishedAt,
                            i.similarArticles,
                            i.source,
                            i.sourceDomain,
                            i.thumbnail,
                            i.title,
                            i.url,
                            i.words
                        )
                    )
                }

                newsMutableLiveData.value = data
            }
        })
    }
}