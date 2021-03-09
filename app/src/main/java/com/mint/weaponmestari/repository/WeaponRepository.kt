package com.mint.weaponmestari.repository

import com.mint.weaponmestari.database.WeaponDAO
import com.mint.weaponmestari.model.local.Weapon
import com.mint.weaponmestari.model.mapper.mapListWeaponFromEntity
import com.mint.weaponmestari.model.mapper.mapListWeaponResponseToEntity
import com.mint.weaponmestari.networking.Resource
import com.mint.weaponmestari.networking.WeaponService
import com.mint.weaponmestari.networking.networkBoundResource
import kotlinx.coroutines.flow.Flow

class WeaponRepository(
    private val weaponDAO: WeaponDAO,
    private val weaponService: WeaponService
) {

    fun fetchWeapons(): Flow<Resource<List<Weapon>>> {
        return networkBoundResource(
            fetchFromRemote = { weaponService.fetchWeapons() },
            saveToDB = { weaponDAO.saveAllWeapons(mapListWeaponResponseToEntity(it)) },
            fetchFromLocal = { mapListWeaponFromEntity(weaponDAO.getAllWeapons()) }
        )
    }

    suspend fun getWeapons(): List<Weapon> = mapListWeaponFromEntity(weaponDAO.getAllWeapons())
}