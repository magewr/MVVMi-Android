package com.magewr.mvvmi.clients

import retrofit2.Retrofit

interface ClientInterface<T : Any> {
    fun <T : Any> createRetrofit(clazz: T): Retrofit
}
