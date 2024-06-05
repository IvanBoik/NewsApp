package com.boiko.newsapp.presentation.onboarding

import androidx.annotation.DrawableRes
import com.boiko.newsapp.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)


val pages = listOf(
    Page(
        title = "Lightweight news viewing app",
        description = "Stay up to date with the latest news anywhere in the world.",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Save news and share it",
        description = "Save news and reread it at any time, share it in any messenger.",
        image = R.drawable.onboarding2
    ),
    Page(
        title = "Diversify your reading",
        description = "Listen to music while reading the news (helps you relax if the news is sad).",
        image = R.drawable.onboarding4
    )
)