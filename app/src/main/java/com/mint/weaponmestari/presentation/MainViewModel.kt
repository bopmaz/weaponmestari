package com.mint.weaponmestari.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mint.weaponmestari.model.local.Warrior
import com.mint.weaponmestari.model.local.Weapon
import com.mint.weaponmestari.networking.Resource
import com.mint.weaponmestari.repository.WarriorRepository
import com.mint.weaponmestari.repository.WeaponRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val weaponRepository: WeaponRepository,
    private val warriorRepository: WarriorRepository
) : ViewModel() {
    private val _state = Channel<MainState>(Channel.UNLIMITED)
    val state: Channel<MainState>
        get() = _state
    val mainIntent = Channel<MainIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            mainIntent.consumeAsFlow().collect {
                when (it) {
                    MainIntent.LoadWeapon -> fetchWeapons()
                    is MainIntent.ItemClicked -> handleItemClick(it.warrior)
                    is MainIntent.WeaponSelected -> handleWeaponSelected(it.weaponList, it.warrior)
                }
            }
        }
    }

    private suspend fun handleItemClick(warrior: Warrior) {
        val weaponList = weaponRepository.getWeapons()
        sendStateChange(MainState.InventoryAvailable(weaponList, warrior))
    }

    private suspend fun fetchWeapons() {
        if (weaponRepository.getWeapons().isNullOrEmpty()) {
            weaponRepository.fetchWeapons().onEach {
                when (it) {
                    is Resource.Error -> sendStateChange(MainState.Error)
                    is Resource.Loading -> sendStateChange(MainState.Loading)
                    is Resource.Success -> fetchWarriors()
                }
            }.launchIn(viewModelScope)
        } else {
            fetchWarriors()
        }
    }

    private suspend fun fetchWarriors() {
        val warriorList = warriorRepository.getAllWarriors()
        if (warriorList.isNullOrEmpty()) {
            warriorRepository.fetchWarriors()
                .onEach {
                    when (it) {
                        is Resource.Error -> sendStateChange(MainState.Error)
                        is Resource.Loading -> sendStateChange(MainState.Loading)
                        is Resource.Success -> {
                            if (it.data != null) {
                                sendStateChange(MainState.WarriorLoaded(it.data))
                            } else {
                                sendStateChange(MainState.Error)
                            }
                        }
                    }
                }.launchIn(viewModelScope)
        } else {
            sendStateChange(MainState.WarriorLoaded(warriorList))
        }
    }

    private suspend fun handleWeaponSelected(weaponList: List<Weapon>, warrior: Warrior) {
        warriorRepository.updateWeapon(warrior, weaponList)
        val warriorList = warriorRepository.getAllWarriors()
        val warriorIndex = warriorList.indexOfFirst { it.id == warrior.id }
        sendStateChange(MainState.WarriorUpdated(warriorList, warriorIndex))
    }

    private fun sendStateChange(mainState: MainState) {
        _state.offer(mainState)
    }

}

sealed class MainState {
    object Loading : MainState()
    object Error : MainState()
    class WarriorLoaded(val warriorList: List<Warrior>) : MainState()
    class InventoryAvailable(val weaponList: List<Weapon>, val warrior: Warrior) : MainState()
    class WarriorUpdated(val warriorList: List<Warrior>, val warriorIndex: Int) : MainState()
}

sealed class MainIntent {
    object LoadWeapon : MainIntent()
    class ItemClicked(val warrior: Warrior) : MainIntent()
    class WeaponSelected(val weaponList: List<Weapon>, val warrior: Warrior) : MainIntent()
}