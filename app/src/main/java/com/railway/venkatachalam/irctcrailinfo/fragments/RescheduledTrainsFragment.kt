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
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.rescheduletrains.RescheduledBean
import kotlinx.android.synthetic.main.indiv_rescheduledtrainsfragment.view.*
import kotlinx.android.synthetic.main.rescheduledtrainsfragment.view.*

class RescheduledTrainsFragment:Fragment() {

    var resp:RescheduledBean? = null
    var contex: Context? = null

    fun setRescheduledTrains(body: RescheduledBean?, applicationContext: Context) {
        this.resp = body
        this.contex = applicationContext

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View = inflater.inflate(R.layout.rescheduledtrainsfragment,container,false)

        view.rescheduledtrainsrecyclerview.layoutManager = LinearLayoutManager(contex,LinearLayoutManager.VERTICAL,false)
        view.rescheduledtrainsrecyclerview.adapter = RescheduledTrainAdapter(contex,resp)
        return view
    }
}

class RescheduledTrainAdapter(contex: Context?, resp: RescheduledBean?) : RecyclerView.Adapter<RescheduledHolder>() {

    var contex = contex
    var resp = resp

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RescheduledHolder {
        var view = LayoutInflater.from(contex).inflate(R.layout.indiv_rescheduledtrainsfragment,p0,false)
        return RescheduledHolder(view)
    }

    override fun getItemCount(): Int {
        return resp!!.trains!!.size
    }

    override fun onBindViewHolder(p0: RescheduledHolder, p1: Int) {

        p0.travel_date!!.text = resp!!.trains!![p1].rescheduledDate
        p0.res_by_time!!.text = "Rescheduled by : "+resp!!.trains!![p1].timeDiff
        p0.res_to_time!!.text = "Rescheduled to : "+resp!!.trains!![p1].rescheduledTime
        p0.train_number!!.text = resp!!.trains!![p1].number
        p0.train_name!!.text = resp!!.trains!![p1].name
        p0.dest_station_code!!.text = resp!!.trains!![p1].toStation.code
        p0.dest_station_name!!.text = resp!!.trains!![p1].toStation.name
        p0.src_station_code!!.text = resp!!.trains!![p1].fromStation.code
        p0.src_station_name!!.text = resp!!.trains!![p1].fromStation.name
    }

}
class RescheduledHolder:RecyclerView.ViewHolder
{
    var src_station_name: TextView? = null
    var src_station_code: TextView? = null
    var dest_station_name: TextView? = null
    var dest_station_code: TextView? = null
    var train_number: TextView? = null
    var travel_date: TextView? = null
    var train_name: TextView? = null
    var res_by_time:TextView? = null
    var res_to_time:TextView? = null

    constructor(view: View):super(view)
    {
        src_station_name = view.rescheduled_src_stn_name
        src_station_code = view.rescheduled_src_stn_code
        dest_station_name = view.rescheduled_dest_stn_name
        dest_station_code = view.rescheduled_dest_stn_code
        train_name = view.rescheduled_train_name
        train_number = view.rescheduled_train_number
        travel_date = view.rescheduled_date
        res_by_time = view.rescheduled_by_time
        res_to_time = view.rescheduled_to_time
    }
}