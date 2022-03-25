package com.unina.natourkt.core.domain.repository

import com.unina.natourkt.core.domain.model.RatingCreation
import com.unina.natourkt.core.util.DataState

/**
 * Interface for rating functions repository
 */
interface RatingRepository {

    /**
     * This function creates a rating taking only a [RatingCreation] model as
     * parameter
     */
    suspend fun createRating(rating: RatingCreation): DataState<Unit>
}