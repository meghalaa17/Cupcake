package com.example.cupcake.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 Represents a saved cupcake order stored in the Room database
 */
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val quantity: Int,
    val flavor: String,
    val date: String,
    val price: String,
    val timestamp: Long = System.currentTimeMillis()
)