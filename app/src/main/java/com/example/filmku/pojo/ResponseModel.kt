package com.example.filmku.pojo

data class ResponseModel(
    val errorMessage: String,
    val items: List<MovieModel>
)