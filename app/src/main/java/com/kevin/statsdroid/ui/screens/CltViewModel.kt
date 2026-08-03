package com.kevin.statsdroid.ui.screens

import androidx.lifecycle.ViewModel
import com.kevin.statsdroid.domain.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CltViewModel @Inject constructor(
    private val repository:StatsRepository
):ViewModel(){
    private val _uiState=MutableStateFlow(CltUiState())
    val uiState:StateFlow<CltUiState>=_uiState.asStateFlow()
    fun generateSimulation(){
        val state=_uiState.value
        val sampleSize=state.sampleSizeInput.toIntOrNull()?: return
        val numSamples=state.numSamplesInput.toIntOrNull()?: return
        val means=repository.generateCltSampleMeans(
            state.selectedDistribution,sampleSize,numSamples
        )
        _uiState.update{it.copy(sampleMeans=means)}
    }
}
