package com.msl.talfinalproject.network.api.googlemaps.models

import com.google.gson.annotations.SerializedName


data class Routes (

	@SerializedName("bounds") val bounds : Bounds,
	@SerializedName("copyrights") val copyrights : String,
	@SerializedName("legs") val legs : List<Legs>,
	@SerializedName("overview_polyline") val overview_polyline : Overview_polyline,
	@SerializedName("summary") val summary : String,
	@SerializedName("warnings") val warnings : List<String>,
	@SerializedName("waypoint_order") val waypoint_order : List<String>
)