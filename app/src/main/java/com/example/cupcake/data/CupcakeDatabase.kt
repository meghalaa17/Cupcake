package com.example.cupcake.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Database for the Cupcake app.
 *
 * Annotated with @Database to declare the entities it manages and its version.
 * Uses the singleton pattern to prevent multiple instances of the database being opened simultaneously.
 */
@Database(entities = [OrderEntity::class], version = 1, exportSchema = false)
abstract class CupcakeDatabase : RoomDatabase() {

    /**
     * Provides the DAO for order operations.
     */
    abstract fun orderDao(): OrderDao

    companion object {
        /**
         * @Volatile ensures the value of INSTANCE is always up to date on all threads.
         * The value of a volatile variable is never cached; all reads and writes are to/from main memory.
         */
        @Volatile
        private var INSTANCE: CupcakeDatabase? = null

        /**
         * Returns the singleton instance of [CupcakeDatabase].
         * Creates the database if it doesn't exist yet.
         */
        fun getDatabase(context: Context): CupcakeDatabase {
            // Return the existing instance if available; otherwise create a new one.
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CupcakeDatabase::class.java,
                    "cupcake_database"
                )
                    .fallbackToDestructiveMigration() // Recreate DB if schema changes (dev only)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}