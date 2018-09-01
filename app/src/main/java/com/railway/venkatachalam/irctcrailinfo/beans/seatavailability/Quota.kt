package com.railway.venkatachalam.irctcrailinfo.beans.seatavailability

import com.google.gson.annotations.SerializedName

data class Quota(@SerializedName("code")
                 val code: String = "",
                 @SerializedName("name")
                 val name: String = "")