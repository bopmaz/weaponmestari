package com.mint.weaponmestari.networking

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

inline fun <LOCAL, REMOTE> networkBoundResource(
    crossinline fetchFromRemote: () -> Flow<ApiResponse<REMOTE>>,
    crossinline saveToDB: suspend (response: REMOTE) -> Unit,
    crossinline fetchFromLocal: suspend () -> LOCAL
): Flow<Resource<LOCAL>> = flow {

    emit(Resource.Loading<LOCAL>())

    fetchFromRemote().collect { apiResponse ->
        when (apiResponse) {
            is ApiSuccessResponse -> {
                try {
                    apiResponse.body?.let {
                        saveToDB(it)
                        emit(Resource.Success(fetchFromLocal()))
                    }
                } catch (e: Exception) {
                    emit(Resource.Error<LOCAL>(e.toString()))
                }
            }

            is ApiEmptyResponse -> {
                emit(Resource.Success(fetchFromLocal()))
            }

            is ApiErrorResponse -> {
                emit(Resource.Error<LOCAL>(apiResponse.errorMessage))
            }
        }
    }
}