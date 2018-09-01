package com.railway.venkatachalam.irctcrailinfo.beans.cancelledtrains

import com.google.gson.annotations.SerializedName

data class TrainsItem(@SerializedName("number")
                      val number: String = "",
                      @SerializedName("start_time")
                      val startTime: String = "",
                      @SerializedName("name")
                      val name: String = "",
                      @SerializedName("source")
                      val source: Source,
                      @SerializedName("type")
                      val type: String = "",
                      @SerializedName("dest")
                      val dest: Dest)