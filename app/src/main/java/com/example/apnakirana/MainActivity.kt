package com.example.apnakirana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.apnakirana.data.local.entity.Address
import com.example.apnakirana.domain.repository.OrderRepository
import com.example.apnakirana.navigation.BottomNavigationBar
import com.example.apnakirana.navigation.NavGraph
import com.example.apnakirana.navigation.Screen
import com.example.apnakirana.presentation.cart.CartViewModel
import com.example.apnakirana.ui.theme.ApnaKiranaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var orderRepository: OrderRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize sample addresses
        lifecycleScope.launch {
            initializeSampleAddresses()
        }

        setContent {
            ApnaKiranaTheme {
                MainContent()
            }
        }
    }

    private suspend fun initializeSampleAddresses() {
        try {
            // Check if addresses already exist
            orderRepository.getAddresses().collect { addresses ->
                if (addresses.isEmpty()) {
                    // Add sample addresses
                    val sampleAddresses = listOf(
                        Address(
                            addressId = UUID.randomUUID().toString(),
                            name = "John Doe",
                            phone = "+91 9876543210",
                            addressLine1 = "123, Green Avenue",
                            addressLine2 = "Near City Mall",
                            landmark = "Opposite Metro Station",
                            city = "Mumbai",
                            state = "Maharashtra",
                            pincode = "400001",
                            addressType = "HOME",
                            isDefault = true
                        ),
                        Address(
                            addressId = UUID.randomUUID().toString(),
                            name = "John Doe",
                            phone = "+91 9876543210",
                            addressLine1 = "456, Tech Park, Block A",
                            addressLine2 = "4th Floor",
                            landmark = "Near Food Court",
                            city = "Mumbai",
                            state = "Maharashtra",
                            pincode = "400002",
                            addressType = "OFFICE",
                            isDefault = false
                        ),
                        Address(
                            addressId = UUID.randomUUID().toString(),
                            name = "Jane Smith",
                            phone = "+91 8765432109",
                            addressLine1 = "789, Rose Gardens",
                            addressLine2 = "Villa No. 15",
                            landmark = "Behind Community Center",
                            city = "Mumbai",
                            state = "Maharashtra",
                            pincode = "400003",
                            addressType = "OTHER",
                            isDefault = false
                        )
                    )

                    sampleAddresses.forEach { address ->
                        orderRepository.addAddress(address)
                    }
                }
                return@collect // Exit the collect loop after first emission
            }
        } catch (e: Exception) {
            println("Error initializing sample addresses: ${e.message}")
        }
    }
}

// Fix 2: Update your MainActivity.kt
// Replace the MainContent composable with this:

@Composable
private fun MainContent(
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Get cart count from CartViewModel
    val cartUiState by cartViewModel.uiState.collectAsState()

    // Screens where we don't want to show bottom navigation
    val screensWithoutBottomNav = listOf(
        Screen.Splash.route,
        Screen.Onboarding.route,
        Screen.Checkout.route,
        Screen.OrderConfirmation.route
    )

    val showBottomBar = currentRoute !in screensWithoutBottomNav

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    cartItemCount = cartUiState.uniqueItemsCount // ✅ Use unique items count instead
                )
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        )
    }
}