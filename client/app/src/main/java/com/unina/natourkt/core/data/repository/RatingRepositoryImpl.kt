package com.unina.natourkt.core.data.repository

import com.unina.natourkt.core.data.remote.dto.mapper.RatingCreationApiMapper
import com.unina.natourkt.core.data.remote.retrofit.RatingApi
import com.unina.natourkt.core.data.util.safeApiCall
import com.unina.natourkt.core.domain.model.RatingCreation
import com.unina.natourkt.core.domain.repository.RatingRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(
    private val api: RatingApi,
    private val ratingCreationApiMapper: RatingCreationApiMapper,
) : RatingRepository {

    override suspend fun createRating(rating: RatingCreation): DataState<Unit> =
        safeApiCall(Dispatchers.IO) {
            val ratingRequest = ratingCreationApiMapper.mapToDto(rating)
            api.createRating(ratingRequest)
        }
}