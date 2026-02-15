package com.rpla.fakestore.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rpla.fakestore.core.database.entity.FavoriteProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    @Query("SELECT * FROM favorite_products ORDER BY id ASC")
    fun observeFavorites(): Flow<List<FavoriteProductEntity>>

    @Query("SELECT id FROM favorite_products")
    fun observeFavoriteIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFavorite(entity: FavoriteProductEntity)

    @Query("DELETE FROM favorite_products WHERE id = :productId")
    suspend fun deleteFavoriteById(productId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_products WHERE id = :productId)")
    suspend fun exists(productId: Int): Boolean

    @Transaction
    suspend fun toggleFavorite(entity: FavoriteProductEntity) {
        if (exists(entity.id)) {
            deleteFavoriteById(entity.id)
        } else {
            upsertFavorite(entity)
        }
    }
}
