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
import com.railway.venkatachalam.irctcrailinfo.beans.cancelledtrains.CancelledBean
import kotlinx.android.synthetic.main.cancelledtrainsfragment.view.*
import kotlinx.android.synthetic.main.indiv_cancelledtrainfragment.view.*

class CancelledTrainsFragment:Fragment(){

    var resp_obj:CancelledBean? = null
    var contex:Context? = null

    fun setCancelledData(applicationContext: Context?, resp_obj: CancelledBean?) {
        this.resp_obj = resp_obj
        this.contex = applicationContext
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View = inflater.inflate(R.layout.cancelledtrainsfragment,container,false)

        view.cancelledtrainsrecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        view.cancelledtrainsrecyclerview.adapter = CancelledTrainsAdapter(contex,resp_obj)
        return view
    }


}

class CancelledTrainsAdapter(contex: Context?, resp_obj: CancelledBean?) : RecyclerView.Adapter<CancelledHolder>() {

    var contex = contex
    var resp_obj = resp_obj

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CancelledHolder
    {
        var view:View = LayoutInflater.from(contex).inflate(R.layout.indiv_cancelledtrainfragment,p0,false)
        return CancelledHolder(view)
    }

    override fun getItemCount(): Int
    {
        return resp_obj!!.trains!!.size
    }

    override fun onBindViewHolder(p0: CancelledHolder, p1: Int) {

        p0.train_name!!.text= resp_obj!!.trains!![p1].name
        p0.travel_date!!.text=resp_obj!!.trains!![p1].startTime
        p0.train_number!!.text=resp_obj!!.trains!![p1].number
        p0.train_type!!.text=resp_obj!!.trains!![p1].type
        p0.dest_station_name!!.text= resp_obj!!.trains!![p1].dest.name
        p0.dest_station_code!!.text= resp_obj!!.trains!![p1].dest.code
        p0.src_station_name!!.text= resp_obj!!.trains!![p1].source.name
        p0.src_station_code!!.text= resp_obj!!.trains!![p1].source.code
    }

}
class CancelledHolder:RecyclerView.ViewHolder{

    var src_station_name:TextView? = null
    var src_station_code:TextView? = null
    var dest_station_name:TextView? = null
    var dest_station_code:TextView? = null
    var train_type:TextView? = null
    var train_number:TextView? = null
    var travel_date:TextView? = null
    var train_name:TextView? = null

    constructor(view: View):super(view)
    {
        src_station_code = view.cancelled_src_stn_code
        src_station_name = view.cancelled_src_stn_name
        dest_station_code = view.cancelled_dest_stn_code
        dest_station_name = view.cancelled_dest_stn_name
        train_type = view.cancelled_type
        train_number = view.cancelled_train_number
        travel_date = view.cancelled_date
        train_name = view.cancelled_train_name
    }
}
