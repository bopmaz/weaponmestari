package com.mint.weaponmestari.presentation.warrior

import android.content.Context
import com.airbnb.epoxy.TypedEpoxyController
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.presentation.epoxymodel.warriorViewHolder


class WarriorListController(
    private val context: Context,
    private val onWarriorSelected: OnWarriorSelected
) : TypedEpoxyController<List<Warrior>>() {

    override fun buildModels(data: List<Warrior>) {
        data.forEachIndexed { index, warrior ->
            warriorViewHolder(context) {
                id(index)
                warrior(warrior)
                clickListener(onWarriorSelected)
            }
        }
    }
}

interface OnWarriorSelected {
    fun select(warrior: Warrior)
}
