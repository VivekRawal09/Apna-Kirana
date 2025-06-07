package com.example.apnakirana.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.apnakirana.data.local.entity.Product

// Color Palette
object AppColors {
    val Primary = Color(0xFF4CAF50)
    val PrimaryDark = Color(0xFF388E3C)
    val Secondary = Color(0xFFFFC107)
    val Error = Color(0xFFF44336)
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFF9800)
    val Background = Color(0xFFF5F5F5)
    val Surface = Color.White
    val OnSurface = Color(0xFF212121)
    val TextSecondary = Color(0xFF757575)
    val Divider = Color(0xFFE0E0E0)
}

// Search Bar Component
@Composable
fun SearchBar(
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    placeholder: String = "Search for groceries...",
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = enabled) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSearch()
                }
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = AppColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = if (query.isEmpty()) placeholder else query,
                color = if (query.isEmpty()) AppColors.TextSecondary else AppColors.OnSurface,
                fontSize = 16.sp
            )
        }
    }
}

// Enhanced Product Card with Animations
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProductCard(
    product: Product,
    isInCart: Boolean = false,
    cartQuantity: Int = 0,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    onUpdateQuantity: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )

    Card(
        modifier = modifier
            .width(180.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column {
            // Product Image with Badges
            Box {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                    error = painterResource(android.R.drawable.ic_menu_gallery)
                )

                // Discount Badge
                if (product.discount > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        color = AppColors.Error,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${product.discount}% OFF",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Favorite Button
                IconButton(
                    onClick = { /* TODO: Add to favorites */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(
                            Color.White.copy(alpha = 0.9f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        contentDescription = "Add to favorites",
                        tint = AppColors.Primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Product Details
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Text(
                    text = product.unit,
                    fontSize = 12.sp,
                    color = AppColors.TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Price Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "₹${product.price.toInt()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.Primary
                        )
                        product.originalPrice?.let {
                            Text(
                                text = "₹${it.toInt()}",
                                fontSize = 12.sp,
                                color = AppColors.TextSecondary,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }

                    // Add to Cart / Quantity Selector
                    AnimatedContent(
                        targetState = isInCart,
                        transitionSpec = {
                            fadeIn() + slideInHorizontally() with
                                    fadeOut() + slideOutHorizontally()
                        }
                    ) { inCart ->
                        if (inCart) {
                            QuantitySelector(
                                quantity = cartQuantity,
                                onQuantityChange = onUpdateQuantity,
                                compact = true
                            )
                        } else {
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onAddToCart()
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.Primary
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(
                                    text = "ADD",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Quantity Selector Component
@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = modifier
            .background(
                AppColors.Primary.copy(alpha = 0.1f),
                RoundedCornerShape(8.dp)
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                if (quantity > 0) onQuantityChange(quantity - 1)
            },
            modifier = Modifier.size(if (compact) 28.dp else 36.dp)
        ) {
            Icon(
                Icons.Default.Remove,
                contentDescription = "Decrease",
                tint = AppColors.Primary,
                modifier = Modifier.size(if (compact) 16.dp else 20.dp)
            )
        }

        Text(
            text = quantity.toString(),
            fontSize = if (compact) 14.sp else 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = if (compact) 8.dp else 12.dp)
        )

        IconButton(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onQuantityChange(quantity + 1)
            },
            modifier = Modifier.size(if (compact) 28.dp else 36.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Increase",
                tint = AppColors.Primary,
                modifier = Modifier.size(if (compact) 16.dp else 20.dp)
            )
        }
    }
}

// Section Header Component
@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.OnSurface
        )

        actionText?.let {
            TextButton(
                onClick = { onActionClick?.invoke() }
            ) {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = AppColors.Primary
                )
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(16.dp),
                    tint = AppColors.Primary
                )
            }
        }
    }
}

// Loading Skeleton Component
@Composable
fun LoadingSkeleton(
    modifier: Modifier = Modifier,
    height: Int = 200
) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.1f),
        Color.LightGray.copy(alpha = 0.3f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray.copy(alpha = 0.1f))
    )
}

// Empty State Component
@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    description: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = AppColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = AppColors.OnSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            fontSize = 14.sp,
            color = AppColors.TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        actionText?.let {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onAction?.invoke() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Primary
                )
            ) {
                Text(text = it)
            }
        }
    }
}