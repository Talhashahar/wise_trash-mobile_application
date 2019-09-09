package com.msl.talfinalproject.network.api.googlemaps.models

import com.google.gson.annotations.SerializedName


data class Steps (

	@SerializedName("distance") val distance : Distance,
	@SerializedName("duration") val duration : Duration,
	@SerializedName("end_location") val end_location : End_location,
	@SerializedName("html_instructions") val html_instructions : String,
	@SerializedName("polyline") val polyline : Polyline,
	@SerializedName("start_location") val start_location : Start_location,
	@SerializedName("travel_mode") val travel_mode : String
)