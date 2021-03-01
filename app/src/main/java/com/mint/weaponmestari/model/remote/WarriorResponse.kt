package com.mint.weaponmestari.model.remote

import com.google.gson.annotations.SerializedName

data class WarriorResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("damage")
    val damage: Int,
    @SerializedName("armor")
    val armor: Int,
    @SerializedName("weapon_id")
    val weaponId: Int
)