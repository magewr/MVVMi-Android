package com.magewr.gitusersearch.clients.apis

import com.magewr.gitusersearch.ui.main.model.SearchUsersResultModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface APISearch {
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") q: String,
        @Query("sort") sort: String?,
        @Query("order") order: String?,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Single<SearchUsersResultModel>
}