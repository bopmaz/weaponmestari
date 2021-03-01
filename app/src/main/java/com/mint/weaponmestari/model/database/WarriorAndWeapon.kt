package com.mint.weaponmestari.model.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WarriorAndWeapon(
    @Embedded
    val warrior: WarriorEntity,

    @Relation(
        parentColumn = "warriorId",
        entityColumn = "weaponId",
        associateBy = Junction(WarriorWeaponsEntity::class)
    )
    val weaponEntity: List<WeaponEntity>
)