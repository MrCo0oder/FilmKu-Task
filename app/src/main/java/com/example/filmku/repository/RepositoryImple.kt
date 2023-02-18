package com.example.filmku.repository

import com.example.filmku.BuildConfig
import com.example.filmku.network.Api
import com.example.filmku.pojo.ResponseModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RepositoryImple @Inject constructor(private val apiService: Api) : Repository {
    private val apiKey = BuildConfig.API_KEY
    override fun getTopMovies(): Observable<ResponseModel> {
        return apiService.getTopMovies(apiKey)
    }

}