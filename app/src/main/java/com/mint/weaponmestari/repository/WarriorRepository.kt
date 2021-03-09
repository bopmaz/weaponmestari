package com.mint.weaponmestari.repository

import com.mint.weaponmestari.database.WarriorAndWeaponsDAO
import com.mint.weaponmestari.database.WarriorDAO
import com.mint.weaponmestari.model.database.WarriorWeaponsEntity
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.model.local.Weapon
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

    suspend fun updateWeapon(warrior: Warrior, weaponList: List<Weapon>) {
        val currentWeaponIds = warriorDAO.getWeaponForWarrior(warrior.id).weaponEntity.map { it.weaponId }
        val selectedWeaponIds = weaponList.map { it.id }
        val additionListIds = selectedWeaponIds.filter { it !in currentWeaponIds }
        val deletionListIds = currentWeaponIds.filter { it !in selectedWeaponIds }

        additionListIds.onEach { warriorAndWeaponsDAO.insertWarriorAndWeapons(WarriorWeaponsEntity(warrior.id, it)) }
        deletionListIds.onEach { warriorAndWeaponsDAO.deleteWarriorAndWeapon(WarriorWeaponsEntity(warrior.id, it)) }
    }
}