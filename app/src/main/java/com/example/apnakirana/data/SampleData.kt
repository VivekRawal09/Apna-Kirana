package com.example.apnakirana.data

import com.example.apnakirana.data.local.entity.Category
import com.example.apnakirana.data.local.entity.Product

object SampleData {

    // Sample Categories
    val categories = listOf(
        Category(
            id = "vegetables",
            name = "Fresh Vegetables",
            icon = "🥬",
            isActive = true
        ),
        Category(
            id = "fruits",
            name = "Fresh Fruits",
            icon = "🍎",
            isActive = true
        ),
        Category(
            id = "dairy",
            name = "Dairy & Eggs",
            icon = "🥛",
            isActive = true
        ),
        Category(
            id = "spices",
            name = "Spices & Masalas",
            icon = "🌶️",
            isActive = true
        ),
        Category(
            id = "grains",
            name = "Rice & Grains",
            icon = "🌾",
            isActive = true
        ),
        Category(
            id = "pulses",
            name = "Dal & Pulses",
            icon = "🫘",
            isActive = true
        ),
        Category(
            id = "snacks",
            name = "Snacks & Namkeen",
            icon = "🍿",
            isActive = true
        ),
        Category(
            id = "beverages",
            name = "Tea & Beverages",
            icon = "☕",
            isActive = true
        )
    )

    // Sample Products
    val products = listOf(
        // Fresh Vegetables
        Product(
            id = "onion_1kg",
            name = "Fresh Onions",
            description = "Premium quality red onions, perfect for daily cooking",
            price = 35.0,
            originalPrice = 40.0,
            imageUrl = "https://images.unsplash.com/photo-1508747703725-719777637510?w=400",
            category = "vegetables",
            unit = "1 kg",
            isInStock = true,
            rating = 4.2f,
            discount = 12
        ),
        Product(
            id = "tomato_1kg",
            name = "Fresh Tomatoes",
            description = "Ripe and juicy tomatoes, handpicked for freshness",
            price = 45.0,
            originalPrice = 50.0,
            imageUrl = "https://images.unsplash.com/photo-1546470427-e2618b4a9a63?w=400",
            category = "vegetables",
            unit = "1 kg",
            isInStock = true,
            rating = 4.5f,
            discount = 10
        ),
        Product(
            id = "potato_1kg",
            name = "Fresh Potatoes",
            description = "Farm fresh potatoes, ideal for all your cooking needs",
            price = 28.0,
            imageUrl = "https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=400",
            category = "vegetables",
            unit = "1 kg",
            isInStock = true,
            rating = 4.3f
        ),
        Product(
            id = "green_chili_250g",
            name = "Green Chillies",
            description = "Fresh green chillies to add that perfect spice",
            price = 25.0,
            imageUrl = "https://images.unsplash.com/photo-1583328475180-88a71f92ebc6?w=400",
            category = "vegetables",
            unit = "250g",
            isInStock = true,
            rating = 4.1f
        ),
        Product(
            id = "coriander_100g",
            name = "Fresh Coriander",
            description = "Aromatic fresh coriander leaves for garnishing",
            price = 15.0,
            imageUrl = "https://images.unsplash.com/photo-1615485290628-0e65174c8f0b?w=400",
            category = "vegetables",
            unit = "100g",
            isInStock = true,
            rating = 4.4f
        ),

        // Fresh Fruits
        Product(
            id = "banana_1dozen",
            name = "Fresh Bananas",
            description = "Sweet and ripe bananas, perfect for snacking",
            price = 48.0,
            originalPrice = 55.0,
            imageUrl = "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=400",
            category = "fruits",
            unit = "1 dozen",
            isInStock = true,
            rating = 4.6f,
            discount = 13
        ),
        Product(
            id = "apple_1kg",
            name = "Red Apples",
            description = "Crispy and sweet red apples from Kashmir",
            price = 180.0,
            originalPrice = 200.0,
            imageUrl = "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=400",
            category = "fruits",
            unit = "1 kg",
            isInStock = true,
            rating = 4.7f,
            discount = 10
        ),
        Product(
            id = "mango_1kg",
            name = "Alphonso Mangoes",
            description = "King of mangoes - sweet and pulpy Alphonso",
            price = 320.0,
            imageUrl = "https://images.unsplash.com/photo-1553279768-865429fa0078?w=400",
            category = "fruits",
            unit = "1 kg",
            isInStock = true,
            rating = 4.9f
        ),
        Product(
            id = "orange_1kg",
            name = "Sweet Oranges",
            description = "Juicy Nagpur oranges packed with Vitamin C",
            price = 85.0,
            imageUrl = "https://images.unsplash.com/photo-1547514701-42782101795e?w=400",
            category = "fruits",
            unit = "1 kg",
            isInStock = true,
            rating = 4.4f
        ),

        // Dairy & Eggs
        Product(
            id = "milk_1l",
            name = "Fresh Milk",
            description = "Farm fresh full cream milk",
            price = 65.0,
            imageUrl = "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=400",
            category = "dairy",
            unit = "1 liter",
            isInStock = true,
            rating = 4.5f
        ),
        Product(
            id = "eggs_12pc",
            name = "Farm Fresh Eggs",
            description = "Brown eggs from free-range chickens",
            price = 84.0,
            originalPrice = 90.0,
            imageUrl = "https://images.unsplash.com/photo-1518569656558-1f25e69d93d7?w=400",
            category = "dairy",
            unit = "12 pieces",
            isInStock = true,
            rating = 4.6f,
            discount = 7
        ),
        Product(
            id = "paneer_250g",
            name = "Fresh Paneer",
            description = "Soft and fresh cottage cheese",
            price = 125.0,
            imageUrl = "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=400",
            category = "dairy",
            unit = "250g",
            isInStock = true,
            rating = 4.3f
        ),
        Product(
            id = "yogurt_500g",
            name = "Fresh Curd",
            description = "Thick and creamy homestyle curd",
            price = 45.0,
            imageUrl = "https://images.unsplash.com/photo-1571212515416-576a6e016376?w=400",
            category = "dairy",
            unit = "500g",
            isInStock = true,
            rating = 4.4f
        ),

        // Spices & Masalas
        Product(
            id = "turmeric_100g",
            name = "Turmeric Powder",
            description = "Pure and aromatic turmeric powder",
            price = 35.0,
            imageUrl = "https://images.unsplash.com/photo-1615485500704-8e990f9900f7?w=400",
            category = "spices",
            unit = "100g",
            isInStock = true,
            rating = 4.7f
        ),
        Product(
            id = "red_chili_100g",
            name = "Red Chilli Powder",
            description = "Hot and spicy red chilli powder",
            price = 48.0,
            imageUrl = "https://images.unsplash.com/photo-1599907475913-f488c7c8c1c6?w=400",
            category = "spices",
            unit = "100g",
            isInStock = true,
            rating = 4.5f
        ),
        Product(
            id = "garam_masala_50g",
            name = "Garam Masala",
            description = "Authentic blend of aromatic spices",
            price = 65.0,
            originalPrice = 70.0,
            imageUrl = "https://images.unsplash.com/photo-1596040798888-c8ad6b495c72?w=400",
            category = "spices",
            unit = "50g",
            isInStock = true,
            rating = 4.8f,
            discount = 7
        ),

        // Rice & Grains
        Product(
            id = "basmati_rice_1kg",
            name = "Basmati Rice",
            description = "Premium aged basmati rice with long grains",
            price = 145.0,
            originalPrice = 160.0,
            imageUrl = "https://images.unsplash.com/photo-1536304993881-ff6e9eefa2a6?w=400",
            category = "grains",
            unit = "1 kg",
            isInStock = true,
            rating = 4.6f,
            discount = 9
        ),
        Product(
            id = "wheat_flour_1kg",
            name = "Wheat Flour",
            description = "Fresh ground whole wheat flour (Atta)",
            price = 55.0,
            imageUrl = "https://images.unsplash.com/photo-1595941438235-6541e7ad7b93?w=400",
            category = "grains",
            unit = "1 kg",
            isInStock = true,
            rating = 4.4f
        ),

        // Dal & Pulses
        Product(
            id = "toor_dal_1kg",
            name = "Toor Dal",
            description = "Premium quality yellow pigeon peas",
            price = 130.0,
            imageUrl = "https://images.unsplash.com/photo-1599037834134-2b793ba06b8e?w=400",
            category = "pulses",
            unit = "1 kg",
            isInStock = true,
            rating = 4.5f
        ),
        Product(
            id = "moong_dal_500g",
            name = "Moong Dal",
            description = "Split green gram, perfect for khichdi",
            price = 85.0,
            imageUrl = "https://images.unsplash.com/photo-1599037834134-2b793ba06b8e?w=400",
            category = "pulses",
            unit = "500g",
            isInStock = true,
            rating = 4.3f
        ),

        // Snacks & Namkeen
        Product(
            id = "mixture_200g",
            name = "Bombay Mixture",
            description = "Crunchy and spicy traditional namkeen",
            price = 65.0,
            imageUrl = "https://images.unsplash.com/photo-1606491956689-2ea866880c84?w=400",
            category = "snacks",
            unit = "200g",
            isInStock = true,
            rating = 4.2f
        ),
        Product(
            id = "bhujia_150g",
            name = "Aloo Bhujia",
            description = "Famous Haldiram's crispy potato bhujia",
            price = 45.0,
            originalPrice = 50.0,
            imageUrl = "https://images.unsplash.com/photo-1606491956689-2ea866880c84?w=400",
            category = "snacks",
            unit = "150g",
            isInStock = true,
            rating = 4.7f,
            discount = 10
        ),

        // Tea & Beverages
        Product(
            id = "tea_250g",
            name = "Assam Tea",
            description = "Strong and flavorful Assam black tea",
            price = 185.0,
            imageUrl = "https://images.unsplash.com/photo-1597318181409-d0d457680a4e?w=400",
            category = "beverages",
            unit = "250g",
            isInStock = true,
            rating = 4.6f
        ),
        Product(
            id = "coffee_100g",
            name = "Filter Coffee",
            description = "South Indian style filter coffee powder",
            price = 125.0,
            imageUrl = "https://images.unsplash.com/photo-1511920170033-f8396924c348?w=400",
            category = "beverages",
            unit = "100g",
            isInStock = true,
            rating = 4.5f
        )
    )

    // Helper function to get products by category
    fun getProductsByCategory(categoryId: String): List<Product> {
        return products.filter { it.category == categoryId }
    }

    // Helper function to get featured products (with discounts)
    fun getFeaturedProducts(): List<Product> {
        return products.filter { it.discount > 0 }.take(6)
    }

    // Helper function to search products
    fun searchProducts(query: String): List<Product> {
        val searchTerm = query.lowercase()
        return products.filter {
            it.name.lowercase().contains(searchTerm) ||
                    it.description.lowercase().contains(searchTerm)
        }
    }
}