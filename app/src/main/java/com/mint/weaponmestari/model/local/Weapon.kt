package com.mint.weaponmestari.model.local

data class Weapon(
    val weaponType: WeaponType
)

enum class WeaponType(val type: String) {
    SWORD("sword"),
    UNDEFINED("");

    companion object {
        fun from(typeName: String) = values().firstOrNull { it.type == typeName } ?: UNDEFINED
    }
}