package com.magewr.mvvmi.interactors.searchusers

import com.magewr.mvvmi.clients.ClientInterface
import com.magewr.mvvmi.clients.apis.APISearch
import com.magewr.mvvmi.interactors.favoriteusers.LocalFavoriteUsers
import com.magewr.mvvmi.ui.main.model.SearchUsersResultModel
import io.reactivex.rxjava3.core.Single

interface SearchUsersInteractorInterface {
    fun getSearchUsers(param: SearchUsersParam): Single<SearchUsersResultModel>
}

class SearchUsersInteractor(private val client: ClientInterface<APISearch>) : SearchUsersInteractorInterface {
    override fun getSearchUsers(param: SearchUsersParam): Single<SearchUsersResultModel> {
        return client.api.getSearchUsers(param.q, param.sort, param.order, param.page, param.per_page)
    }
}

class LocalUsersInteractor : SearchUsersInteractorInterface {

    override fun getSearchUsers(param: SearchUsersParam): Single<SearchUsersResultModel> {
        val users = LocalFavoriteUsers.users.values.toList().filter { it.login.contains(param.q) }
        val count = users.size.toLong()
        val incompleteResults = false
        val resultModel = SearchUsersResultModel(count, incompleteResults, users)

        return Single.just(resultModel)
    }

}