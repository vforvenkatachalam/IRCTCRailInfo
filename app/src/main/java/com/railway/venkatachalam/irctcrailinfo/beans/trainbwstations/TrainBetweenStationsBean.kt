package com.railway.venkatachalam.irctcrailinfo.beans.trainbwstations

import com.google.gson.annotations.SerializedName

data class TrainBetweenStationsBean(@SerializedName("response_code")
                                    val responseCode: Int = 0,
                                    @SerializedName("total")
                                    val total: Int = 0,
                                    @SerializedName("debit")
                                    val debit: Int = 0,
                                    @SerializedName("trains")
                                    val trains: List<TrainsItem>?)