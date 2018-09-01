package com.railway.venkatachalam.irctcrailinfo.beans.rescheduletrains

import com.google.gson.annotations.SerializedName

data class RescheduledBean(@SerializedName("response_code")
                           val responseCode: Int = 0,
                           @SerializedName("debit")
                           val debit: Int = 0,
                           @SerializedName("trains")
                           val trains: List<TrainsItem>?)