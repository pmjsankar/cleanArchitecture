package com.pmj.domain.repository

import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import kotlinx.coroutines.flow.Flow

/**
 * Interface of Dining Repository to expose to other module
 */
interface DiningRepository {

    /**
     * Method to fetch the dining details from Repository
     * @return Flow of Output with dining list
     */
    fun fetchDining(): Flow<Output<List<Dining>>>
}