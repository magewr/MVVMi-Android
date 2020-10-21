package com.magewr.gitusersearch

import com.magewr.gitusersearch.ui.main.model.SearchUsersResultModel
import com.magewr.gitusersearch.ui.main.model.Users

class DummyDataFactory {
    companion object {
        fun getUsers() = Users(
            "magewr",
            32250713,
            "MDQ6VXNlcjMyMjUwNzEz",
            "https://avatars0.githubusercontent.com/u/32250713?v=4",
            "",
            "https://api.github.com/users/magewr",
            "https://github.com/magewr",
            "https://api.github.com/users/magewr/followers",
            "https://api.github.com/users/magewr/following{/other_user}",
            "https://api.github.com/users/magewr/gists{/gist_id}",
            "https://api.github.com/users/magewr/starred{/owner}{/repo}",
            "https://api.github.com/users/magewr/subscriptions",
            "https://api.github.com/users/magewr/orgs",
            1
        )

        fun getResultModel() =
            SearchUsersResultModel(
                1,
                false,
                listOf(getUsers())
            )
    }
}