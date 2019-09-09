package com.msl.talfinalproject.network.api.googlemaps.models

import com.google.gson.annotations.SerializedName

data class Overview_polyline (

	@SerializedName("points") val points : String
)