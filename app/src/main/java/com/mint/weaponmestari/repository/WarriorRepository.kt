package com.mint.weaponmestari.repository

import com.mint.weaponmestari.database.WarriorDAO
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.model.mapper.mapListWarriorFromEntity
import com.mint.weaponmestari.model.mapper.mapListWarriorResponseToEntity
import com.mint.weaponmestari.networking.Resource
import com.mint.weaponmestari.networking.WeaponService
import com.mint.weaponmestari.networking.networkBoundResource
import kotlinx.coroutines.flow.Flow

class WarriorRepository(
    private val warriorDAO: WarriorDAO,
    private val weaponService: WeaponService
) {

    fun fetchWarriors(): Flow<Resource<List<Warrior>>> {
        return networkBoundResource(
            fetchFromRemote = { weaponService.fetchWarriors() },
            saveToDB = { warriorDAO.saveWarriors(mapListWarriorResponseToEntity(it)) },
            fetchFromLocal = { mapListWarriorFromEntity(warriorDAO.getWarriorAndWeapon()) }
        )
    }

    suspend fun getAllWarriors() = mapListWarriorFromEntity(warriorDAO.getWarriorAndWeapon())
}