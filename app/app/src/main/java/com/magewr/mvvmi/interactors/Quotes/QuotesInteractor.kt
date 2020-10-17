package com.magewr.mvvmi.interactors.Quotes

import com.magewr.mvvmi.clients.RestClient
import com.magewr.mvvmi.clients.apis.APIQuotes
import com.magewr.mvvmi.scenes.main.model.QuotesResultModel
import io.reactivex.rxjava3.core.Single

class QuotesInteractor() {
    private lateinit var client: RestClient<APIQuotes>

    constructor(client: RestClient<APIQuotes>) : this() {
        this.client = client
    }

    fun requestRandomQuotes(): Single<QuotesResultModel> {
        return client.api.getRandomQuotes()
    }
}