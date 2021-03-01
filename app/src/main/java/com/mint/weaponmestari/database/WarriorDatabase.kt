package com.mint.weaponmestari.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mint.weaponmestari.model.database.WarriorEntity
import com.mint.weaponmestari.model.database.WarriorWeaponsEntity
import com.mint.weaponmestari.model.database.WeaponEntity

@Database(entities = [WarriorEntity::class, WeaponEntity::class, WarriorWeaponsEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class WarriorDatabase : RoomDatabase() {
    abstract fun warriorDAO(): WarriorDAO
    abstract fun weaponDAO(): WeaponDAO
    abstract fun warriorAndWeaponsDAO(): WarriorAndWeaponsDAO
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE TABLE warrior_backup(
                warriorId INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                damage INTEGER NOT NULL,
                armor INTEGER NOT NULL)
                  """.trimIndent())
        database.execSQL(
            """
                INSERT INTO warrior_backup SELECT warriorId, name, type, damage, armor FROM WarriorEntity;
            """.trimIndent()
        )
        database.execSQL(
            """
                DROP TABLE WarriorEntity;
            """.trimIndent()
        )
        database.execSQL(
            """
            CREATE TABLE WarriorEntity(
                warriorId INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                damage INTEGER NOT NULL,
                armor INTEGER NOT NULL)
            """.trimIndent())
        database.execSQL(
            """
                INSERT INTO WarriorEntity SELECT warriorId, name, type, damage, armor FROM warrior_backup;
            """.trimIndent()
        )
        database.execSQL(
            """
                DROP TABLE warrior_backup;
            """.trimIndent()
        )
    }
}