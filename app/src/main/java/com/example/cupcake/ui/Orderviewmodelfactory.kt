package com.example.cupcake.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cupcake.data.OrderRepository

/**
 * Factory for creating [OrderViewModel] with its [OrderRepository] dependency injected.
 *
 * Because [OrderViewModel] takes a constructor parameter (the repository),
 * we cannot use the default [viewModel()] factory. This factory bridges
 * the gap by providing the repository when Android creates the ViewModel.
 *
 * Usage in Compose:
 * ```kotlin
 * val factory = OrderViewModelFactory((application as CupcakeApplication).repository)
 * val viewModel: OrderViewModel = viewModel(factory = factory)
 * ```
 */
class OrderViewModelFactory(private val repository: OrderRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}