package com.mint.weaponmestari.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mint.weaponmestari.model.database.WeaponEntity

@Dao
interface WeaponDAO {

    @Query("SELECT * FROM WeaponEntity")
    suspend fun getAllWeapons(): List<WeaponEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllWeapons(weaponList: List<WeaponEntity>)
}