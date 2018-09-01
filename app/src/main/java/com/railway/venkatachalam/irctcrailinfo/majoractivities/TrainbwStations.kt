package com.railway.venkatachalam.irctcrailinfo.majoractivities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentTransaction
import android.widget.*
import com.google.android.gms.ads.*
import com.railway.venkatachalam.irctcrailinfo.Interfaces.RailwayAPI
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.trainbwstations.TrainBetweenStationsBean
import com.railway.venkatachalam.irctcrailinfo.broadcast.CustomBroadcast
import com.railway.venkatachalam.irctcrailinfo.fragments.FirstFragment
import com.railway.venkatachalam.irctcrailinfo.fragments.TrainBetweenStationFragment
import kotlinx.android.synthetic.main.activity_trainbw_stations.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class TrainbwStations : AppCompatActivity() {

    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
    var cal: Calendar = Calendar.getInstance()
    var responseobj:TrainBetweenStationsBean? = null
    var fromStn: AutoCompleteTextView? = null
    var toStn: AutoCompleteTextView? = null
    var fromStation:String? = null
    var toStation:String? = null
    var broadcastReceiver: CustomBroadcast = CustomBroadcast()
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainbw_stations)
        MobileAds.initialize(this@TrainbwStations,resources.getString(R.string.appUnitID))

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = resources.getString(R.string.InterstitialAdId)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

       var progressDial = ProgressDialog(this@TrainbwStations)

        var fm = supportFragmentManager
        var tx = fm.beginTransaction()
        var firstFragment = FirstFragment()
        tx.add(R.id.trainbwstationsfrag,firstFragment)
        tx.commit()

        var progressDialog = ProgressDialog(this@TrainbwStations)
        progressDialog.setTitle("Loading...!")
        progressDialog.isIndeterminate=true
        progressDialog.setMessage("Please wait to fetch your requested data")
        progressDialog.setCancelable(false)

        var calen: Calendar = Calendar.getInstance()
        trainbwstations_date.setText(dateFormat.format(calen.time))

        trainbwstations_today.setOnClickListener({
            var calen: Calendar = Calendar.getInstance()
            trainbwstations_date.setText(dateFormat.format(calen.time))
        })

        trainbwstations_tomorrow.setOnClickListener({
            var calen: Calendar = Calendar.getInstance()
            calen.add(Calendar.DAY_OF_YEAR,1)
            trainbwstations_date.setText(dateFormat.format(calen.time))
        })

        trainbwstations_date.setOnClickListener({

            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year:Int, monthOfYear:Int, dayOfMonth:Int ->
                // Display Selected date in textbox
                cal.set(year,monthOfYear,dayOfMonth)
                trainbwstations_date.setText(dateFormat.format(cal.time))
            }, year, month, day)
            dpd.show()
        })

        fromStn = findViewById<AutoCompleteTextView>(R.id.from_station)
        toStn = findViewById<AutoCompleteTextView>(R.id.to_station)

        var adapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,resources.getStringArray(R.array.stations))

        fromStn!!.setAdapter(adapter)
        fromStn!!.threshold = 1

        toStn!!.setAdapter(adapter)
        toStn!!.threshold = 1

        fromStn!!.onItemClickListener = AdapterView.OnItemClickListener({
            parent,view,position,id->
            var str: String = fromStn!!.text.toString()
            fromStation = str.split(" ")[0]
        })

        toStn!!.onItemClickListener = AdapterView.OnItemClickListener({
            parent,view,position,id->
            var str: String = toStn!!.text.toString()
            toStation = str.split(" ")[0]
        })

        get_train_bw_stn.setOnClickListener({
            progressDialog.show()
            var retro = Retrofit.Builder().baseUrl("https://api.railwayapi.com/").addConverterFactory(GsonConverterFactory.create()).build()
            var api = retro.create(RailwayAPI::class.java)

            if(fromStn!!.text.isEmpty() || toStn!!.text.isEmpty()) {
                if(fromStn!!.text.isEmpty())
                {
                    fromStn!!.setError("Enter Source")
                }else{
                    toStn!!.setError("Enter Destination")
                }
            }else
            {
                var call = api.gettrainbetweenstations(fromStation!!,toStation!!,trainbwstations_date.text.toString())
                call.enqueue(object : Callback<TrainBetweenStationsBean> {
                    override fun onFailure(call: Call<TrainBetweenStationsBean>?, t: Throwable?) {
                        progressDialog.dismiss()
                        Snackbar.make(it,"Failure Cause "+t!!.message,Snackbar.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<TrainBetweenStationsBean>?, response: Response<TrainBetweenStationsBean>?) {
                        progressDialog.dismiss()
                        responseobj = response!!.body()

                        if (responseobj!!.responseCode == 200) {
                            var trainBetweenStationFragment = TrainBetweenStationFragment()
                            trainBetweenStationFragment.setTrainData(responseobj, applicationContext,trainbwstations_date.text.toString(),fm,progressDial)
                            tx = fm.beginTransaction()
                            tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            tx.replace(R.id.trainbwstationsfrag, trainBetweenStationFragment)
                            tx.commit()
                        } else
                            dialogshow(response.body()!!.responseCode.toString())
                    }
                })
            }
        })
    }
    override fun onStart() {
        var intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(broadcastReceiver,intentFilter)
        super.onStart()
    }
    override fun onStop() {
        unregisterReceiver(broadcastReceiver)
        super.onStop()
    }
    override fun onBackPressed() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            //Log.d("TAG", "The interstitial wasn't loaded yet.")
        }
        super.onBackPressed()
    }

    fun dialogshow(toString: String)
    {
        var responseinfo:String?=null

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

        var myDialog = BottomSheetDialog(this@TrainbwStations)
        myDialog.setTitle("Testing Dialog")
        myDialog.setContentView(R.layout.customdialog)
        myDialog.setCancelable(true)
        myDialog.findViewById<TextView>(R.id.custom_title)!!.setText("Error")
        myDialog.findViewById<TextView>(R.id.custom_message)!!.setText(responseinfo)
        myDialog.findViewById<TextView>(R.id.custom_ok)!!.setOnClickListener({
            myDialog.dismiss()
        })
        myDialog.show()
    }
}
