package com.msl.talfinalproject.network.api.trashwise

import com.bnhp.network.annotations.ServerUrl
import com.bnhp.network.baseresponses.BaseResponse
import com.msl.talfinalproject.network.api.trashwise.models.Locations
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@ServerUrl(serverUrl = "http://34.244.239.221:5000/")
interface ITrashWiseApi {


    @GET("get_trash_bins_to_pickup")
    fun getLocations() : Observable<Locations>


    @POST("send_event")
    fun sendTrashEvent(
        @Query("origin") origin: String,
        @Query("driver_id") driverId: String,
        @Query("trashStatus") status : Int
    ) : Observable<BaseResponse>
}