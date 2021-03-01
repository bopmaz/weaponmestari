package com.mint.weaponmestari.model.database

import androidx.room.Embedded
import androidx.room.Relation

data class WarriorAndWeapon(
    @Embedded val warrior: WarriorEntity,
    @Relation(
        parentColumn = "weaponId",
        entityColumn = "weaponId"
    )
    val weaponEntity: WeaponEntity
)