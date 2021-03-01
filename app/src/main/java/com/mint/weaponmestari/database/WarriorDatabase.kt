package com.mint.weaponmestari.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mint.weaponmestari.model.database.WarriorEntity
import com.mint.weaponmestari.model.database.WeaponEntity

@Database(entities = [WarriorEntity::class, WeaponEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class WarriorDatabase : RoomDatabase() {
    abstract fun warriorDAO(): WarriorDAO
    abstract fun weaponDAO(): WeaponDAO
}