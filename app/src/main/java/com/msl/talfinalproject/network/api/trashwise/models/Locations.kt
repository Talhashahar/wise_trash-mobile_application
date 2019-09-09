package com.msl.talfinalproject.network.api.trashwise.models

import com.bnhp.network.baseresponses.BaseResponse


data class Locations(var arr: ArrayList<Destination>,
                     var capacity: Int = 0,
                     var driver: Driver? = null,
                     var count_bins: Int = 0) : BaseResponse(){

}