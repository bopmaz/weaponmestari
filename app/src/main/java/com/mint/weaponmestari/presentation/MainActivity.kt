package com.mint.weaponmestari.presentation

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import com.mint.weaponmestari.R
import com.mint.weaponmestari.databinding.ActivityMainBinding
import com.mint.weaponmestari.databinding.ViewLoadingDialogBinding
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.model.local.Weapon
import com.mint.weaponmestari.presentation.warrior.OnWarriorSelected
import com.mint.weaponmestari.presentation.warrior.WarriorListController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingDialog: AlertDialog

    private val viewModel: MainViewModel by viewModels()
    private lateinit var warriorController: WarriorListController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        observeViewModel()
        sendIntent(MainIntent.LoadWeapon)
    }

    private fun setupView() {
        val dialogBinding = ViewLoadingDialogBinding.inflate(layoutInflater)
        loadingDialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.consumeAsFlow().collect {
                loadingDialog.dismiss()
                when (it) {
                    MainState.Loading -> loadingDialog.show()
                    MainState.Error -> Unit
                    is MainState.WarriorLoaded -> setupWarriorList(it.warriorList)
                    is MainState.InventoryAvailable -> openInventory(it.weaponList, it.warrior)
                    is MainState.WarriorUpdated -> updateWarrior(it.warriorList, it.warriorIndex)
                }
            }
        }
    }

    private fun setupWarriorList(warriorList: List<Warrior>) {
        warriorController = WarriorListController(this, object : OnWarriorSelected {
            override fun select(warrior: Warrior) {
                sendIntent(MainIntent.ItemClicked(warrior))
            }
        })
        binding.recyclerViewWarrior.adapter = warriorController.adapter
        (binding.recyclerViewWarrior.itemAnimator as SimpleItemAnimator)
            .supportsChangeAnimations = false
        warriorController.setData(warriorList)
    }

    private fun updateWarrior(warriorList: List<Warrior>, warriorIndex: Int) {
        warriorController.setData(warriorList)
        warriorController.notifyModelChanged(warriorIndex)
    }

    private fun openInventory(weaponList: List<Weapon>, warrior: Warrior) {
        val weaponArray = weaponList.map { it.weaponType.type }.toTypedArray()
        val checkedOption = weaponList.map { warrior.weaponList.contains(it) }.toBooleanArray()
        val selectedWeapon = weaponList.filter { warrior.weaponList.contains(it) }.toMutableList()
        AlertDialog.Builder(this)
            .setMultiChoiceItems(weaponArray, checkedOption) { dialog, position, isChecked ->
                if (isChecked) {
                    selectedWeapon.add(weaponList[position])
                } else {
                    selectedWeapon.remove(weaponList[position])
                }
            }
            .setPositiveButton(R.string.done) { dialog, _ ->
                sendIntent(MainIntent.WeaponSelected(selectedWeapon, warrior))
                dialog.dismiss()
            }.show()
    }

    private fun sendIntent(intent: MainIntent) {
        lifecycleScope.launchWhenCreated {
            viewModel.mainIntent.send(intent)
        }
    }
}