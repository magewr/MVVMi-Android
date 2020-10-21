package com.magewr.gitusersearch.clients

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.magewr.gitusersearch.commons.BASE_URL
import com.magewr.gitusersearch.commons.HTTP_TIMEOUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient<API : Any>(clazz: Class<API>) :
    ClientInterface<API> {

    private val retrofit: Retrofit
    private val gson: Gson
    override val api: API

    init {
        gson = createGson()
        retrofit = createRetrofit(clazz)
        api = retrofit.create(clazz)
    }

    override fun createRetrofit(clazz: Class<API>): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .readTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .connectTimeout(HTTP_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(logging)

        val okHttpClient = builder.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    private fun createGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()
    }
}