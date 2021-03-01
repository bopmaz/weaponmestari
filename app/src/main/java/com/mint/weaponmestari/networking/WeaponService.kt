package com.mint.weaponmestari.networking

import com.mint.weaponmestari.model.remote.WarriorResponse
import com.mint.weaponmestari.model.remote.WeaponResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface WeaponService {

    @GET("warriors")
    fun fetchWarriors(): Flow<ApiResponse<List<WarriorResponse>>>

    @GET("weapons")
    fun fetchWeapons(): Flow<ApiResponse<List<WeaponResponse>>>
}