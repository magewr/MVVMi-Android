package com.magewr.gitusersearch.clients

import retrofit2.Retrofit

interface ClientInterface<API : Any> {
    fun createRetrofit(clazz: Class<API>): Retrofit
    val api: API
}
