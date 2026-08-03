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
class HypothesisViewModel @Inject constructor(
    private val repository:StatsRepository
):ViewModel(){
    private val _uiState=MutableStateFlow(HypothesisUiState())
    val uiState:StateFlow<HypothesisUiState>=_uiState.asStateFlow()
    fun calculateZTest() {
        val state=_uiState.value
        val sampleMean=state.sampleMeanInput.toDoubleOrNull()?: return
        val popMean=state.popMeanInput.toDoubleOrNull()?: return
        val popStdDev=state.popStdDevInput.toDoubleOrNull()?: return
        val n=state.nInput.toIntOrNull()?: return
        val alpha=state.alphaInput.toDoubleOrNull()?: return
        val res=repository.performZTestOneSample(
            sampleMean,popMean,popStdDev,n,alpha,state.isTwoTailed
        )
        _uiState.update{it.copy(result=res)}
    }
}
