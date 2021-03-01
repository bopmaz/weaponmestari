package com.mint.weaponmestari.presentation

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mint.weaponmestari.databinding.ActivityMainBinding
import com.mint.weaponmestari.databinding.ViewLoadingDialogBinding
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.model.local.Weapon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingDialog: AlertDialog

    private val viewModel: MainViewModel by viewModels()

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
                }
            }
        }
    }

    private fun setupWarriorList(warriorList: List<Warrior>) {
        binding.gridViewWarriors.adapter = WarriorAdapter(this, warriorList)
        binding.gridViewWarriors.setOnItemClickListener { _, _, position, _ ->
            sendIntent(MainIntent.ItemClicked(warriorList[position]))
        }
    }

    private fun openInventory(weaponList: List<Weapon>, warrior: Warrior) {
        val weaponArray = weaponList.map { it.weaponType.type }.toTypedArray()
        val weaponSelectionDialog = AlertDialog.Builder(this)
            .setSingleChoiceItems(weaponArray, 0) { dialog, position ->
                sendIntent(MainIntent.WeaponSelected(weaponList[position], warrior))
                dialog.dismiss()
            }.show()
    }

    private fun sendIntent(intent: MainIntent) {
        lifecycleScope.launchWhenCreated {
            viewModel.mainIntent.send(intent)
        }
    }
}