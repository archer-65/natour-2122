package com.unina.natourkt.core.domain.use_case.rating

import android.util.Log
import com.unina.natourkt.core.domain.model.RatingCreation
import com.unina.natourkt.core.domain.repository.RatingRepository
import com.unina.natourkt.core.util.Constants
import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * This UseCase is used to rate a route
 * @see [RatingRepository]
 */
class RateRouteUseCase @Inject constructor(
    private val ratingRepository: RatingRepository,
) {

    operator fun invoke(rating: RatingCreation): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading())
        Log.i(
            Constants.RATING_MODEL,
            "Reporting route with ID: ${rating.ratedRouteId} authored by user with ID: ${rating.author}"
        )

        val result = ratingRepository.createRating(rating)
        emit(result)
    }
}