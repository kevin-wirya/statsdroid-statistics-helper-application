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
            category = "Google Drive"
        ),
        ReferenceItem(
            title = "02. Probabilitas",
            pdfUrl = "https://drive.google.com/file/d/1T_egBZnhVf2OPo2x-HbFunZ8SbE_2wQU/view?usp=sharing",
            category = "Google Drive"
        ),
        ReferenceItem(
            title = "03. Var-Random",
            pdfUrl = "https://drive.google.com/file/d/1LUo0Mc48DUIyaMvVHTcHIEisb-6dTZkb/view?usp=sharing",
            category = "Google Drive"
        ),
        ReferenceItem(
            title = "04. Nilai-Ekspektasi",
            pdfUrl = "https://drive.google.com/file/d/12f-Kb1IE9Ubnpq7n7gfiftzKJ4MrKKPE/view?usp=sharing",
            category = "Google Drive"
        ),
        ReferenceItem(
            title = "05. Distribusi-Diskrit",
            pdfUrl = "https://drive.google.com/file/d/1qUH9lG-DxcdcmCWHusISnV0Jhf7--CBu/view?usp=sharing",
            category = "Google Drive"
        ),
        ReferenceItem(
            title = "06. Distribusi-Kontinu",
            pdfUrl = "https://drive.google.com/file/d/18Bu5ye2bdK5YUHRDQR5WSMP2O2yq5Rrm/view?usp=sharing",
            category = "Google Drive"
        ),
        ReferenceItem(
            title = "07. Distribusi-Sampel",
            pdfUrl = "https://drive.google.com/file/d/14wshJ9dcdFxj6ValQV9ZkJER0HOKB9OJ/view?usp=sharing",
            category = "Google Drive"
        ),
        ReferenceItem(
            title = "08. Sampling",
            pdfUrl = "https://drive.google.com/file/d/1DbvDFJbyz5tG8aPDxxB_nfK30KMk07hQ/view?usp=sharing",
            category = "Google Drive"
        ),
        ReferenceItem(
            title = "09. Penaksir",
            pdfUrl = "https://drive.google.com/file/d/11CPflqZweYpBE_bilEAlW5rh-z4KaO6G/view?usp=sharing",
            category = "Google Drive"
        ),
        ReferenceItem(
            title = "10. Tes Hipotesa",
            pdfUrl = "https://drive.google.com/file/d/16fjE5HJlKscBVRy4jeSbR522QYH7nOyn/view?usp=sharing",
            category = "Google Drive"
        ),
        ReferenceItem(
            title = "11. Simple Linear Regression & Correlation",
            pdfUrl = "https://drive.google.com/file/d/1TEgA6PWexb6GSzPHm-wRu3y-o3CcqPRq/view?usp=sharing",
            category = "Google Drive"
        )
    )

    private val rinaldiFallbackReferences = listOf(
        ReferenceItem(
            title = "01. Ruang Sampel",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Ruang%20Sampel.pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "02. Kejadian dan Peluang Kejadian",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Kejadian%20(Event).pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "03. Beberapa Hukum Peluang",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Beberapa%20Hukum%20Peluang.pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "04. Peubah Acak",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Peubah%20Acak.pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "05. Distribusi Peluang Kontinu",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Distribusi%20Peluang%20Kontinu.pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "06. Ekspektasi Matematik",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Ekspektasi%20Matematik.pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "07. Variansi dan Kovariansi",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Variansi%20dan%20Kovariansi.pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "08. Teorema Chebysev",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Teorema%20Chebysev.pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "09. Beberapa Distribusi Peluang Diskrit",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Beberapa%20Distribusi%20Peluang%20Diskrit.pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "10. Sampel Acak dan Distribusinya",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Sampel%20Acak%20dan%20Distribusinya.pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "11. Teori Penaksiran",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Teori%20Penaksiran.pdf",
            category = "Web Scraper"
        ),
        ReferenceItem(
            title = "12. Pengujian Hipotesis Statistik",
            pdfUrl = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/Pengujian%20Hipotesis.pdf",
            category = "Web Scraper"
        )
    )

    init {
        _uiState.update {
            it.copy(
                driveItems = driveReferences,
                scrapedItems = rinaldiFallbackReferences,
                items = driveReferences,
                selectedItem = driveReferences.firstOrNull()
            )
        }
        scrapePakRinaldiWebsite()
    }

    fun selectItem(item: ReferenceItem) {
        _uiState.update { it.copy(selectedItem = item) }
    }

    fun setSourceFilter(filter: String) {
        _uiState.update { state ->
            val filteredList = when (filter) {
                "Google Drive" -> state.driveItems
                "Web Scraper" -> if (state.scrapedItems.isNotEmpty()) state.scrapedItems else rinaldiFallbackReferences
                else -> state.driveItems + (if (state.scrapedItems.isNotEmpty()) state.scrapedItems else rinaldiFallbackReferences)
            }
            state.copy(
                selectedSourceFilter = filter,
                items = filteredList,
                selectedItem = filteredList.firstOrNull()
            )
        }
    }

    fun scrapePakRinaldiWebsite() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val url = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Probstat/2010-2011/probstat10-11.htm"
                val doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(15000)
                    .get()

                val scraped = mutableListOf<ReferenceItem>()
                val links = doc.select("a[href]")

                for (element in links) {
                    val href = element.attr("abs:href")
                    val text = element.text().trim()
                    if ((href.endsWith(".pdf", ignoreCase = true) || href.endsWith(".ppt", ignoreCase = true) || href.endsWith(".pptx", ignoreCase = true))
                        && text.isNotBlank() && !text.equals("ini", ignoreCase = true)
                    ) {
                        scraped.add(
                            ReferenceItem(
                                title = text,
                                pdfUrl = href,
                                category = "Web Scraper"
                            )
                        )
                    }
                }

                val finalScrapedList = if (scraped.isNotEmpty()) scraped else rinaldiFallbackReferences

                _uiState.update { state ->
                    val combined = state.driveItems + finalScrapedList
                    val activeList = when (state.selectedSourceFilter) {
                        "Google Drive" -> state.driveItems
                        "Web Scraper" -> finalScrapedList
                        else -> combined
                    }
                    state.copy(
                        scrapedItems = finalScrapedList,
                        items = activeList,
                        selectedItem = activeList.firstOrNull(),
                        isLoading = false,
                        isScrapedLive = scraped.isNotEmpty()
                    )
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    val activeList = when (state.selectedSourceFilter) {
                        "Google Drive" -> state.driveItems
                        "Web Scraper" -> rinaldiFallbackReferences
                        else -> state.driveItems + rinaldiFallbackReferences
                    }
                    state.copy(
                        scrapedItems = rinaldiFallbackReferences,
                        items = activeList,
                        selectedItem = activeList.firstOrNull(),
                        isLoading = false,
                        errorMessage = "Mode Offline: Menggunakan materi lokal Pak Rinaldi (${e.localizedMessage})"
                    )
                }
            }
        }
    }
}
