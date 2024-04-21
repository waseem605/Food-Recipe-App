package com.waseem.foodapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseem.foodapp.viewModel.AnimRepository
import com.waseem.foodapp.viewModel.ApiState
import com.waseem.foodapp.viewModel.AppConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AnimViewModel : ViewModel() {
    private val repository = AnimRepository(
        AppConfig().apiService()
    )
    val animationState: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)


    fun getNewComment(page: String) {
        animationState.value = ApiState.Loading
        viewModelScope.launch {
            repository.getAnimations(page)
                .catch {
                    animationState.value =
                        ApiState.Failure(it)
                }
                .collect { model ->
                    animationState.value = ApiState.Success(model)
                }
        }
    }
}