package com.railway.venkatachalam.irctcrailinfo.beans.trainarrivals

import com.google.gson.annotations.SerializedName

data class TrainArrivalsBean(@SerializedName("total")
                             val total: Int = 0,
                             @SerializedName("response_code")
                             val responseCode: Int = 0,
                             @SerializedName("debit")
                             val debit: Int = 0,
                             @SerializedName("trains")
                             val trains: List<TrainsItem>?)