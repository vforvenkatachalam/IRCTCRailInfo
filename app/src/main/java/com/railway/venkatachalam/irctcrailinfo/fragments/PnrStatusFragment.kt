package com.railway.venkatachalam.irctcrailinfo.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.pnrstatus.PassengersItem
import com.railway.venkatachalam.irctcrailinfo.beans.pnrstatus.PnrStatusBean
import kotlinx.android.synthetic.main.indiv_passengerdetailsfragment.view.*
import kotlinx.android.synthetic.main.pnrstatusfragment.view.*

class PnrStatusFragment: Fragment {

    var pnr_resp:PnrStatusBean? = null
    var contex:Context? = null
    constructor(){

    }
    fun setPnrStatusFragment(pnr_res:PnrStatusBean,context: Context)
    {
        this.contex = context
        this.pnr_resp = pnr_res
        Log.i("setPnrStatusFragment : ",""+pnr_resp)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View = inflater.inflate(R.layout.pnrstatusfragment,container,false)

        view.passengerdetailrecycle.layoutManager = LinearLayoutManager(contex,LinearLayoutManager.VERTICAL,false)
        view.passengerdetailrecycle.adapter = PassengerDetailsRecycleAdapter(pnr_resp!!.passengers, contex!!)
        view.pnr_train_name.text = pnr_resp!!.train.name
        view.pnr_train_number.text = pnr_resp!!.train.number
        view.pnr_from.text = pnr_resp!!.fromStation.name+" ("+pnr_resp!!.fromStation.code+")"
        view.pnr_to.text = pnr_resp!!.toStation.name+" ("+pnr_resp!!.toStation.code+")"
        view.pnr_boarding.text = pnr_resp!!.boardingPoint.name+" ("+pnr_resp!!.boardingPoint.code+")"
        view.pnr_chart_prep.text = pnr_resp!!.chartPrepared.toString()
        view.pnr_no_of_psgr.text = pnr_resp!!.totalPassengers.toString()
        view.pnr_reser_upto.text = pnr_resp!!.reservationUpto.name+" ("+pnr_resp!!.reservationUpto.code+")"

        return view
    }

    class PassengerDetailsRecycleAdapter(passengers: List<PassengersItem>?,context: Context) : RecyclerView.Adapter<PassengerDetailHolder>() {

        var list = passengers
        var contex = context
        var sno:MutableList<String> = mutableListOf()
        var booking:MutableList<String> = mutableListOf()
        var current:MutableList<String> = mutableListOf()
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PassengerDetailHolder {
            var view:View = LayoutInflater.from(contex).inflate(R.layout.indiv_passengerdetailsfragment,p0,false)

            for(x in this.list!!)
            {
                sno.add(x.no.toString())
                booking.add(x.bookingStatus)
                current.add(x.currentStatus)
            }

            return PassengerDetailHolder(view)

        }

        override fun getItemCount(): Int {
            return list!!.size
        }

        override fun onBindViewHolder(p0: PassengerDetailHolder, p1: Int) {
            p0.sno!!.text = sno[p1]
            p0.booking!!.text = booking[p1]
            p0.current!!.text = current[p1]
        }

    }
    class PassengerDetailHolder:RecyclerView.ViewHolder
    {
        var sno:TextView? = null
        var booking:TextView? = null
        var current:TextView? = null
        constructor(view: View):super(view)
        {
            sno = view.sno
            booking = view.booking_status
            current = view.current_status
        }
    }
}