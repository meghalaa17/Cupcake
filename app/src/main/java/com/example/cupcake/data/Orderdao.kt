package com.example.cupcake.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the orders table.
 * Defines all database operations for [OrderEntity].
 */
@Dao
interface OrderDao {

    /**
     * Insert a new order. If the order already exists, replace it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity)

    /**
     * Delete a specific order from the database.
     */
    @Delete
    suspend fun delete(order: OrderEntity)

    /**
     * Retrieve all orders, sorted by most recent first.
     * Returns a [Flow] so the UI updates automatically when data changes.
     */
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    /**
     * Delete all orders (useful for cancel/reset).
     */
    @Query("DELETE FROM orders")
    suspend fun deleteAll()
}