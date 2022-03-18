package com.gmail.wwon.seokk.imagestorage.data

sealed class DataResult<out T : Any> {
    data class Success<T : Any>(val data: T) : DataResult<T>()
    data class Error(val ex: Throwable) : DataResult<Nothing>()
    data class Loading(val isLoading: Boolean) : DataResult<Boolean>()
}