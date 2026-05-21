package com.example.cupcake

import android.app.Application
import com.example.cupcake.data.CupcakeDatabase
import com.example.cupcake.data.OrderRepository

/**
 * Custom [Application] class that initialises the [CupcakeDatabase] and [OrderRepository]
 * as singletons available throughout the app's lifetime.
 *
 * Remember to register this class in AndroidManifest.xml:
 *   android:name=".CupcakeApplication"
 */
class CupcakeApplication : Application() {

    /**
     * The database instance. Created lazily — only when first accessed.
     */
    val database: CupcakeDatabase by lazy {
        CupcakeDatabase.getDatabase(this)
    }

    /**
     * The repository instance. Created lazily using the database's DAO.
     */
    val repository: OrderRepository by lazy {
        OrderRepository(database.orderDao())
    }
}