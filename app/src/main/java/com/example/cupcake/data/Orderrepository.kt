package com.example.cupcake.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, delete, and retrieve functions for [OrderEntity].
 *
 * The Repository is the single source of truth for all order data.
 * It abstracts the data layer from the ViewModel so the ViewModel never
 * interacts directly with the database.
 *
 * @param orderDao The DAO that provides database access.
 */
class OrderRepository(private val orderDao: OrderDao) {

    /**
     * Retrieve all orders from the database as a [Flow].
     * The Flow emits a new value whenever the data in the database changes.
     */
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()

    /**
     * Insert a new order into the database.
     * This is a suspend function, so it must be called from a coroutine or another suspend function.
     */
    suspend fun insert(order: OrderEntity) {
        orderDao.insert(order)
    }

    /**
     * Delete a specific order from the database.
     */
    suspend fun delete(order: OrderEntity) {
        orderDao.delete(order)
    }

    /**
     * Delete all saved orders.
     */
    suspend fun deleteAll() {
        orderDao.deleteAll()
    }
}