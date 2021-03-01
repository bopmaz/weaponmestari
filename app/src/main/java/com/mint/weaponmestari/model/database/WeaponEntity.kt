package com.mint.weaponmestari.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeaponEntity(
    @PrimaryKey val weaponId: Int,
    val weaponType: String,
    val damage: Int,
    val range: Int
)