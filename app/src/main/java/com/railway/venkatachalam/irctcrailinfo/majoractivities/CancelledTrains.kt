package com.railway.venkatachalam.irctcrailinfo.majoractivities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentTransaction
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.*
import com.railway.venkatachalam.irctcrailinfo.Interfaces.RailwayAPI
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.cancelledtrains.CancelledBean
import com.railway.venkatachalam.irctcrailinfo.broadcast.CustomBroadcast
import com.railway.venkatachalam.irctcrailinfo.fragments.CancelledTrainsFragment
import com.railway.venkatachalam.irctcrailinfo.fragments.FirstFragment
import kotlinx.android.synthetic.main.activity_cancelled_trains.*
import kotlinx.android.synthetic.main.activity_trainbw_stations.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class CancelledTrains : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
    var cal:Calendar = Calendar.getInstance()
    var resp_obj:CancelledBean? = null
    var broadcastReceiver: CustomBroadcast = CustomBroadcast()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancelled_trains)
        MobileAds.initialize(this@CancelledTrains,resources.getString(R.string.appUnitID))

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = resources.getString(R.string.InterstitialAdId)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        var progressDialog = ProgressDialog(this@CancelledTrains)
        progressDialog.setTitle("Loading...!")
        progressDialog.isIndeterminate=true
        progressDialog.setMessage("Please wait to fetch your requested data")
        progressDialog.setCancelable(false)

        var fm = supportFragmentManager
        var tx = fm.beginTransaction()
        var firstFragment = FirstFragment()
        tx.add(R.id.cancelled_train_fragment,firstFragment)
        tx.commit()

        var calen: Calendar = Calendar.getInstance()
        cancel_date.setText(dateFormat.format(calen.time))

        today.setOnClickListener({
            var calen: Calendar = Calendar.getInstance()
            cancel_date.setText(dateFormat.format(calen.time))
        })

        tomorrow.setOnClickListener({
            var calen: Calendar = Calendar.getInstance()
            calen.add(Calendar.DAY_OF_YEAR,1)
            cancel_date.setText(dateFormat.format(calen.time))
        })

        cancel_date.setOnClickListener({

            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year:Int, monthOfYear:Int, dayOfMonth:Int ->
                // Display Selected date in textbox
                cal.set(year,monthOfYear,dayOfMonth)
                cancel_date.setText(dateFormat.format(cal.time))
            }, year, month, day)
            dpd.show()
        })

        cancelled_train.setOnClickListener({
        progressDialog.show()
            var retro = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://api.railwayapi.com/").build()
            var api = retro.create(RailwayAPI::class.java)
            var call = api.getcancelledtrains(cancel_date.text.toString())
            call.enqueue(object :Callback<CancelledBean>{
                override fun onFailure(call: Call<CancelledBean>?, t: Throwable?) {
                    progressDialog.dismiss()
                    Snackbar.make(it,"Failure Cause "+t!!.message, Snackbar.LENGTH_LONG).show()                }

                override fun onResponse(call: Call<CancelledBean>?, response: Response<CancelledBean>?) {
                    progressDialog.dismiss()
                    resp_obj = response!!.body()

                    if(resp_obj!!.responseCode == 200) {
                        var cancelledTrainsFragment = CancelledTrainsFragment()
                        cancelledTrainsFragment.setCancelledData(applicationContext, resp_obj)
                        var fm = supportFragmentManager
                        var tx: FragmentTransaction = fm.beginTransaction()
                        tx.replace(R.id.cancelled_train_fragment, cancelledTrainsFragment)
                        tx.commit()
                    }else
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

        var myDialog = BottomSheetDialog(this@CancelledTrains)
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
