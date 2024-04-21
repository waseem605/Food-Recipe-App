package com.waseem.foodapp.viewModel

import com.waseem.foodapp.models.FoodModels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AnimRepository(private val apiService: ApiService) {

    suspend fun getAnimations(page: String): Flow<FoodModels> {
        return flow {
            println("HomeFragment55   123  getComment repo ${page}")

            val comment = apiService.getAnimationListNew(page)
            // Emit this data wrapped in
            // the helper class [CommentApiState]
            emit(comment)
        }.flowOn(Dispatchers.IO)
    }
}