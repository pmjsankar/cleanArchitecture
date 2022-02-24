package com.pmj.domain.model

import androidx.annotation.Keep

/**
 * Generic class for holding success response, error response and loading status
 */
@Keep
data class Output<out T>(
    val status: Status,
    val data: T?,
    val error: ApiError?,
    val message: String?
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): Output<T> {
            return Output(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String, error: ApiError?): Output<T> {
            return Output(Status.ERROR, null, error, message)
        }

        fun <T> loading(data: T? = null): Output<T> {
            return Output(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error, message=$message)"
    }
}