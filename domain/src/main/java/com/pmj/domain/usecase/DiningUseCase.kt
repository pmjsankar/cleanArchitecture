package com.pmj.domain.usecase

import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import kotlinx.coroutines.flow.Flow

/**
 * Interface of dining details UseCase to expose to ui module
 */
interface DiningUseCase {
    /**
     * UseCase Method to fetch the dining details from Data Layer
     */
    fun execute(): Flow<Output<List<Dining>>>
}