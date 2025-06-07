package com.example.apnakirana.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Search : Screen("search")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object ProductCatalog : Screen("product_catalog/{categoryId}/{categoryName}") {
        fun createRoute(categoryId: String, categoryName: String) =
            "product_catalog/$categoryId/$categoryName"
    }
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    // Checkout Screens
    object Checkout : Screen("checkout")
    object OrderConfirmation : Screen("order_confirmation/{orderId}") {
        fun createRoute(orderId: String) = "order_confirmation/$orderId"
    }

    // ✅ NEW: Order History Screens
    object OrderHistory : Screen("order_history")
    object OrderDetails : Screen("order_details/{orderId}") {
        fun createRoute(orderId: String) = "order_details/$orderId"
    }

    // ✅ NEW: Address Management Screens
    object AddressManagement : Screen("address_management")
    object AddAddress : Screen("add_address")
}