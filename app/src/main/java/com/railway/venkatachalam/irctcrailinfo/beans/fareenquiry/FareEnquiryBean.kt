package com.railway.venkatachalam.irctcrailinfo.beans.fareenquiry

import com.google.gson.annotations.SerializedName

data class FareEnquiryBean(@SerializedName("fare")
                           val fare: Int = 0,
                           @SerializedName("response_code")
                           val responseCode: Int = 0,
                           @SerializedName("quota")
                           val quota: Quota,
                           @SerializedName("from_station")
                           val fromStation: FromStation,
                           @SerializedName("journey_class")
                           val journeyClass: JourneyClass,
                           @SerializedName("debit")
                           val debit: Int = 0,
                           @SerializedName("train")
                           val train: Train,
                           @SerializedName("to_station")
                           val toStation: ToStation)