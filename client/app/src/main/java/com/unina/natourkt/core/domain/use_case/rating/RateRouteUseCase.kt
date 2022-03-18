package com.unina.natourkt.core.domain.use_case.rating

import com.unina.natourkt.core.domain.model.RatingCreation
import com.unina.natourkt.core.domain.repository.RatingRepository
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RateRouteUseCase @Inject constructor(
    private val ratingRepository: RatingRepository,
) {

    operator fun invoke(rating: RatingCreation): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())

        val result = ratingRepository.createRating(rating)
        emit(result)
    }
}