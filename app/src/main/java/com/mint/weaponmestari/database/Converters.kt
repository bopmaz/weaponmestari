package com.mint.weaponmestari.database

import androidx.room.TypeConverter
import com.mint.weaponmestari.model.database.WarriorType

class Converters {

    @TypeConverter
    fun toWarriorType(warriorType: String): WarriorType = WarriorType.from(warriorType)

    @TypeConverter
    fun fromWarriorType(warriorType: WarriorType): String = warriorType.type
}