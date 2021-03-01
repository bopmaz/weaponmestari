package com.mint.weaponmestari.presentation

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.mint.weaponmestari.R
import com.mint.weaponmestari.databinding.ViewWarriorBinding
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.model.local.WeaponType

class WarriorAdapter(
    context: Context,
    private val warriorList: List<Warrior>
) : ArrayAdapter<Warrior>(context, 0, warriorList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val warrior = warriorList[position]
        val viewHolder: WarriorViewHolder
        if (view == null) {
            viewHolder = WarriorViewHolder(
                context,
                ViewWarriorBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false))
            view = viewHolder.binding.root
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as WarriorViewHolder
        }

        viewHolder.setWarrior(warrior)
        return view
    }
}

class WarriorViewHolder(
    private val context: Context,
    val binding: ViewWarriorBinding
) {
    fun setWarrior(warrior: Warrior) {
        binding.textViewWarriorName.text = warrior.name
        warrior.weaponList.forEachIndexed { index, weapon ->
            when (index) {
                0 -> binding.imageViewWeaponSlot1.setImageDrawable(getWeaponImage(weapon.weaponType))
                1 -> binding.imageViewWeaponSlot2.setImageDrawable(getWeaponImage(weapon.weaponType))
                2 -> binding.imageViewWeaponSlot3.setImageDrawable(getWeaponImage(weapon.weaponType))
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