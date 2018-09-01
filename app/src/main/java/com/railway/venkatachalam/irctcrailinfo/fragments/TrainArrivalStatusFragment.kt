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
import com.railway.venkatachalam.irctcrailinfo.beans.trainarrivals.TrainArrivalsBean
import kotlinx.android.synthetic.main.indiv_trainarrivalstatusfragment.view.*
import kotlinx.android.synthetic.main.trainarrivalstatusfragment.view.*

class TrainArrivalStatusFragment:Fragment() {

    var contex:Context? = null
    var resp:TrainArrivalsBean? = null

    fun setTrainArrivals(applicationContext: Context?, body: TrainArrivalsBean?) {
        this.contex = applicationContext
        this.resp = body
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View = inflater.inflate(R.layout.trainarrivalstatusfragment,container,false)

        view.train_arrival_status_recycle.layoutManager = LinearLayoutManager(contex,LinearLayoutManager.VERTICAL,false)
        view.train_arrival_status_recycle.adapter = TrainArrivalsAdapter(contex,resp)

        return view
    }
}

class TrainArrivalsAdapter(contex: Context?, resp: TrainArrivalsBean?) : RecyclerView.Adapter<ArrivalHolder>() {

    var contex = contex
    var resp = resp
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ArrivalHolder {
        var view:View = LayoutInflater.from(contex).inflate(R.layout.indiv_trainarrivalstatusfragment,p0,false)
        return ArrivalHolder(view)
    }

    override fun getItemCount(): Int {
        return resp!!.total
    }

    override fun onBindViewHolder(p0: ArrivalHolder, p1: Int) {

        p0.trainarrivalname!!.text = resp!!.trains!![p1].name
        p0.trainarrivalnumber!!.text = resp!!.trains!![p1].number
        p0.sch_arr!!.text = resp!!.trains!![p1].scharr
        p0.sch_dept!!.text = resp!!.trains!![p1].schdep
        p0.acc_arr!!.text = resp!!.trains!![p1].actarr
        p0.acc_dept!!.text = resp!!.trains!![p1].actdep
        p0.arrival_delay!!.text = "Delay :"+resp!!.trains!![p1].delayarr
        p0.depart_delay!!.text = "Delay : "+resp!!.trains!![p1].delaydep
    }

}
class ArrivalHolder:RecyclerView.ViewHolder
{
    var trainarrivalnumber:TextView? = null
    var trainarrivalname:TextView? = null
    var sch_arr:TextView? = null
    var sch_dept:TextView? = null
    var acc_arr:TextView? = null
    var acc_dept:TextView? = null
    var arrival_delay:TextView? = null
    var depart_delay:TextView? = null

    constructor(view: View):super(view)
    {
        trainarrivalnumber = view.train_arrival_number
        trainarrivalname = view.train_arrival_name
        sch_arr = view.train_arrival_sch_arr
        sch_dept = view.train_arrival_sch_dep
        acc_arr = view.train_arrival_acc_arr
        acc_dept = view.train_arrival_acc_dep
        arrival_delay = view.arr_delay
        depart_delay = view.dept_delay
    }
}