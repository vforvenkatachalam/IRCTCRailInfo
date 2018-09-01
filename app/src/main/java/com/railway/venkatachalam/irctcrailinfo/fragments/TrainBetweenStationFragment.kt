package com.railway.venkatachalam.irctcrailinfo.fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.adapters.TrainBetweenStationsAdapter
import com.railway.venkatachalam.irctcrailinfo.beans.trainbwstations.TrainBetweenStationsBean
import kotlinx.android.synthetic.main.trainbwstationfragment.view.*

class TrainBetweenStationFragment:Fragment() {
    var resp:TrainBetweenStationsBean? = null
    var fm:FragmentManager? = null
    var contex:Context? = null
    var resp_date:String? = null
    var progressDialog:ProgressDialog? = null
    fun setTrainData(responseobj: TrainBetweenStationsBean?, applicationContext: Context, resp_date: String, fm: FragmentManager, progressDialog: ProgressDialog) {
        this.resp_date = resp_date
        this.resp = responseobj
        this.fm = fm
        this.progressDialog = progressDialog
        this.contex = applicationContext
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v: View = inflater.inflate(R.layout.trainbwstationfragment, container, false)

        v.trainbwstationsrecycle.layoutManager = LinearLayoutManager(contex, LinearLayoutManager.VERTICAL, false)
        //Toast.makeText(context,"Creation \n"+resp,Toast.LENGTH_LONG).show()
        //Log.i("onCreateView",""+resp)
        v.trainbwstationsrecycle.adapter = TrainBetweenStationsAdapter(resp!!, contex!!, resp_date!!,fm!!,progressDialog!!)
        v.trainbwstationsrecycle.setHasFixedSize(true)
        return v
    }
}