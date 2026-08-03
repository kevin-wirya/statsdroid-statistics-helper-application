package com.kevin.statsdroid.ui.screens

data class ReferenceItem(
    val title: String,
    val pdfUrl: String,
    val category: String = "Google Drive"
)

data class ReferenceUiState(
    val items: List<ReferenceItem> = emptyList(),
    val driveItems: List<ReferenceItem> = emptyList(),
    val scrapedItems: List<ReferenceItem> = emptyList(),
    val selectedItem: ReferenceItem? = null,
    val selectedSourceFilter: String = "Semua", // "Semua", "Google Drive", "Web Scraper"
    val isLoading: Boolean = false,
    val isScrapedLive: Boolean = false,
    val errorMessage: String? = null
)
