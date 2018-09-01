package com.railway.venkatachalam.irctcrailinfo.beans.pnrstatus

import com.google.gson.annotations.SerializedName

data class JourneyClass(@SerializedName("code")
                        val code: String = "",
                        @SerializedName("name")
                        val name: String = "")