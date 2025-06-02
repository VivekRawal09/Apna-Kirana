package com.example.apnakirana.presentation.onboarding.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apnakirana.data.model.OnboardingPage
import com.example.apnakirana.ui.theme.ApnaKiranaTheme

@Composable
fun OnboardingPageItem(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Image/Icon
        Text(
            text = page.image,
            fontSize = 120.sp,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Title
        Text(
            text = page.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Description
        Text(
            text = page.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPageItemPreview() {
    ApnaKiranaTheme {
        OnboardingPageItem(
            page = OnboardingPage(
                title = "Fresh Groceries",
                description = "Get fresh vegetables, fruits and daily essentials delivered to your doorstep",
                image = "🥬"
            )
        )
    }
}