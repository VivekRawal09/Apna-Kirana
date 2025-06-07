package com.example.apnakirana.presentation.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnakirana.data.local.entity.Address
import com.example.apnakirana.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressManagementViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressManagementUiState())
    val uiState: StateFlow<AddressManagementUiState> = _uiState.asStateFlow()

    init {
        loadAddresses()
    }

    private fun loadAddresses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                orderRepository.getAddresses().collect { addresses ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        addresses = addresses
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load addresses"
                )
            }
        }
    }

    fun setDefaultAddress(addressId: String) {
        viewModelScope.launch {
            try {
                orderRepository.setDefaultAddress(addressId)
                // Addresses will be automatically updated via the Flow
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to set default address"
                )
            }
        }
    }

    fun deleteAddress(addressId: String) {
        viewModelScope.launch {
            try {
                orderRepository.deleteAddress(addressId)
                // Addresses will be automatically updated via the Flow
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to delete address"
                )
            }
        }
    }

    fun refreshAddresses() {
        loadAddresses()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class AddressManagementUiState(
    val isLoading: Boolean = false,
    val addresses: List<Address> = emptyList(),
    val errorMessage: String? = null
)