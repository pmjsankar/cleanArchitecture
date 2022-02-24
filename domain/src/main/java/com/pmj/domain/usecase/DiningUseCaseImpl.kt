package com.pmj.domain.usecase

import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import com.pmj.domain.repository.DiningRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of dining details UseCase
 * @param diningRepository the dining repository object
 */
internal class DiningUseCaseImpl @Inject constructor(private val diningRepository: DiningRepository) :
    DiningUseCase {

    override fun execute(): Flow<Output<List<Dining>>> {
        return diningRepository.fetchDining()
    }
}