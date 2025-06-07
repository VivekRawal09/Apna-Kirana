package com.example.apnakirana.presentation.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(
    onBackPressed: () -> Unit,
    onAddressAdded: () -> Unit,
    viewModel: AddAddressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Listen for address added success
    LaunchedEffect(uiState.isAddressAdded) {
        if (uiState.isAddressAdded) {
            onAddressAdded()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Add New Address") },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Personal Information Section
            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::updateName,
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.nameError != null,
                supportingText = uiState.nameError?.let { { Text(it) } }
            )

            OutlinedTextField(
                value = uiState.phone,
                onValueChange = viewModel::updatePhone,
                label = { Text("Phone Number") },
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.phoneError != null,
                supportingText = uiState.phoneError?.let { { Text(it) } }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Address Information Section
            Text(
                text = "Address Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = uiState.addressLine1,
                onValueChange = viewModel::updateAddressLine1,
                label = { Text("Address Line 1") },
                leadingIcon = {
                    Icon(Icons.Default.Home, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.addressLine1Error != null,
                supportingText = uiState.addressLine1Error?.let { { Text(it) } },
                placeholder = { Text("House/Flat/Building name") }
            )

            OutlinedTextField(
                value = uiState.addressLine2,
                onValueChange = viewModel::updateAddressLine2,
                label = { Text("Address Line 2 (Optional)") },
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Area/Street/Sector") }
            )

            OutlinedTextField(
                value = uiState.landmark,
                onValueChange = viewModel::updateLandmark,
                label = { Text("Landmark (Optional)") },
                leadingIcon = {
                    Icon(Icons.Default.Place, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Near famous place") }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.city,
                    onValueChange = viewModel::updateCity,
                    label = { Text("City") },
                    modifier = Modifier.weight(1f),
                    isError = uiState.cityError != null,
                    supportingText = uiState.cityError?.let { { Text(it) } }
                )

                OutlinedTextField(
                    value = uiState.pincode,
                    onValueChange = viewModel::updatePincode,
                    label = { Text("Pincode") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    isError = uiState.pincodeError != null,
                    supportingText = uiState.pincodeError?.let { { Text(it) } }
                )
            }

            OutlinedTextField(
                value = uiState.state,
                onValueChange = viewModel::updateState,
                label = { Text("State") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.stateError != null,
                supportingText = uiState.stateError?.let { { Text(it) } }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Address Type Section
            Text(
                text = "Address Type",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("HOME", "OFFICE", "OTHER").forEach { type ->
                    FilterChip(
                        onClick = { viewModel.updateAddressType(type) },
                        label = { Text(type) },
                        selected = uiState.addressType == type,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Default Address Option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = uiState.isDefault,
                        onClick = { viewModel.toggleIsDefault() }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.isDefault,
                    onCheckedChange = { viewModel.toggleIsDefault() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Make this my default address",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = { viewModel.saveAddress() },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (uiState.isLoading) "Saving..." else "Save Address",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Error handling
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            viewModel.clearError()
        }
    }
}