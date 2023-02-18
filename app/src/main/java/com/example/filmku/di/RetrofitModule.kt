package com.example.filmku.di

import com.example.filmku.network.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    private val BASE_URL = "https://imdb-api.com/"

    @Provides
    @Singleton
    fun apiService(): Api {
        val requestInterceptor = Interceptor { chain ->
            val url: HttpUrl = chain.request().url().newBuilder().build()
            val request = chain.request().newBuilder().url(url).build()
            return@Interceptor chain.proceed(request)
        }
        val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS).build()

        return Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build()
            .create(Api::class.java)
    }
}