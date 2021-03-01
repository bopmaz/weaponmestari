package com.mint.weaponmestari.model.mapper

import com.mint.weaponmestari.model.database.WarriorAndWeapon
import com.mint.weaponmestari.model.database.WarriorEntity
import com.mint.weaponmestari.model.database.WarriorType
import com.mint.weaponmestari.model.database.WeaponEntity
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.model.local.Weapon
import com.mint.weaponmestari.model.local.WeaponType
import com.mint.weaponmestari.model.remote.WarriorResponse
import com.mint.weaponmestari.model.remote.WeaponResponse

fun mapSingleWarriorResponseToEntity(input: WarriorResponse): WarriorEntity =
    WarriorEntity(
        input.id,
        input.name,
        WarriorType.from(input.type),
        input.damage,
        input.armor,
        input.weaponId)

fun mapListWarriorResponseToEntity(input: List<WarriorResponse>): List<WarriorEntity> =
    input.map { mapSingleWarriorResponseToEntity(it) }

fun mapSingleWarriorFromEntity(input: WarriorAndWeapon): Warrior =
    Warrior(
        input.warrior.name,
        listOf(Weapon(WeaponType.from(input.weaponEntity.weaponType))))

fun mapListWarriorFromEntity(input: List<WarriorAndWeapon>): List<Warrior> =
    input.map { mapSingleWarriorFromEntity(it) }

fun mapSingleWeaponResponseToEntity(input: WeaponResponse): WeaponEntity =
    WeaponEntity(
        input.weaponId,
        input.weaponType,
        input.damage,
        input.range)

fun mapListWeaponResponseToEntity(input: List<WeaponResponse>): List<WeaponEntity> =
    input.map { mapSingleWeaponResponseToEntity(it) }

fun mapSingleWeaponFromEntity(input: WeaponEntity): Weapon =
    Weapon(WeaponType.from(input.weaponType))

fun mapListWeaponFromEntity(input: List<WeaponEntity>): List<Weapon> =
    input.map { mapSingleWeaponFromEntity(it) }