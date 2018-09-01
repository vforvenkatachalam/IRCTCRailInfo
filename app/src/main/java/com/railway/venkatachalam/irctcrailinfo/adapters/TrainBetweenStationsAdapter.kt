package com.railway.venkatachalam.irctcrailinfo.adapters

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.railway.venkatachalam.irctcrailinfo.Interfaces.RailwayAPI
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.seatavailability.SeatAvailabilityBean
import com.railway.venkatachalam.irctcrailinfo.beans.trainbwstations.TrainBetweenStationsBean
import com.railway.venkatachalam.irctcrailinfo.fragments.SeatAvailFragment
import kotlinx.android.synthetic.main.indiv_trainbwstations.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrainBetweenStationsAdapter:RecyclerView.Adapter<MyHolder> {

    var contex:Context? = null
    var respdate:String? = null
    var fm:FragmentManager? = null
    var resp:TrainBetweenStationsBean? = null
    var progressDialog:ProgressDialog? = null
    constructor(resp:TrainBetweenStationsBean, context: Context, respdate:String, fm:FragmentManager,progressDialog:ProgressDialog)
    {
        this.fm = fm
        this.contex = context
        this.resp = resp
        this.respdate = respdate
        this.progressDialog = progressDialog
        this.progressDialog!!.setTitle("Loading...!")
        this.progressDialog!!.isIndeterminate=true
        this.progressDialog!!.setMessage("Please wait to fetch your requested data")
        this.progressDialog!!.setCancelable(false)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder {
        var view:View = LayoutInflater.from(contex).inflate(R.layout.indiv_trainbwstations,p0,false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return resp!!.total
    }

    fun getSeatAvail(number: String, fromcode: String, tocode: String, respdate: String, quota: String, classcode: String, it: View)
    {
        var retro = Retrofit.Builder().baseUrl("https://api.railwayapi.com/").addConverterFactory(GsonConverterFactory.create()).build()
        var api = retro.create(RailwayAPI::class.java)

        var call = api.getSeatAvailability(number,fromcode,tocode,respdate,quota,classcode)
        call.enqueue(object: Callback<SeatAvailabilityBean> {
            override fun onFailure(call: Call<SeatAvailabilityBean>?, t: Throwable?) {
                progressDialog!!.dismiss()
                Snackbar.make(it,"Failure Cause : "+t!!.message,Snackbar.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<SeatAvailabilityBean>?, response: Response<SeatAvailabilityBean>?) {
                progressDialog!!.dismiss()
                if(response!!.body()!!.responseCode == 200)
                {
                    var tx:FragmentTransaction = fm!!.beginTransaction()
                    var seatAvailFragment = SeatAvailFragment()
                    seatAvailFragment.setavaildata(contex,response.body())
                    tx.add(R.id.trainbwstationsfrag,seatAvailFragment)
                    tx.addToBackStack("back")
                    tx.commit()
                }else {
                    dialogshow(response.body()!!.responseCode.toString())
                    Snackbar.make(it, dialogshow(response.body()!!.responseCode.toString())!!,Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onBindViewHolder(p0: MyHolder, p1: Int) {
        p0.dest_ariv_time!!.text = resp!!.trains!![p1].destArrivalTime
        p0.src_dept_time!!.text = resp!!.trains!![p1].srcDepartureTime
        p0.from_stn_name!!.text = resp!!.trains!![p1].fromStation.name
        p0.from_stn_code!!.text = resp!!.trains!![p1].fromStation.code
        p0.to_stn_name!!.text = resp!!.trains!![p1].toStation.name
        p0.to_stn_code!!.text = resp!!.trains!![p1].toStation.code
        p0.traveltime!!.text = resp!!.trains!![p1].travelTime+" Hrs"
        p0.train_number!!.text = resp!!.trains!![p1].number
        p0.train_name!!.text = resp!!.trains!![p1].name

        p0.trainbwstn_card!!.setOnClickListener({

            progressDialog!!.show()
            getSeatAvail(resp!!.trains!![p1].number,resp!!.trains!![p1].fromStation.code,resp!!.trains!![p1].toStation.code,
                    respdate.toString(),"GN","SL",it)
        })

        for(x in resp!!.trains!![p1].days!!){
            if(x.code.equals("MON")) {
                if(x.runs.equals("N")) {
                    p0.monday!!.setTextColor(Color.parseColor("#BDBDBD"))
                }
            }else if(x.code.equals("TUE")) {
                if(x.runs.equals("N")) {
                    p0.tuesday!!.setTextColor(Color.parseColor("#BDBDBD"))
                }
            }else if(x.code.equals("WED")) {
                if(x.runs.equals("N")) {
                    p0.wednesday!!.setTextColor(Color.parseColor("#BDBDBD"))
                }
            }else if(x.code.equals("THU")) {
                if(x.runs.equals("N")) {
                    p0.thursday!!.setTextColor(Color.parseColor("#BDBDBD"))
                }
            }else if(x.code.equals("FRI")) {
                if(x.runs.equals("N")) {
                    p0.friday!!.setTextColor(Color.parseColor("#BDBDBD"))
                }
            }else if(x.code.equals("SAT")) {
                if(x.runs.equals("N")) {
                    p0.saturday!!.setTextColor(Color.parseColor("#BDBDBD"))
                }
            }else if(x.code.equals("SUN")) {
                if(x.runs.equals("N")) {
                    p0.sunday!!.setTextColor(Color.parseColor("#BDBDBD"))
                }
            }else{
            }
        }
    }
    fun dialogshow(toString: String): String? {
        lateinit var responseinfo: String
        if(toString.equals("502")){
            responseinfo = "Invalid arguments passed"
        }else if(toString.equals("210")){
            responseinfo = "Train doesn’t run on the date queried"
        }else if(toString.equals("211")){
            responseinfo = "Train doesn’t have journey class queried"
        }else if(toString.equals("220")){
            responseinfo = "Flushed PNR"
        }else if(toString.equals("221")){
            responseinfo = "Invalid PNR"
        }else if(toString.equals("230")){
            responseinfo = "Date chosen for the query is not valid for the chosen parameters"
        }else if(toString.equals("404")){
            responseinfo = "Data couldn’t be loaded on our servers. No data available"
        }else if(toString.equals("405")){
            responseinfo = "Data couldn’t be loaded on our servers. Request couldn’t go through"
        }else if(toString.equals("500")){
            responseinfo = "Unauthorized API Key"
        }else if(toString.equals("501")){
            responseinfo = "Account Expired"
        }else{
            responseinfo = "Success"
        }
        return responseinfo
    }
}
class MyHolder:RecyclerView.ViewHolder
{
    var trainbwstn_card:CardView? = null
    var train_name:TextView? = null
    var train_number:TextView? = null
    var from_stn_code:TextView? = null
    var from_stn_name:TextView? = null
    var to_stn_code:TextView? = null
    var to_stn_name:TextView? = null
    var traveltime:TextView? = null
    var src_dept_time:TextView? = null
    var dest_ariv_time:TextView? = null

    var sunday:TextView? = null
    var monday:TextView? = null
    var tuesday:TextView? = null
    var wednesday:TextView? = null
    var thursday:TextView? = null
    var friday:TextView? = null
    var saturday:TextView? = null

    constructor(view: View):super(view)
    {
        trainbwstn_card = view.trainbwstations_cardview
        train_name = view.trainbwstations_train_name
        train_number = view.trainbwstations_train_number
        from_stn_code = view.trainbwstations_from_stn_code
        from_stn_name = view.trainbwstations_from_stn_name
        to_stn_code = view.trainbwstations_dest_code
        to_stn_name = view.trainbwstations_dest_name
        traveltime = view.trainbwstations_travel_time
        src_dept_time = view.trainbwstations_src_dep_time
        dest_ariv_time = view.trainbwstations_dest_arrv_time

        sunday = view.trainbwstations_sun
        monday = view.trainbwstations_mon
        tuesday = view.trainbwstations_tue
        wednesday = view.trainbwstations_wed
        thursday = view.trainbwstations_thur
        friday = view.trainbwstations_fri
        saturday = view.trainbwstations_sat
    }

}