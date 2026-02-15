package com.rpla.fakestore.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rpla.fakestore.core.database.dao.FavoriteProductDao
import com.rpla.fakestore.core.database.entity.FavoriteProductEntity

@Database(
    entities = [FavoriteProductEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class FakeStoreDatabase : RoomDatabase() {
    abstract val favoriteProductDao: FavoriteProductDao
}
