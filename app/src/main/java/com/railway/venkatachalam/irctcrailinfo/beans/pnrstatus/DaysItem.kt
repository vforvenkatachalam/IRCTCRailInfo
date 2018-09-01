package com.railway.venkatachalam.irctcrailinfo.beans.pnrstatus

import com.google.gson.annotations.SerializedName

data class DaysItem(@SerializedName("code")
                    val code: String = "",
                    @SerializedName("runs")
                    val runs: String = "")