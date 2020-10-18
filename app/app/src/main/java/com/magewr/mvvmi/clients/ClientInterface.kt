package com.magewr.mvvmi.clients

import retrofit2.Retrofit

interface ClientInterface<API : Any> {
    fun createRetrofit(clazz: Class<API>): Retrofit
    val api: API
}
