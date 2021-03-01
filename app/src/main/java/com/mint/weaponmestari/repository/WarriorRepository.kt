package com.mint.weaponmestari.repository

import android.util.Log
import com.mint.weaponmestari.database.WarriorAndWeaponsDAO
import com.mint.weaponmestari.database.WarriorDAO
import com.mint.weaponmestari.model.database.WarriorWeaponsEntity
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.model.mapper.mapListWarriorFromEntity
import com.mint.weaponmestari.model.mapper.mapListWarriorResponseToEntity
import com.mint.weaponmestari.networking.Resource
import com.mint.weaponmestari.networking.WeaponService
import com.mint.weaponmestari.networking.networkBoundResource
import kotlinx.coroutines.flow.Flow

class WarriorRepository(
    private val warriorDAO: WarriorDAO,
    private val warriorAndWeaponsDAO: WarriorAndWeaponsDAO,
    private val weaponService: WeaponService
) {

    fun fetchWarriors(): Flow<Resource<List<Warrior>>> {
        return networkBoundResource(
            fetchFromRemote = { weaponService.fetchWarriors() },
            saveToDB = {
                warriorDAO.saveWarriors(mapListWarriorResponseToEntity(it))
                it.forEach { warriorResponse ->
                    warriorResponse.weaponList.forEach { weaponId ->
                        warriorAndWeaponsDAO.insertWarriorAndWeapons(WarriorWeaponsEntity(warriorResponse.id, weaponId))
                    }
                }
            },
            fetchFromLocal = { mapListWarriorFromEntity(warriorDAO.getWarriorAndWeapon()) }
        )
    }

    suspend fun getAllWarriors() = mapListWarriorFromEntity(warriorDAO.getWarriorAndWeapon())
}