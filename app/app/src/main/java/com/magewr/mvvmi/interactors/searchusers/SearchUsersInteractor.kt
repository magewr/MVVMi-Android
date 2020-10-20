package com.magewr.mvvmi.interactors.searchusers

import com.magewr.mvvmi.clients.ClientInterface
import com.magewr.mvvmi.clients.apis.APISearch
import com.magewr.mvvmi.interactors.favoriteusers.LocalFavoriteUsers
import com.magewr.mvvmi.ui.main.model.SearchUsersResultModel
import io.reactivex.rxjava3.core.Single

interface SearchUsersInteractorInterface {
    fun getSearchUsers(param: SearchUsersParam): Single<SearchUsersResultModel>
    fun favoriteUpdated() : Single<SearchUsersResultModel>
}

// API 호출용 인터렉터
class SearchUsersInteractor(private val client: ClientInterface<APISearch>) : SearchUsersInteractorInterface {
    override fun getSearchUsers(param: SearchUsersParam): Single<SearchUsersResultModel> {
        if (param.q.isBlank())
            return Single.never()

        return client.api.getSearchUsers(param.q, param.sort, param.order, param.page, param.per_page)
    }

    override fun favoriteUpdated(): Single<SearchUsersResultModel> {
        return Single.never()
    }
}

// 로컬 호출용 인터렉터
class LocalUsersInteractor : SearchUsersInteractorInterface {
    private var latestParam: SearchUsersParam = SearchUsersParam("", null, null, 1)
    override fun getSearchUsers(param: SearchUsersParam): Single<SearchUsersResultModel> {
        val users = LocalFavoriteUsers.users.values.toList().filter { it.login.contains(param.q, true) }
        val count = users.size.toLong()
        val incompleteResults = false
        val resultModel = SearchUsersResultModel(count, incompleteResults, users)

        latestParam = param

        return Single.just(resultModel)
    }

    override fun favoriteUpdated(): Single<SearchUsersResultModel> {
        return getSearchUsers(latestParam)
    }

}