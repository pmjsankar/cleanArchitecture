package com.pmj.domain.model

import androidx.annotation.Keep

/**
 * Default Error Entity class for Api service errors
 * @param statusCode the status code of service
 * @param statusMessage the message from api service
 */
@Keep
data class ApiError(
    val statusCode: Int = 0,
    val statusMessage: String? = null
)