package com.example.filmku.repository

class NetworkState(val status: Status, val msg: String) {
    companion object {
        val LOADED: NetworkState
        val LOADING: NetworkState
        val ERROR: NetworkState

        init {
            LOADED = NetworkState(Status.SUCCESS, "Success")
            LOADING = NetworkState(Status.RUNNING, "Running")
            ERROR = NetworkState(Status.FAiLD, "something went wrong!")
        }
    }
}

enum class Status {
    RUNNING,
    SUCCESS,
    FAiLD
}