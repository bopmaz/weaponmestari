package com.mint.weaponmestari.di;

import android.content.Context
import androidx.room.Room
import com.mint.weaponmestari.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideWarriorDb(@ApplicationContext context: Context): WarriorDatabase {
        return Room
            .databaseBuilder(
                context,
                WarriorDatabase::class.java,
                "weapon.db")
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Singleton
    @Provides
    fun provideWarriorDAO(warriorDatabase: WarriorDatabase): WarriorDAO {
        return warriorDatabase.warriorDAO()
    }

    @Singleton
    @Provides
    fun provideWeaponDAO(warriorDatabase: WarriorDatabase): WeaponDAO {
        return warriorDatabase.weaponDAO()
    }

    @Singleton
    @Provides
    fun provideWarriorAndWeaponsDAO(warriorDatabase: WarriorDatabase): WarriorAndWeaponsDAO {
        return warriorDatabase.warriorAndWeaponsDAO()
    }
}
