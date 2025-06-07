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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAddressUiState())
    val uiState: StateFlow<AddAddressUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = null
        )
    }

    fun updatePhone(phone: String) {
        _uiState.value = _uiState.value.copy(
            phone = phone,
            phoneError = null
        )
    }

    fun updateAddressLine1(addressLine1: String) {
        _uiState.value = _uiState.value.copy(
            addressLine1 = addressLine1,
            addressLine1Error = null
        )
    }

    fun updateAddressLine2(addressLine2: String) {
        _uiState.value = _uiState.value.copy(addressLine2 = addressLine2)
    }

    fun updateLandmark(landmark: String) {
        _uiState.value = _uiState.value.copy(landmark = landmark)
    }

    fun updateCity(city: String) {
        _uiState.value = _uiState.value.copy(
            city = city,
            cityError = null
        )
    }

    fun updateState(state: String) {
        _uiState.value = _uiState.value.copy(
            state = state,
            stateError = null
        )
    }

    fun updatePincode(pincode: String) {
        if (pincode.all { it.isDigit() } && pincode.length <= 6) {
            _uiState.value = _uiState.value.copy(
                pincode = pincode,
                pincodeError = null
            )
        }
    }

    fun updateAddressType(type: String) {
        _uiState.value = _uiState.value.copy(addressType = type)
    }

    fun toggleIsDefault() {
        _uiState.value = _uiState.value.copy(
            isDefault = !_uiState.value.isDefault
        )
    }

    fun saveAddress() {
        val currentState = _uiState.value

        // Validate form
        val validationResult = validateForm(currentState)
        if (!validationResult.isValid) {
            _uiState.value = currentState.copy(
                nameError = validationResult.nameError,
                phoneError = validationResult.phoneError,
                addressLine1Error = validationResult.addressLine1Error,
                cityError = validationResult.cityError,
                stateError = validationResult.stateError,
                pincodeError = validationResult.pincodeError
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

            try {
                val address = Address(
                    addressId = UUID.randomUUID().toString(),
                    name = currentState.name.trim(),
                    phone = currentState.phone.trim(),
                    addressLine1 = currentState.addressLine1.trim(),
                    addressLine2 = currentState.addressLine2.trim(),
                    landmark = currentState.landmark.trim(),
                    city = currentState.city.trim(),
                    state = currentState.state.trim(),
                    pincode = currentState.pincode.trim(),
                    addressType = currentState.addressType,
                    isDefault = currentState.isDefault
                )

                orderRepository.addAddress(address)

                _uiState.value = currentState.copy(
                    isLoading = false,
                    isAddressAdded = true
                )

            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to save address"
                )
            }
        }
    }

    private fun validateForm(state: AddAddressUiState): ValidationResult {
        val nameError = when {
            state.name.isBlank() -> "Name is required"
            state.name.length < 2 -> "Name must be at least 2 characters"
            else -> null
        }

        val phoneError = when {
            state.phone.isBlank() -> "Phone number is required"
            state.phone.length != 10 -> "Phone number must be 10 digits"
            !state.phone.all { it.isDigit() } -> "Phone number must contain only digits"
            else -> null
        }

        val addressLine1Error = when {
            state.addressLine1.isBlank() -> "Address is required"
            state.addressLine1.length < 5 -> "Address must be at least 5 characters"
            else -> null
        }

        val cityError = when {
            state.city.isBlank() -> "City is required"
            state.city.length < 2 -> "City name must be at least 2 characters"
            else -> null
        }

        val stateError = when {
            state.state.isBlank() -> "State is required"
            state.state.length < 2 -> "State name must be at least 2 characters"
            else -> null
        }

        val pincodeError = when {
            state.pincode.isBlank() -> "Pincode is required"
            state.pincode.length != 6 -> "Pincode must be 6 digits"
            !state.pincode.all { it.isDigit() } -> "Pincode must contain only digits"
            else -> null
        }

        return ValidationResult(
            isValid = nameError == null && phoneError == null && addressLine1Error == null &&
                    cityError == null && stateError == null && pincodeError == null,
            nameError = nameError,
            phoneError = phoneError,
            addressLine1Error = addressLine1Error,
            cityError = cityError,
            stateError = stateError,
            pincodeError = pincodeError
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class AddAddressUiState(
    val name: String = "",
    val phone: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val landmark: String = "",
    val city: String = "",
    val state: String = "",
    val pincode: String = "",
    val addressType: String = "HOME",
    val isDefault: Boolean = false,
    val isLoading: Boolean = false,
    val isAddressAdded: Boolean = false,
    val errorMessage: String? = null,
    val nameError: String? = null,
    val phoneError: String? = null,
    val addressLine1Error: String? = null,
    val cityError: String? = null,
    val stateError: String? = null,
    val pincodeError: String? = null
)

data class ValidationResult(
    val isValid: Boolean,
    val nameError: String? = null,
    val phoneError: String? = null,
    val addressLine1Error: String? = null,
    val cityError: String? = null,
    val stateError: String? = null,
    val pincodeError: String? = null
)