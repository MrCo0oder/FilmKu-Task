package com.example.filmku.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmku.pojo.ResponseModel
import com.example.filmku.repository.NetworkState
import com.example.filmku.repository.RepositoryImple
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private var repository: RepositoryImple
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState
    private val _response = MutableLiveData<ResponseModel>()
    val response: LiveData<ResponseModel>
        get() = _response

    @SuppressLint("CheckResult")
    fun getTopMovies() {
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                repository.getTopMovies().subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread()).subscribe({
                        _response.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("TAG", "getTopMoviesError: $it")
                    })
            )
        } catch (e: Exception) {
            _networkState.postValue(NetworkState.ERROR)
            Log.e("TAG", "getTopMoviesError: ${e.message}")
        }
    }

    fun isEmptyError(): Boolean {
        return _response.value?.errorMessage?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
