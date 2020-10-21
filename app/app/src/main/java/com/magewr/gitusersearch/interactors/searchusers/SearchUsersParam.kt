package com.magewr.gitusersearch.interactors.searchusers

import com.magewr.gitusersearch.commons.PER_PAGE

data class SearchUsersParam (
    val q: String,
    val sort: String?,
    val order: String?,
    val page: Int,
    val per_page: Int = PER_PAGE
)