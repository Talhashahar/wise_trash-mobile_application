package com.msl.talfinalproject.network.api.googlemaps.models

import com.google.gson.annotations.SerializedName

data class End_location (

	@SerializedName("lat") val lat : Double,
	@SerializedName("lng") val lng : Double
)