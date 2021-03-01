package com.mint.weaponmestari.database

import androidx.room.Dao
import androidx.room.Insert
import com.mint.weaponmestari.model.database.WarriorWeaponsEntity

@Dao
interface WarriorAndWeaponsDAO {

    @Insert
    suspend fun insertWarriorAndWeapons(warriorWeaponsEntity: WarriorWeaponsEntity)
}