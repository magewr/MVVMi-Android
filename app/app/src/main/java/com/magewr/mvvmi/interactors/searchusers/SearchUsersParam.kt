package com.magewr.mvvmi.interactors.searchusers

import com.magewr.mvvmi.commons.PER_PAGE

data class SearchUsersParam (
    val q: String,
    val sort: String?,
    val order: String?,
    val page: Int,
    val per_page: Int = PER_PAGE
) {
}