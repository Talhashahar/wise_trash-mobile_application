package com.msl.talfinalproject.network.api.googlemaps

import com.bnhp.network.annotations.ServerUrl
import com.msl.talfinalproject.network.api.googlemaps.models.Directions
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

@ServerUrl(serverUrl = "https://maps.googleapis.com/")
interface IGoogleMapsApi {

    @GET("maps/api/directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Observable<Directions>
}