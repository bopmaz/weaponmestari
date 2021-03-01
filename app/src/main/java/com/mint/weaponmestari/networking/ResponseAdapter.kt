package com.mint.weaponmestari.networking

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.awaitResponse
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class ResponseAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<ApiResponse<T>>> {

    override fun responseType() = responseType

    @ExperimentalCoroutinesApi
    override fun adapt(call: Call<T>): Flow<ApiResponse<T>> = flow {

        val response = call.awaitResponse()
        emit(ApiResponse.create(response))

    }.catch { error ->
        emit(ApiResponse.create(error))
    }
}

class ResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != Flow::class.java) {
            return null
        }
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        require(rawObservableType == ApiResponse::class.java) { "type must be a resource" }
        require(observableType is ParameterizedType) { "resource must be parameterized" }
        val bodyType = getParameterUpperBound(0, observableType)
        return ResponseAdapter<Any>(bodyType)
    }

}