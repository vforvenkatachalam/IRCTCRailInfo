package com.railway.venkatachalam.irctcrailinfo.beans.rescheduletrains

import com.google.gson.annotations.SerializedName

data class TrainsItem(@SerializedName("rescheduled_date")
                      val rescheduledDate: String = "",
                      @SerializedName("number")
                      val number: String = "",
                      @SerializedName("name")
                      val name: String = "",
                      @SerializedName("from_station")
                      val fromStation: FromStation,
                      @SerializedName("rescheduled_time")
                      val rescheduledTime: String = "",
                      @SerializedName("time_diff")
                      val timeDiff: String = "",
                      @SerializedName("to_station")
                      val toStation: ToStation)