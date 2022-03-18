package com.unina.natourkt.core.domain.repository

import com.unina.natourkt.core.domain.model.RatingCreation
import com.unina.natourkt.core.util.DataState

interface RatingRepository {

    suspend fun createRating(rating: RatingCreation): DataState<Unit>
}