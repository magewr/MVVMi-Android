package com.magewr.mvvmi.interactors.Quotes

import com.magewr.mvvmi.clients.ClientInterface
import com.magewr.mvvmi.clients.apis.APIQuotes
import com.magewr.mvvmi.ui.main.model.QuotesResultModel
import io.reactivex.rxjava3.core.Single

interface QuotesInteractorInterface {
    fun requestRandomQuotes(): Single<QuotesResultModel>
}

class QuotesInteractor(private val client: ClientInterface<APIQuotes>) : QuotesInteractorInterface {

    override fun requestRandomQuotes(): Single<QuotesResultModel> {
        return client.api.getRandomQuotes()
    }
}