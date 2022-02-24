package com.pmj.domain.model

import androidx.annotation.Keep

@Keep
data class Dining(
	val offer: String? = null,
	val address: String? = null,
	val price: String? = null,
	val imageUrl: String? = null,
	val rating: String? = null,
	val title: String? = null,
	val desc: String? = null
)

