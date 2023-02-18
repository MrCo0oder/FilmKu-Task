package com.example.filmku.repository

import com.example.filmku.pojo.ResponseModel
import io.reactivex.rxjava3.core.Observable

interface Repository {
    fun getTopMovies(): Observable<ResponseModel>
}