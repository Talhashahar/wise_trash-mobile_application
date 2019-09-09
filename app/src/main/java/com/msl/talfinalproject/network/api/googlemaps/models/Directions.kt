package com.msl.talfinalproject.network.api.googlemaps.models

import com.bnhp.network.baseresponses.BaseResponse
import com.google.gson.annotations.SerializedName

data class Directions(

	@SerializedName("geocoded_waypoints") val geocoded_waypoints : List<Geocoded_waypoints>,
	@SerializedName("routes") val routes : List<Routes>,
	@SerializedName("status") val status : String
) : BaseResponse()