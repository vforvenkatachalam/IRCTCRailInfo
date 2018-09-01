package com.railway.venkatachalam.irctcrailinfo.beans.livetrainstatus

import com.google.gson.annotations.SerializedName

data class ClassesItem(@SerializedName("code")
                       val code: String = "",
                       @SerializedName("available")
                       val available: String = "",
                       @SerializedName("name")
                       val name: String = "")