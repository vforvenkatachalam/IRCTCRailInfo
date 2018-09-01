package com.railway.venkatachalam.irctcrailinfo.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.fareenquiry.FareEnquiryBean
import kotlinx.android.synthetic.main.farenquiryfragment.view.*

class FareEnquiryFragment:Fragment() {

    var contex:Context? = null
    var respo:FareEnquiryBean? = null
    var superfastcharges:String? = null
    var reservationcharges:String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View = inflater.inflate(R.layout.farenquiryfragment,container,false)

        if(respo!!.journeyClass.code.equals("SL")){
            superfastcharges = "30"
            reservationcharges = "20"
        }else if(respo!!.journeyClass.code.equals("1A")){
            superfastcharges = "75"
            reservationcharges = "60"
        }else if(respo!!.journeyClass.code.equals("2A")){
            superfastcharges = "45"
            reservationcharges = "50"
        }else if(respo!!.journeyClass.code.equals("3A")){
            superfastcharges = "45"
            reservationcharges = "40"
        }else if(respo!!.journeyClass.code.equals("CC")){
            superfastcharges = "30"
            reservationcharges = "20"
        }else if(respo!!.journeyClass.code.equals("2S")){
            superfastcharges = "15"
            reservationcharges = "15"
        }else{
            superfastcharges = "15"
            reservationcharges = "15"
        }
        view.fare_train_name.text = respo!!.train.name
        view.fare_train_number.text = respo!!.train.number
        view.fare_source.text = respo!!.fromStation.name+" ("+respo!!.fromStation.code+")"
        view.fare_dest.text = respo!!.toStation.name+" ("+respo!!.toStation.code+")"
        view.fare_class.text = respo!!.journeyClass.name
        view.fare_reservcharges.text = reservationcharges
        view.fare_superfastcharges.text = superfastcharges
        view.fare_basefare.text =(((respo!!.fare - reservationcharges!!.toLong()) - superfastcharges!!.toLong()).toString())
        view.fare_othercharges.text = "0.0"
        view.fare_totalfare.text = respo!!.fare.toString()

        return view
    }

    fun setFareEnquiryInfo(applicationContext: Context?, body: FareEnquiryBean?) {
        this.contex = applicationContext
        this.respo = body
    }
}