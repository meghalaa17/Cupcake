package com.example.cupcake.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cupcake.R
import com.example.cupcake.data.OrderEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Displays all orders saved in the Room database.
 * Shows a scrollable list of [OrderEntity] cards, each with a delete button.
 */
@Composable
fun OrderHistoryScreen(
    savedOrders: List<OrderEntity>,
    onDeleteOrder: (OrderEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (savedOrders.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No saved orders yet.",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            items(savedOrders) { order ->
                OrderHistoryItem(
                    order = order,
                    onDeleteOrder = { onDeleteOrder(order) }
                )
            }
        }
    }
}

/**
 * A single card showing the details of a saved [OrderEntity].
 */
@Composable
fun OrderHistoryItem(
    order: OrderEntity,
    onDeleteOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    val savedAt = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        .format(Date(order.timestamp))

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${order.quantity} × ${order.flavor}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Pickup: ${order.date}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Total: ${order.price}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Divider(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = "Saved: $savedAt",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDeleteOrder) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete order"
                )
            }
        }
    }
}