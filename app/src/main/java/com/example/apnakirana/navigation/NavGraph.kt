package com.example.apnakirana.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.apnakirana.presentation.onboarding.OnboardingScreen
import com.example.apnakirana.presentation.splash.SplashScreen
import com.example.apnakirana.presentation.home.HomeScreen
import com.example.apnakirana.presentation.catalog.ProductCatalogScreen
import com.example.apnakirana.presentation.detail.ProductDetailScreen
import com.example.apnakirana.presentation.cart.CartScreen
import com.example.apnakirana.presentation.search.SearchScreen
import com.example.apnakirana.presentation.checkout.CheckoutScreen
import com.example.apnakirana.presentation.checkout.OrderConfirmationScreen
import com.example.apnakirana.presentation.orders.OrderHistoryScreen
import com.example.apnakirana.presentation.orders.OrderDetailsScreen
import com.example.apnakirana.presentation.profile.ProfileScreen
import com.example.apnakirana.presentation.address.AddressManagementScreen
import com.example.apnakirana.presentation.address.AddAddressScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onCategoryClick = { category ->
                    navController.navigate(
                        Screen.ProductCatalog.createRoute(category.id, category.name)
                    )
                },
                onProductClick = { product ->
                    navController.navigate(
                        Screen.ProductDetail.createRoute(product.id)
                    )
                },
                onAddToCartClick = { product ->
                    // Cart functionality handled by HomeViewModel
                }
            )
        }

        composable(
            route = Screen.ProductCatalog.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) {
            ProductCatalogScreen(
                onBackPress = {
                    navController.popBackStack()
                },
                onProductClick = { product ->
                    navController.navigate(
                        Screen.ProductDetail.createRoute(product.id)
                    )
                }
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) {
            ProductDetailScreen(
                onBackPress = {
                    navController.popBackStack()
                },
                onRelatedProductClick = { product ->
                    navController.navigate(
                        Screen.ProductDetail.createRoute(product.id)
                    )
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onProductClick = { product ->
                    navController.navigate(
                        Screen.ProductDetail.createRoute(product.id)
                    )
                }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onProductClick = { productId ->
                    navController.navigate(
                        Screen.ProductDetail.createRoute(productId)
                    )
                },
                onProceedToCheckout = {
                    navController.navigate(Screen.Checkout.route)
                }
            )
        }

        // Checkout Screens
        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onOrderPlaced = { orderId ->
                    navController.navigate(
                        Screen.OrderConfirmation.createRoute(orderId)
                    ) {
                        popUpTo(Screen.Home.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.OrderConfirmation.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderConfirmationScreen(
                orderId = orderId,
                onContinueShopping = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                },
                onTrackOrder = {
                    navController.navigate(
                        Screen.OrderDetails.createRoute(orderId)
                    )
                }
            )
        }

        // ✅ NEW: Order History Screens
        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onOrderClick = { orderId ->
                    navController.navigate(
                        Screen.OrderDetails.createRoute(orderId)
                    )
                }
            )
        }

        composable(
            route = Screen.OrderDetails.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderDetailsScreen(
                orderId = orderId,
                onBackPressed = {
                    navController.popBackStack()
                },
                onReorderClick = { orderItems ->
                    // TODO: Add items to cart and navigate to cart
                    // For now, just navigate to home
                    navController.navigate(Screen.Home.route)
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onOrderHistoryClick = {
                    navController.navigate(Screen.OrderHistory.route)
                },
                onAddressManagementClick = {
                    navController.navigate(Screen.AddressManagement.route)
                }
            )
        }

        // ✅ NEW: Address Management Screens
        composable(Screen.AddressManagement.route) {
            AddressManagementScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onAddAddressClick = {
                    navController.navigate(Screen.AddAddress.route)
                }
            )
        }

        composable(Screen.AddAddress.route) {
            AddAddressScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onAddressAdded = {
                    navController.popBackStack()
                }
            )
        }
    }
}

// Temporary placeholder for screens we haven't built yet
@Composable
fun PlaceholderScreen(title: String) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = title,
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
    }
}