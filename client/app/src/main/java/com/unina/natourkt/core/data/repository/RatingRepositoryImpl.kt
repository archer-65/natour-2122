package com.unina.natourkt.core.data.repository

import com.unina.natourkt.core.data.remote.dto.mapper.RatingCreationApiMapper
import com.unina.natourkt.core.data.remote.retrofit.RatingApi
import com.unina.natourkt.core.data.util.retrofitSafeCall
import com.unina.natourkt.core.domain.model.RatingCreation
import com.unina.natourkt.core.domain.repository.RatingRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * This implementation of [RatingRepository] works with a [RatingApi] Retrofit interface
 * It also contains mapper for model conversions
 * @see [RatingCreationApiMapper]
 */
class RatingRepositoryImpl @Inject constructor(
    private val api: RatingApi,
    private val ratingCreationApiMapper: RatingCreationApiMapper,
) : RatingRepository {

    override suspend fun createRating(rating: RatingCreation): DataState<Unit> =
        retrofitSafeCall(Dispatchers.IO) {
            val ratingRequest = ratingCreationApiMapper.mapToDto(rating)
            api.createRating(ratingRequest)
        }
}