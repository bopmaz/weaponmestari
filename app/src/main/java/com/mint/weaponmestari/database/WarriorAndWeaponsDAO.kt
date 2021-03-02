package com.mint.weaponmestari.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.mint.weaponmestari.model.database.WarriorWeaponsEntity

@Dao
interface WarriorAndWeaponsDAO {

    @Insert
    suspend fun insertWarriorAndWeapons(warriorWeaponsEntity: WarriorWeaponsEntity)

    @Update
    suspend fun updateWarriorAndWeapons(warriorWeaponsEntity: WarriorWeaponsEntity)

    @Delete
    suspend fun deleteWarriorAndWeapon(warriorWeaponsEntity: WarriorWeaponsEntity)

}