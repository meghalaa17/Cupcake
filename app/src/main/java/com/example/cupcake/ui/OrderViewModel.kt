package com.example.cupcake.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cupcake.data.OrderEntity
import com.example.cupcake.data.OrderRepository
import com.example.cupcake.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

/**
 * [OrderViewModel] holds information about a cupcake order and connects to
 * the [OrderRepository] for persistent Room database storage.
 *
 * @param repository The repository that handles all data operations.
 */
class OrderViewModel(private val repository: OrderRepository) : ViewModel() {

    // -------------------------------------------------------------------------
    // Current order UI state (in-memory, tracks the order being built)
    // -------------------------------------------------------------------------

    private val _uiState = MutableStateFlow(OrderUiState(pickupOptions = pickupOptions()))
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    // -------------------------------------------------------------------------
    // Saved orders from Room (persistent, survives app restarts)
    // -------------------------------------------------------------------------

    /**
     * A [StateFlow] of all saved orders from the Room database.
     * The UI can collect this to display order history.
     */
    val savedOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // -------------------------------------------------------------------------
    // Order-building functions (same as before)
    // -------------------------------------------------------------------------

    fun setQuantity(numberCupcakes: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                quantity = numberCupcakes,
                price = calculatePrice(quantity = numberCupcakes)
            )
        }
    }

    fun setFlavor(desiredFlavor: String) {
        _uiState.update { currentState ->
            currentState.copy(flavor = desiredFlavor)
        }
    }

    fun setDate(pickupDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                date = pickupDate,
                price = calculatePrice(pickupDate = pickupDate)
            )
        }
    }

    fun resetOrder() {
        _uiState.value = OrderUiState(pickupOptions = pickupOptions())
    }

    // -------------------------------------------------------------------------
    // Room persistence functions
    // -------------------------------------------------------------------------

    /**
     * Save the current order to the Room database.
     * Called when the user confirms the order on the Summary screen.
     */
    fun saveOrder() {
        val currentState = _uiState.value
        val orderEntity = OrderEntity(
            quantity = currentState.quantity,
            flavor = currentState.flavor,
            date = currentState.date,
            price = currentState.price
        )
        viewModelScope.launch {
            repository.insert(orderEntity)
        }
    }

    /**
     * Delete a specific saved order from the database.
     */
    fun deleteOrder(order: OrderEntity) {
        viewModelScope.launch {
            repository.delete(order)
        }
    }

    /**
     * Delete all saved orders from the database.
     */
    fun deleteAllOrders() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private fun calculatePrice(
        quantity: Int = _uiState.value.quantity,
        pickupDate: String = _uiState.value.date
    ): String {
        var calculatedPrice = quantity * PRICE_PER_CUPCAKE
        if (pickupOptions()[0] == pickupDate) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        return NumberFormat.getCurrencyInstance().format(calculatedPrice)
    }

    private fun pickupOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            dateOptions.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return dateOptions
    }

    // -------------------------------------------------------------------------
    // ViewModelFactory — required because our ViewModel takes a constructor param
    // -------------------------------------------------------------------------

    companion object {
        /**
         * Factory for creating [OrderViewModel] with its [OrderRepository] dependency.
         * Pass this to [viewModel()] in Compose: viewModel(factory = OrderViewModel.Factory)
         */
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // The factory needs the application's repository.
                // This requires the ViewModel to be created in an Activity/Fragment context
                // where the Application is available.
                throw UnsupportedOperationException(
                    "Use OrderViewModelFactory instead of this companion factory."
                )
            }
        }
    }
}