package com.railway.venkatachalam.irctcrailinfo.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.seatavailability.SeatAvailabilityBean
import kotlinx.android.synthetic.main.indiv_seatavailfragment.view.*
import kotlinx.android.synthetic.main.seatavailfragment.*
import kotlinx.android.synthetic.main.seatavailfragment.view.*

class SeatAvailFragment:Fragment()
{
    var contex:Context? = null
    var respo:SeatAvailabilityBean? = null

    fun setavaildata(applicationContext: Context?, body: SeatAvailabilityBean?) {
        this.contex = applicationContext
        this.respo = body
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View = inflater.inflate(R.layout.seatavailfragment,container,false)

        if(respo!!.responseCode == 200) {
            view.seatfrag_trainname.text = respo!!.train.name
            view.seatfrag_trainnumber.text = respo!!.train.number
            view.seatfrag_source.text = respo!!.fromStation.name
            view.seatfrag_source_code.text = respo!!.fromStation.code
            view.seatfrag_dest.text = respo!!.toStation.name
            view.seatfrag_dest_code.text = respo!!.toStation.code

            view.seat_avail_recycle.layoutManager = LinearLayoutManager(contex, LinearLayoutManager.VERTICAL, false)
            view.seat_avail_recycle.adapter = SeatAvailRecycleAdapter(contex, respo)
        }else
            Toast.makeText(contex,"Response Code : "+respo!!.responseCode, Toast.LENGTH_SHORT).show()
        return view
    }

}

class SeatAvailRecycleAdapter(contex: Context?, respo: SeatAvailabilityBean?) : RecyclerView.Adapter<SeatAvailHolder>() {
    var contex = contex
    var respo = respo
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SeatAvailHolder {
        var view:View = LayoutInflater.from(contex).inflate(R.layout.indiv_seatavailfragment,p0,false)
        return SeatAvailHolder(view)
    }

    override fun getItemCount(): Int {
        return respo!!.availability!!.size
    }

    override fun onBindViewHolder(p0: SeatAvailHolder, p1: Int) {

        p0.indiv_sno!!.text = (p1+1).toString()
        p0.indiv_date!!.text = respo!!.availability!![p1].date
        p0.indiv_avail!!.text = respo!!.availability!![p1].status
    }

}
class SeatAvailHolder:RecyclerView.ViewHolder
{
    var indiv_sno:TextView? = null
    var indiv_date:TextView? = null
    var indiv_avail:TextView? = null

    constructor(view: View):super(view){
        indiv_sno = view.indiv_seat_sno
        indiv_date = view.indiv_seat_date
        indiv_avail = view.indiv_seat_avail
    }
}
