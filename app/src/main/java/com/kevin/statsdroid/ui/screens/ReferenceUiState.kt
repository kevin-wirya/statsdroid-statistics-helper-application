package com.kevin.statsdroid.ui.screens

data class ReferenceItem(
    val title: String,
    val pdfUrl: String,
    val category: String = "Probstat"
)
data class ReferenceUiState(
    val items: List<ReferenceItem> = emptyList(),
    val selectedItem: ReferenceItem? = null,
    val isLoading: Boolean = false,
    val isScrapedLive: Boolean = false,
    val errorMessage: String? = null
)
