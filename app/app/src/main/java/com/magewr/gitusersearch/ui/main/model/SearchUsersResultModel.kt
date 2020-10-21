package com.magewr.gitusersearch.ui.main.model

import com.magewr.gitusersearch.interactors.favoriteusers.LocalFavoriteUsers

data class SearchUsersResultModel (
    val total_count: Long,
    val incomplete_results: Boolean,
    val items: List<Users>
)

data class Users (
    val login: String,
    val id: Long,
    val node_id: String,
    val avatar_url: String,
    val gravatar_id: String,
    val url: String,
    val html_url: String,
    val followers_url: String,
    val subscriptions_url: String,
    val organizations_url: String,
    val repos_url: String,
    val received_events_url: String,
    val type: String,
    val score: Long
) {
    fun isFavorite(): Boolean = LocalFavoriteUsers.users.containsKey(id)
}



