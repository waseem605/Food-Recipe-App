package com.waseem.foodapp.viewModel

import com.waseem.foodapp.models.FoodModels
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //    @GET("charging_animation")
    @GET("/quotes")
    suspend fun getAnimationListNew(@Query("page") page: String): FoodModels
//    suspend fun getAnimationListNew(@Query("page") page: String): FoodModels

}