package com.railway.venkatachalam.irctcrailinfo.beans.seatavailability

import com.google.gson.annotations.SerializedName

data class SeatAvailabilityBean(@SerializedName("response_code")
                                val responseCode: Int = 0,
                                @SerializedName("quota")
                                val quota: Quota,
                                @SerializedName("from_station")
                                val fromStation: FromStation,
                                @SerializedName("journey_class")
                                val journeyClass: JourneyClass,
                                @SerializedName("availability")
                                val availability: List<AvailabilityItem>?,
                                @SerializedName("debit")
                                val debit: Int = 0,
                                @SerializedName("train")
                                val train: Train,
                                @SerializedName("to_station")
                                val toStation: ToStation)