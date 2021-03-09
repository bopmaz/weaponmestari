package com.mint.weaponmestari.database

import androidx.room.*
import com.mint.weaponmestari.model.database.WarriorAndWeapon
import com.mint.weaponmestari.model.database.WarriorEntity

@Dao
interface WarriorDAO {

    @Transaction
    @Query("SELECT * FROM WarriorEntity")
    suspend fun getWarriorAndWeapon(): List<WarriorAndWeapon>

    @Transaction
    @Query("SELECT * FROM WarriorEntity WHERE warriorId = :id")
    suspend fun getWeaponForWarrior(id: Int): WarriorAndWeapon

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWarriors(warriorList: List<WarriorEntity>)
}