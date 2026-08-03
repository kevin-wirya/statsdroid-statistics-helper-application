package com.kevin.statsdroid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class ReferenceViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ReferenceUiState())
    val uiState: StateFlow<ReferenceUiState> = _uiState.asStateFlow()
    private val driveReferences = listOf(
        ReferenceItem(
            title = "01. Introduction to Statistics",
            pdfUrl = "https://drive.google.com/file/d/1FM9H9DBki853CYyG7RNPfHNclaRyllx7/view?usp=drive_link",
            category = "Google Drive PPT"
        ),
        ReferenceItem(
            title = "02. Probabilitas",
            pdfUrl = "https://drive.google.com/file/d/1T_egBZnhVf2OPo2x-HbFunZ8SbE_2wQU/view?usp=sharing",
            category = "Google Drive PPT"
        ),
        ReferenceItem(
            title = "03. Var-Random",
            pdfUrl = "https://drive.google.com/file/d/1LUo0Mc48DUIyaMvVHTcHIEisb-6dTZkb/view?usp=sharing",
            category = "Google Drive PPT"
        ),
        ReferenceItem(
            title = "04. Nilai-Ekspektasi",
            pdfUrl = "https://drive.google.com/file/d/12f-Kb1IE9Ubnpq7n7gfiftzKJ4MrKKPE/view?usp=sharing",
            category = "Google Drive PPT"
        ),
        ReferenceItem(
            title = "05. Distribusi-Diskrit",
            pdfUrl = "https://drive.google.com/file/d/1qUH9lG-DxcdcmCWHusISnV0Jhf7--CBu/view?usp=sharing",
            category = "Google Drive PPT"
        ),
        ReferenceItem(
            title = "06. Distribusi-Kontinu",
            pdfUrl = "https://drive.google.com/file/d/18Bu5ye2bdK5YUHRDQR5WSMP2O2yq5Rrm/view?usp=sharing",
            category = "Google Drive PPT"
        ),
        ReferenceItem(
            title = "07. Distribusi-Sampel",
            pdfUrl = "https://drive.google.com/file/d/14wshJ9dcdFxj6ValQV9ZkJER0HOKB9OJ/view?usp=sharing",
            category = "Google Drive PPT"
        ),
        ReferenceItem(
            title = "08. Sampling",
            pdfUrl = "https://drive.google.com/file/d/1DbvDFJbyz5tG8aPDxxB_nfK30KMk07hQ/view?usp=sharing",
            category = "Google Drive PPT"
        ),
        ReferenceItem(
            title = "09. Penaksir",
            pdfUrl = "https://drive.google.com/file/d/11CPflqZweYpBE_bilEAlW5rh-z4KaO6G/view?usp=sharing",
            category = "Google Drive PPT"
        ),
        ReferenceItem(
            title = "10. Tes Hipotesa",
            pdfUrl = "https://drive.google.com/file/d/16fjE5HJlKscBVRy4jeSbR522QYH7nOyn/view?usp=sharing",
            category = "Google Drive PPT"
        ),
        ReferenceItem(
            title = "11. Simple Linear Regression & Correlation",
            pdfUrl = "https://drive.google.com/file/d/1TEgA6PWexb6GSzPHm-wRu3y-o3CcqPRq/view?usp=sharing",
            category = "Google Drive PPT"
        )
    )
    init {
        _uiState.update {
            it.copy(
                items = driveReferences,
                selectedItem = driveReferences.firstOrNull()
            )
        }
        scrapePakRinaldiWebsite()
    }
    fun selectItem(item: ReferenceItem) {
        _uiState.update { it.copy(selectedItem = item) }
    }
    fun scrapePakRinaldiWebsite() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val doc = Jsoup.connect("https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/probstat.htm")
                    .timeout(10000)
                    .get()
                val scrapedItems = mutableListOf<ReferenceItem>()
                val links = doc.select("a[href]")
                for (element in links) {
                    val href = element.attr("abs:href")
                    val text = element.text().trim()
                    if ((href.endsWith(".pdf", ignoreCase = true) || href.endsWith(".ppt", ignoreCase = true) || href.endsWith(".pptx", ignoreCase = true))
                        && text.isNotBlank()
                    ) {
                        scrapedItems.add(
                            ReferenceItem(
                                title = "[Web] $text",
                                pdfUrl = href,
                                category = "Scraped Pak Rinaldi"
                            )
                        )
                    }
                }
                val combined = driveReferences + scrapedItems
                _uiState.update {
                    it.copy(
                        items = if (scrapedItems.isNotEmpty()) combined else driveReferences,
                        isLoading = false,
                        isScrapedLive = scrapedItems.isNotEmpty()
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Mode Google Drive lokal aktif (${e.localizedMessage})"
                    )
                }
            }
        }
    }
}
