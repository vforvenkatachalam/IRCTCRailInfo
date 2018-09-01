package com.railway.venkatachalam.irctcrailinfo.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.livetrainstatus.LiveTrainStatusBean
import kotlinx.android.synthetic.main.livetrainstatusfragment.view.*

class LiveTrainStatusFragment:Fragment(){

    var contex:Context? = null
    var resp:LiveTrainStatusBean? = null

    fun setLiveTrainStatus(applicationContext: Context?, body: LiveTrainStatusBean?) {

        this.contex = applicationContext
        this.resp = body

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View = inflater.inflate(R.layout.livetrainstatusfragment,container,false)

        view.live_train_recyclerview.layoutManager = LinearLayoutManager(contex,LinearLayoutManager.VERTICAL,false)
        view.live_train_recyclerview.adapter = LiveTrainStatusFragmentAdapter(contex,resp)

        return view
    }
}

class LiveTrainStatusFragmentAdapter(contex: Context?, resp: LiveTrainStatusBean?) : RecyclerView.Adapter<LiveTrainHolder>() {

    var contex = contex
    var resp = resp

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LiveTrainHolder {
        var view = LayoutInflater.from(contex).inflate(R.layout.indiv_trainarrivalstatusfragment,p0,false)
        return LiveTrainHolder(view)
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(p0: LiveTrainHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
class LiveTrainHolder:RecyclerView.ViewHolder
{
    constructor(view: View):super(view)
}
