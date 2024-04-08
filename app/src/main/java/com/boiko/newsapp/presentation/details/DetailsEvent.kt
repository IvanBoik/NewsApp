package com.boiko.newsapp.presentation.details

import com.boiko.newsapp.domain.model.Article

sealed class DetailsEvent {
    data class UpsertDeleteArticle(val article: Article): DetailsEvent()

    data object RemoveSideEffect: DetailsEvent()
}