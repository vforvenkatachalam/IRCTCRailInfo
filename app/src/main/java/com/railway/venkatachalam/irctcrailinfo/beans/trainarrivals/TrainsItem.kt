package com.railway.venkatachalam.irctcrailinfo.beans.trainarrivals

import com.google.gson.annotations.SerializedName

data class TrainsItem(@SerializedName("actarr")
                      val actarr: String = "",
                      @SerializedName("number")
                      val number: String = "",
                      @SerializedName("actdep")
                      val actdep: String = "",
                      @SerializedName("schdep")
                      val schdep: String = "",
                      @SerializedName("scharr")
                      val scharr: String = "",
                      @SerializedName("name")
                      val name: String = "",
                      @SerializedName("delaydep")
                      val delaydep: String = "",
                      @SerializedName("delayarr")
                      val delayarr: String = "")