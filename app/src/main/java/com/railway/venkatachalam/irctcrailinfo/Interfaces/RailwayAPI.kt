package com.railway.venkatachalam.irctcrailinfo.Interfaces

import com.railway.venkatachalam.irctcrailinfo.beans.cancelledtrains.CancelledBean
import com.railway.venkatachalam.irctcrailinfo.beans.fareenquiry.FareEnquiryBean
import com.railway.venkatachalam.irctcrailinfo.beans.livetrainstatus.LiveTrainStatusBean
import com.railway.venkatachalam.irctcrailinfo.beans.pnrstatus.PnrStatusBean
import com.railway.venkatachalam.irctcrailinfo.beans.rescheduletrains.RescheduledBean
import com.railway.venkatachalam.irctcrailinfo.beans.seatavailability.SeatAvailabilityBean
import com.railway.venkatachalam.irctcrailinfo.beans.trainarrivals.TrainArrivalsBean
import com.railway.venkatachalam.irctcrailinfo.beans.trainbwstations.TrainBetweenStationsBean
import com.railway.venkatachalam.irctcrailinfo.beans.trainroute.TrainRouteBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RailwayAPI
{

    @GET("v2/between/source/{from_stn_code}/dest/{to_stn_code}/date/{date}/apikey/9pjess03tx/")
    fun gettrainbetweenstations(@Path("from_stn_code")from: String, @Path("to_stn_code") to: String,@Path("date") date: String):Call<TrainBetweenStationsBean>

    @GET("v2/pnr-status/pnr/{pnr}/apikey/9pjess03tx/")
    fun getpnrstatus(@Path("pnr")pnr_number: String):Call<PnrStatusBean>

    @GET("v2/cancelled/date/{cancel_date}/apikey/9pjess03tx/")
    fun getcancelledtrains(@Path("cancel_date")cancel_date: String):Call<CancelledBean>

    @GET("v2/rescheduled/date/{resc_date}/apikey/9pjess03tx/")
    fun getrescheduletrains(@Path("resc_date")resc_date: String):Call<RescheduledBean>

    @GET("v2/live/train/12603/date/27-07-2018/apikey/9pjess03tx/")
    fun getLiveTrainStatus():Call<LiveTrainStatusBean>

    @GET("v2/arrivals/station/{train_code}/hours/{hours}/apikey/9pjess03tx/")
    fun getTrainArrivals(@Path("train_code")train_code: String,@Path("hours") hours: String):Call<TrainArrivalsBean>

    @GET("v2/fare/train/{trainNumb}/source/{fromStation}/dest/{toStation}/age/25/pref/{classcode}/quota/{quota}/date/{faredate}/apikey/9pjess03tx/")
    fun getFare(@Path("quota") quota: String?, @Path("classcode") classcode: String?, @Path("trainNumb") trainNumb: String?, @Path("fromStation") fromStation: String?, @Path("toStation") toStation: String?,@Path("faredate") faredate: String?):Call<FareEnquiryBean>

    @GET("v2/check-seat/train/{trainNumber}/source/{fromStation}/dest/{toStation}/date/{seatdate}/pref/{classcode}/quota/{seatquota}/apikey/9pjess03tx/")
    fun getSeatAvailability(@Path("trainNumber")trainNumber: String?, @Path("fromStation") fromStation: String?, @Path("toStation") toStation: String?, @Path("seatdate") seatdate: String, @Path("seatquota") seatquota: String,@Path("classcode") classcode: String):Call<SeatAvailabilityBean>

    @GET("v2/route/train/{trainNumber}/apikey/9pjess03tx/")
    fun getTrainRoute(@Path("trainNumber") trainNumber: String?):Call<TrainRouteBean>
}