package com.example.apnakirana

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.apnakirana.navigation.BottomNavigationBar
import com.example.apnakirana.navigation.NavGraph
import com.example.apnakirana.navigation.Screen
import com.example.apnakirana.presentation.cart.CartViewModel
import com.example.apnakirana.ui.theme.ApnaKiranaTheme
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ApnaKiranaTheme {
                MainContent()
            }
        }
    }
}

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
        Screen.Onboarding.route
    )

    val showBottomBar = currentRoute !in screensWithoutBottomNav

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    cartItemCount = cartUiState.totalQuantity // ✅ Now shows actual cart count
                )
            }
        }
    ) { innerPadding ->
        Text(text = "",Modifier.padding(innerPadding))
        NavGraph(
            navController = navController,
            startDestination = Screen.Splash.route,
        )
    }
}