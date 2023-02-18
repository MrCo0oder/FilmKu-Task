package com.example.filmku.network

import com.example.filmku.pojo.ResponseModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("API/Top250Movies/{api_key}")
    fun getTopMovies(@Path("api_key") apiKey: String): Observable<ResponseModel>
}