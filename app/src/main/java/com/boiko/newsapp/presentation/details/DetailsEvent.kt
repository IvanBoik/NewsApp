package com.boiko.newsapp.presentation.details

sealed class DetailsEvent {
    data object SaveArticle: DetailsEvent()
}