package com.waseem.foodapp.viewModel

import com.waseem.foodapp.models.FoodModels


sealed class ApiState{
    class Success(val data: FoodModels) : ApiState()
    class Failure(val msg:Throwable) : ApiState()
    object Loading : ApiState()
    object Empty : ApiState()
}