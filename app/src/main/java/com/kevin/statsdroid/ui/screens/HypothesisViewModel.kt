package com.kevin.statsdroid.ui.screens

import androidx.lifecycle.ViewModel
import com.kevin.statsdroid.domain.repository.StatsRepository
import com.kevin.statsdroid.utils.calculator.HypothesisTestType
import com.kevin.statsdroid.utils.calculator.TailType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HypothesisViewModel @Inject constructor(
    private val repository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HypothesisUiState())
    val uiState: StateFlow<HypothesisUiState> = _uiState.asStateFlow()

    init {
        calculate()
    }

    fun onTestTypeChanged(testType: HypothesisTestType) {
        _uiState.update { it.copy(testType = testType) }
        calculate()
    }

    fun onSampleMeanChanged(input: String) {
        _uiState.update { it.copy(sampleMeanInput = input) }
        calculate()
    }

    fun onPopMeanChanged(input: String) {
        _uiState.update { it.copy(popMeanInput = input) }
        calculate()
    }

    fun onPopStdDevChanged(input: String) {
        _uiState.update { it.copy(popStdDevInput = input) }
        calculate()
    }

    fun onSampleStdDevChanged(input: String) {
        _uiState.update { it.copy(sampleStdDevInput = input) }
        calculate()
    }

    fun onNChanged(input: String) {
        _uiState.update { it.copy(nInput = input) }
        calculate()
    }

    fun onAlphaChanged(input: String) {
        _uiState.update { it.copy(alphaInput = input) }
        calculate()
    }

    fun onTailTypeChanged(tailType: TailType) {
        _uiState.update { it.copy(tailType = tailType) }
        calculate()
    }

    fun calculate() {
        val state = _uiState.value
        val sampleMean = state.sampleMeanInput.toDoubleOrNull() ?: return
        val popMean = state.popMeanInput.toDoubleOrNull() ?: return
        val n = state.nInput.toIntOrNull() ?: return
        val alpha = state.alphaInput.toDoubleOrNull() ?: return

        val result = if (state.testType == HypothesisTestType.Z_TEST) {
            val popStdDev = state.popStdDevInput.toDoubleOrNull() ?: return
            repository.performZTestOneSample(
                sampleMean, popMean, popStdDev, n, alpha, state.tailType
            )
        } else {
            val sampleStdDev = state.sampleStdDevInput.toDoubleOrNull() ?: return
            repository.performTTestOneSample(
                sampleMean, popMean, sampleStdDev, n, alpha, state.tailType
            )
        }

        _uiState.update { it.copy(result = result) }
    }
}
