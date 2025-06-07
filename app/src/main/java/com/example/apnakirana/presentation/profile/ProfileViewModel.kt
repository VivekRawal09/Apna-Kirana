package com.example.apnakirana.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnakirana.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Get order statistics
                val totalOrders = orderRepository.getTotalOrdersCount()
                val totalSpent = orderRepository.getTotalSpent()

                // Get address count
                var addressCount = 0
                orderRepository.getAddresses().collect { addresses ->
                    addressCount = addresses.size
                    return@collect // Just get the count once
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    totalOrders = totalOrders,
                    totalSpent = totalSpent,
                    savedAddresses = addressCount
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load profile data"
                )
            }
        }
    }

    fun refreshProfile() {
        loadProfileData()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userName: String = "John Doe", // In real app, get from user prefs/database
    val userEmail: String = "john.doe@example.com", // In real app, get from user prefs
    val joinDate: String = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date()), // Current date as join date
    val totalOrders: Int = 0,
    val totalSpent: Double = 0.0,
    val savedAddresses: Int = 0,
    val errorMessage: String? = null
)