package com.mint.weaponmestari.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WarriorEntity(
    @PrimaryKey val warriorId: Int,
    val name: String,
    val type: WarriorType,
    val damage: Int,
    val armor: Int,
    val weaponId: Int
)

enum class WarriorType(val type: String) {
    SUPPORT("support"),
    RECON("recon"),
    ASSAULT("assault"),
    DEFENSE("defense"),
    UNDEFINED("");

    companion object {
        fun from(typeName: String) = values().firstOrNull { it.type == typeName } ?: UNDEFINED
    }
}