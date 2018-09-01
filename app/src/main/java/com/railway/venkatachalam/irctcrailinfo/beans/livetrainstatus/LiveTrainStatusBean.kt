package com.railway.venkatachalam.irctcrailinfo.beans.livetrainstatus

import com.google.gson.annotations.SerializedName

data class LiveTrainStatusBean(@SerializedName("response_code")
                               val responseCode: Int = 0,
                               @SerializedName("route")
                               val route: List<RouteItem>?,
                               @SerializedName("position")
                               val position: String = "",
                               @SerializedName("debit")
                               val debit: Int = 0,
                               @SerializedName("current_station")
                               val currentStation: CurrentStation,
                               @SerializedName("train")
                               val train: Train,
                               @SerializedName("start_date")
                               val startDate: String = "")