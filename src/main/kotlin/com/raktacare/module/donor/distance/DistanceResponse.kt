package com.raktacare.module.donor.distance

import kotlinx.serialization.Serializable

@Serializable
data class DistanceResponse (
	val routes : List<Routes>,
	val status : String
) {
	@Serializable
	data class Routes (
		val legs : List<Legs>,
	) {
		@Serializable
		data class Legs (
			val distance : Distance,
			val duration : Duration,
		) {
			@Serializable
			data class Distance (
				val text : String,
				val value : Double
			)
			@Serializable
			data class Duration (
				val text : String,
				val value : Double
			)
		}
	}
	@Serializable
	data class DistanceInfo(
		val distance: Double,
		val duration: Double
	)
	fun toDistanceInfo() = DistanceInfo(
		((routes.firstOrNull()?.legs?.firstOrNull()?.distance?.value ?: 0.0)/1000.0),
		((routes.firstOrNull()?.legs?.firstOrNull()?.duration?.value ?: 0.0)/60.0)
	)
}