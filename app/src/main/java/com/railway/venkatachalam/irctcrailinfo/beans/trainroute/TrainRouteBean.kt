package com.railway.venkatachalam.irctcrailinfo.beans.trainroute

import com.google.gson.annotations.SerializedName

data class TrainRouteBean(@SerializedName("response_code")
                          val responseCode: Int = 0,
                          @SerializedName("route")
                          val route: List<RouteItem>?,
                          @SerializedName("debit")
                          val debit: Int = 0,
                          @SerializedName("train")
                          val train: Train)