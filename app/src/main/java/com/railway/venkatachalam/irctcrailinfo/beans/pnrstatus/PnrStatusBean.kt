package com.railway.venkatachalam.irctcrailinfo.beans.pnrstatus

import com.google.gson.annotations.SerializedName

data class PnrStatusBean(@SerializedName("passengers")
                         val passengers: List<PassengersItem>?,
                         @SerializedName("response_code")
                         val responseCode: Int = 0,
                         @SerializedName("reservation_upto")
                         val reservationUpto: ReservationUpto,
                         @SerializedName("from_station")
                         val fromStation: FromStation,
                         @SerializedName("journey_class")
                         val journeyClass: JourneyClass,
                         @SerializedName("chart_prepared")
                         val chartPrepared: Boolean = false,
                         @SerializedName("boarding_point")
                         val boardingPoint: BoardingPoint,
                         @SerializedName("pnr")
                         val pnr: String = "",
                         @SerializedName("debit")
                         val debit: Int = 0,
                         @SerializedName("doj")
                         val doj: String = "",
                         @SerializedName("to_station")
                         val toStation: ToStation,
                         @SerializedName("train")
                         val train: Train,
                         @SerializedName("total_passengers")
                         val totalPassengers: Int = 0)