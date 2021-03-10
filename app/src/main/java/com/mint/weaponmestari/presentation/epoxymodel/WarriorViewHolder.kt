package com.mint.weaponmestari.presentation.epoxymodel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.mint.weaponmestari.R
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.model.local.WeaponType
import com.mint.weaponmestari.presentation.warrior.OnWarriorSelected


@SuppressLint("NonConstantResourceId")
@EpoxyModelClass(layout = R.layout.view_holder_warrior)
abstract class WarriorViewHolder(private val context: Context) : EpoxyModelWithHolder<WarriorViewHolder.Holder?>() {
    @EpoxyAttribute
    lateinit var warrior: Warrior

    @EpoxyAttribute
    lateinit var clickListener: OnWarriorSelected

    override fun bind(holder: Holder) {
        warrior.apply {
            holder.nameTextView.text = name

            val weaponImageList = listOf(holder.weapon1, holder.weapon2, holder.weapon3)

            for (index in 0..2) {
                weaponList.getOrNull(index).apply {
                    if (this != null) {
                        weaponImageList[index].setImageDrawable(getWeaponImage(this.weaponType))
                    } else {
                        weaponImageList[index].setImageDrawable(null)
                    }
                }
            }

            weaponList.forEachIndexed { index, weapon ->
                when (index) {
                    0 -> holder.weapon1.setImageDrawable(getWeaponImage(weapon.weaponType))
                    1 -> holder.weapon2.setImageDrawable(getWeaponImage(weapon.weaponType))
                    2 -> holder.weapon3.setImageDrawable(getWeaponImage(weapon.weaponType))
                }
            }
            
            holder.rootView.setOnClickListener { clickListener.select(this) }
        }
    }

    private fun getWeaponImage(weaponType: WeaponType): Drawable? {
        return when (weaponType) {
            WeaponType.SWORD -> ContextCompat.getDrawable(context, R.drawable.ic_sword)
            WeaponType.SPEAR -> ContextCompat.getDrawable(context, R.drawable.ic_spear)
            WeaponType.UNDEFINED -> ContextCompat.getDrawable(context, R.drawable.ic_question)
        }
    }

    class Holder : EpoxyHolder() {
        lateinit var nameTextView: TextView
        lateinit var weapon1: ImageView
        lateinit var weapon2: ImageView
        lateinit var weapon3: ImageView
        lateinit var rootView: View

        override fun bindView(itemView: View) {
            rootView = itemView
            nameTextView = itemView.findViewById(R.id.text_view_warrior_name)
            weapon1 = itemView.findViewById(R.id.image_view_weapon_slot_1)
            weapon2 = itemView.findViewById(R.id.image_view_weapon_slot_2)
            weapon3 = itemView.findViewById(R.id.image_view_weapon_slot_3)
        }
    }
}