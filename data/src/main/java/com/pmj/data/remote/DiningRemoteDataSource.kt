package com.pmj.data.remote

import com.pmj.data.ApiService
import com.pmj.data.BaseRemoteDataSource
import com.pmj.domain.model.Dining
import com.pmj.domain.model.Output
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * RemoteDataSource of dining API service
 * @param apiService the object of api service
 */
class DiningRemoteDataSource @Inject constructor(
    private val apiService: ApiService, retrofit: Retrofit
) : BaseRemoteDataSource(retrofit) {

    /**
     * Method to fetch the dining details from RemoteDataSource
     * @return Output with list of Dining
     */
    suspend fun fetchDining(): Output<List<Dining>> {
        return getResponse(
            request = { apiService.getDining() },
            defaultErrorMessage = "Error fetching dining details"
        )
    }
}