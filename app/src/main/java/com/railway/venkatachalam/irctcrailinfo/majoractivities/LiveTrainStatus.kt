package com.railway.venkatachalam.irctcrailinfo.majoractivities

import android.app.ProgressDialog
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.railway.venkatachalam.irctcrailinfo.Interfaces.RailwayAPI
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.livetrainstatus.LiveTrainStatusBean
import com.railway.venkatachalam.irctcrailinfo.broadcast.CustomBroadcast
import com.railway.venkatachalam.irctcrailinfo.fragments.FirstFragment
import com.railway.venkatachalam.irctcrailinfo.fragments.LiveTrainStatusFragment
import kotlinx.android.synthetic.main.activity_live_train_status.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LiveTrainStatus : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    var broadcastReceiver: CustomBroadcast = CustomBroadcast()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_train_status)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = resources.getString(R.string.InterstitialAdId)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        var fm = supportFragmentManager
        var tx = fm.beginTransaction()
        var firstFragment = FirstFragment()
        tx.add(R.id.live_train_fragment,firstFragment)
        tx.commit()

        var progressDialog = ProgressDialog(this@LiveTrainStatus)
        progressDialog.setTitle("Loading...!")
        progressDialog.isIndeterminate=true
        progressDialog.setMessage("Please wait to fetch your requested data")
        progressDialog.setCancelable(false)

        live_train_status.setOnClickListener({

            progressDialog.show()
            var retro = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://api.railwayapi.com/").build()
            var api = retro.create(RailwayAPI::class.java)
            var call = api.getLiveTrainStatus()

            call.enqueue(object:Callback<LiveTrainStatusBean>{
                override fun onFailure(call: Call<LiveTrainStatusBean>?, t: Throwable?) {
                    progressDialog.dismiss()
                    Snackbar.make(it,"Failure Cause "+t!!.message, Snackbar.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<LiveTrainStatusBean>?, response: Response<LiveTrainStatusBean>?) {
                    progressDialog.dismiss()
                    if(response!!.body()!!.responseCode == 200) {
                        var liveTrainStatusFragment = LiveTrainStatusFragment()
                        liveTrainStatusFragment.setLiveTrainStatus(applicationContext, response!!.body())
                        tx = fm.beginTransaction()
                        tx.replace(R.id.live_train_fragment, liveTrainStatusFragment)
                        tx.commit()
                    }
                    else
                        dialogshow(response.body()!!.responseCode.toString())
                }
            })
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

        var myDialog = BottomSheetDialog(this@LiveTrainStatus)
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
