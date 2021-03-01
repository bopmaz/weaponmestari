package com.mint.weaponmestari.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mint.weaponmestari.model.local.Warrior
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
                }
            }
        }
    }

    private fun fetchWarriors() {
        warriorRepository.fetchWarriors()
            .onEach {
                when (it) {
                    is Resource.Error -> {
                        Log.d("DCM", it.errorString)
                        sendStateChange(MainState.Error)
                    }
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
    }

    private fun fetchWeapons() {
        weaponRepository.fetchWeapons().onEach {
            when (it) {
                is Resource.Error -> {
                    Log.d("DCM", it.errorString)
                    sendStateChange(MainState.Error)
                }
                is Resource.Loading -> sendStateChange(MainState.Loading)
                is Resource.Success -> fetchWarriors()
            }
        }.launchIn(viewModelScope)
    }

    private fun sendStateChange(mainState: MainState) {
        _state.offer(mainState)
    }

}

sealed class MainState {
    object Loading : MainState()
    object Error : MainState()
    class WarriorLoaded(val warriorList: List<Warrior>) : MainState()
}

sealed class MainIntent {
    object LoadWeapon : MainIntent()
}