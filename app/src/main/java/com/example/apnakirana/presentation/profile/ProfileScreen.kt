package com.example.apnakirana.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    onOrderHistoryClick: () -> Unit,
    onAddressManagementClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header
        item {
            ProfileHeader(
                userName = uiState.userName,
                userEmail = uiState.userEmail,
                joinDate = uiState.joinDate
            )
        }

        // Statistics Cards
        item {
            StatisticsSection(
                totalOrders = uiState.totalOrders,
                totalSpent = uiState.totalSpent,
                savedAddresses = uiState.savedAddresses
            )
        }

        // Menu Options
        item {
            MenuSection(
                onOrderHistoryClick = onOrderHistoryClick,
                onAddressManagementClick = onAddressManagementClick
            )
        }

        // App Information
        item {
            AppInfoSection()
        }
    }

//    // Loading state
//    if (uiState.isLoading) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    }
}

@Composable
private fun ProfileHeader(
    userName: String,
    userEmail: String,
    joinDate: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Avatar
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(2).uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Member since $joinDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatisticsSection(
    totalOrders: Int,
    totalSpent: Double,
    savedAddresses: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Orders",
            value = totalOrders.toString(),
            icon = Icons.Default.Receipt,
            color = Color(0xFF2196F3)
        )

        StatCard(
            modifier = Modifier.weight(1f),
            title = "Spent",
            value = "₹${String.format("%.0f", totalSpent)}",
            icon = Icons.Default.CurrencyRupee,
            color = Color(0xFF4CAF50)
        )

        StatCard(
            modifier = Modifier.weight(1f),
            title = "Addresses",
            value = savedAddresses.toString(),
            icon = Icons.Default.LocationOn,
            color = Color(0xFFFF9800)
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MenuSection(
    onOrderHistoryClick: () -> Unit,
    onAddressManagementClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Account",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            MenuOption(
                icon = Icons.Default.Receipt,
                title = "Order History",
                subtitle = "View your past orders",
                onClick = onOrderHistoryClick
            )

            MenuOption(
                icon = Icons.Default.LocationOn,
                title = "Manage Addresses",
                subtitle = "Add, edit or delete addresses",
                onClick = onAddressManagementClick
            )

            MenuOption(
                icon = Icons.Default.Favorite,
                title = "Wishlist",
                subtitle = "Your saved items",
                onClick = { /* TODO: Implement wishlist */ }
            )

            MenuOption(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Manage notification settings",
                onClick = { /* TODO: Implement notifications */ }
            )

            MenuOption(
                icon = Icons.Default.HelpOutline,
                title = "Help & Support",
                subtitle = "Get help with your orders",
                onClick = { /* TODO: Implement help */ }
            )
        }
    }
}

@Composable
private fun MenuOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun AppInfoSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "App Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            MenuOption(
                icon = Icons.Default.Info,
                title = "About ApnaKirana",
                subtitle = "Version 1.0.0",
                onClick = { /* TODO: Implement about */ }
            )

            MenuOption(
                icon = Icons.Default.Policy,
                title = "Privacy Policy",
                subtitle = "How we handle your data",
                onClick = { /* TODO: Implement privacy policy */ }
            )

            MenuOption(
                icon = Icons.Default.Description,
                title = "Terms of Service",
                subtitle = "Terms and conditions",
                onClick = { /* TODO: Implement terms */ }
            )

            MenuOption(
                icon = Icons.Default.Star,
                title = "Rate App",
                subtitle = "Rate us on Play Store",
                onClick = { /* TODO: Implement rating */ }
            )
        }
    }
}