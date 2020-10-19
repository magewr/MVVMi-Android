package com.magewr.mvvmi.clients.apis

import com.magewr.mvvmi.ui.main.model.QuotesResultModel
import io.reactivex.rxjava3.core.Single

import retrofit2.http.GET

interface APIQuotes {
    @GET("quotes/random")
    fun getRandomQuotes(): Single<QuotesResultModel>
}