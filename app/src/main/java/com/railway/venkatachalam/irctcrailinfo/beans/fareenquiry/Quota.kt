package com.railway.venkatachalam.irctcrailinfo.beans.fareenquiry

import com.google.gson.annotations.SerializedName

data class Quota(@SerializedName("code")
                 val code: String = "",
                 @SerializedName("name")
                 val name: String = "")