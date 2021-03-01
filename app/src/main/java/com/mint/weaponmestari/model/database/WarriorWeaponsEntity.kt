package com.mint.weaponmestari.model.database

import androidx.room.Entity

@Entity(primaryKeys = ["warriorId", "weaponId"])
data class WarriorWeaponsEntity(
    val warriorId: Int,
    val weaponId: Int
)