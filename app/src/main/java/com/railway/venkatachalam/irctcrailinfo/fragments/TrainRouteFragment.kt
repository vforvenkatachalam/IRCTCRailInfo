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
import com.railway.venkatachalam.irctcrailinfo.beans.trainroute.TrainRouteBean
import kotlinx.android.synthetic.main.indiv_trainroutefragment.view.*
import kotlinx.android.synthetic.main.trainroutefragment.view.*

class TrainRouteFragment:Fragment()
{
    var contex:Context? = null
    var resp:TrainRouteBean? = null

    fun settrainroutedata(applicationContext: Context?, body: TrainRouteBean?) {
        this.contex = applicationContext
        this.resp = body
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.trainroutefragment,container,false)
        view.train_route_name.text = resp!!.train.name
        view.train_route_number.text = resp!!.train.number
        view.train_route_recycle.layoutManager = LinearLayoutManager(contex,LinearLayoutManager.VERTICAL,false)
        view.train_route_recycle.adapter = TrainRouteAdapter(contex,resp)
        return view
    }

}

class TrainRouteAdapter(contex: Context?, resp: TrainRouteBean?) : RecyclerView.Adapter<TrainRouteHolder>() {
    var contex = contex
    var resp = resp
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TrainRouteHolder {
        var view:View = LayoutInflater.from(contex).inflate(R.layout.indiv_trainroutefragment,p0,false)
        return TrainRouteHolder(view)
    }

    override fun getItemCount(): Int {
        return resp!!.route!!.size
    }

    override fun onBindViewHolder(p0: TrainRouteHolder, p1: Int) {
        p0.indiv_trainroute_stnumber!!.text = resp!!.route!![p1].station.code
        p0.indiv_trainroute_stname!!.text = resp!!.route!![p1].station.name
        p0.indiv_trainroute_arrival!!.text = resp!!.route!![p1].scharr
        p0.indiv_trainroute_dept!!.text = resp!!.route!![p1].schdep
        p0.indiv_trainroute_distance!!.text = resp!!.route!![p1].distance.toString()+"Km"
        if(resp!!.route!![p1].halt >= 0)
            p0.indiv_trainroute_halt!!.text = ""+resp!!.route!![p1].halt+" mins"
        else
            p0.indiv_trainroute_halt!!.text = "-"
    }

}
class TrainRouteHolder:RecyclerView.ViewHolder
{
    var indiv_trainroute_stname:TextView? = null
    var indiv_trainroute_stnumber:TextView? = null
    var indiv_trainroute_arrival:TextView? = null
    var indiv_trainroute_dept:TextView? = null
    var indiv_trainroute_distance:TextView? = null
    var indiv_trainroute_halt:TextView? = null

    constructor(view:View):super(view)
    {
        indiv_trainroute_arrival = view.indiv_trainroute_arrival
        indiv_trainroute_dept = view.indiv_trainroute_dept
        indiv_trainroute_distance = view.indiv_trainroute_distance
        indiv_trainroute_halt = view.indiv_trainroute_halt
        indiv_trainroute_stname = view.indiv_trainroute_stname
        indiv_trainroute_stnumber = view.indiv_trainroute_stnumber
    }
}
