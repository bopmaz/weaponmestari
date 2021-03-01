package com.mint.weaponmestari.di

import com.mint.weaponmestari.database.WarriorAndWeaponsDAO
import com.mint.weaponmestari.database.WarriorDAO
import com.mint.weaponmestari.database.WeaponDAO
import com.mint.weaponmestari.networking.WeaponService
import com.mint.weaponmestari.repository.WarriorRepository
import com.mint.weaponmestari.repository.WeaponRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideWarriorRepository(warriorDAO: WarriorDAO,
                                 warriorAndWeaponsDAO: WarriorAndWeaponsDAO,
                                 weaponService: WeaponService): WarriorRepository {
        return WarriorRepository(warriorDAO, warriorAndWeaponsDAO, weaponService)
    }

    @Singleton
    @Provides
    fun provideWeaponRepository(weaponDAO: WeaponDAO, weaponService: WeaponService): WeaponRepository {
        return WeaponRepository(weaponDAO, weaponService)
    }
}