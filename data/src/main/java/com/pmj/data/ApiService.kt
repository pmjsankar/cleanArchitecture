package com.pmj.data

import com.pmj.domain.model.Dining
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit API Service
 */
interface ApiService {

    @GET("dining-v2")
    suspend fun getDining(): Response<List<Dining>>
}