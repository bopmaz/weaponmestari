package com.mint.weaponmestari.presentation.warrior

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.TypedEpoxyController
import com.mint.weaponmestari.R
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.model.local.WeaponType
import com.mint.weaponmestari.warrior

class WarriorListController(
    private val context: Context,
    private val onWarriorSelected: OnWarriorSelected
) : TypedEpoxyController<List<Warrior>>() {

    override fun buildModels(data: List<Warrior>) {
        data.forEachIndexed { index, warrior ->
            warrior {
                id(index)
                warriorData(warrior)
                warrior.weaponList.forEachIndexed { index, weapon ->
                    when (index) {
                        0 -> weapon1(getWeaponImage(weapon.weaponType))
                        1 -> weapon2(getWeaponImage(weapon.weaponType))
                        2 -> weapon3(getWeaponImage(weapon.weaponType))
                    }
                }
                onClick(onWarriorSelected)
            }
        }
    }

    private fun getWeaponImage(weaponType: WeaponType): Drawable? {
        return when (weaponType) {
            WeaponType.SWORD -> ContextCompat.getDrawable(context, R.drawable.ic_sword)
            WeaponType.SPEAR -> ContextCompat.getDrawable(context, R.drawable.ic_spear)
            WeaponType.UNDEFINED -> ContextCompat.getDrawable(context, R.drawable.ic_question)
        }
    }
}

interface OnWarriorSelected {
    fun select(warrior: Warrior)
}
