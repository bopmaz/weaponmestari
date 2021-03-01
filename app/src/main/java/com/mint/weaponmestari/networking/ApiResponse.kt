package com.mint.weaponmestari.networking

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Response

sealed class ApiResponse<T> {
    companion object {

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(
                error.message ?: ResponseConstant.UNKNOWN_ERROR,
                500
            )
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                val headers = response.headers()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body, headers)
                }
            } else {
                val errorBody = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                val errorMsg = errorBody?.errorDescription ?: response.message()
                ApiErrorResponse(errorMsg ?: ResponseConstant.UNKNOWN_ERROR, response.code())
            }
        }
    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(
    val body: T?,
    val headers: okhttp3.Headers
) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String, val statusCode: Int) : ApiResponse<T>()

object ResponseConstant {
    const val UNKNOWN_ERROR = "Unknown Error"
}

data class ErrorBody(@SerializedName("error_desc") val errorDescription: String)