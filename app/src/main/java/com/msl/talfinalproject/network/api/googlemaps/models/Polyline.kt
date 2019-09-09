package com.msl.talfinalproject.network.api.googlemaps.models

import com.google.gson.annotations.SerializedName

data class Polyline (

	@SerializedName("points") val points : String
)