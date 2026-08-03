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
class LookupViewModel @Inject constructor(
    private val repository: StatsRepository
):ViewModel(){
    private val _uiState=MutableStateFlow(LookupUiState())
    val uiState:StateFlow<LookupUiState>=
        _uiState.asStateFlow()
    fun calculate(){
        val state=_uiState.value
        when(state.selectedDistribution){
            DistributionType.BINOMIAL->{
                val n=state.nInput.toIntOrNull()?: return
                val p=state.pInput.toDoubleOrNull()?: return
                val k=state.kBinomialInput.toIntOrNull()?: return
                _uiState.update {
                    it.copy(
                        resultPmf=repository.calculateBinomialPmf(n,p,k),
                        resultCdfLower=repository.calculateBinomialCdfLower(n,p,k),
                        resultCdfUpper=repository.calculateBinomialCdfUpper(n,p,k)
                    )
                }
            }
            DistributionType.POISSON->{
                val lambda=state.lambdaInput.toDoubleOrNull()?: return
                val k=state.kPoissonInput.toIntOrNull()?: return
                _uiState.update {
                    it.copy(
                        resultPmf=repository.calculatePoissonPmf(lambda,k),
                        resultCdfLower=repository.calculatePoissonCdfLower(lambda,k),
                        resultCdfUpper=repository.calculatePoissonCdfUpper(lambda,k)
                    )
                }
            }
            DistributionType.NORMAL->{
                val z=state.zInput.toDoubleOrNull()?: return
                _uiState.update {
                    it.copy(
                        resultCdfLower=repository.calculateNormalCdf(z)
                    )
                }
            }
        }
    }
}
