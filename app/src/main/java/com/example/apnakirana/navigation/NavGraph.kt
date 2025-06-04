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

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
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
            // Placeholder for Search Screen - we'll implement this in Phase 2
            PlaceholderScreen(title = "Search Screen")
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onProductClick = { productId ->
                    navController.navigate(
                        Screen.ProductDetail.createRoute(productId)
                    )
                }
            )
        }

        composable(Screen.Profile.route) {
            // Placeholder for Profile Screen - we'll implement this in Phase 4
            PlaceholderScreen(title = "Profile Screen")
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