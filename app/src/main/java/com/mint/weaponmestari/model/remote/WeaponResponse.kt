package com.mint.weaponmestari.model.remote

import com.google.gson.annotations.SerializedName

data class WeaponResponse(
    @SerializedName("weapon_id")
    val weaponId: Int,
    @SerializedName("weapon_type")
    val weaponType: String,
    @SerializedName("damage")
    val damage: Int,
    @SerializedName("range")
    val range: Int
)
