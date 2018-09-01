package com.railway.venkatachalam.irctcrailinfo.beans.seatavailability

import com.google.gson.annotations.SerializedName

data class AvailabilityItem(@SerializedName("date")
                            val date: String = "",
                            @SerializedName("status")
                            val status: String = "")