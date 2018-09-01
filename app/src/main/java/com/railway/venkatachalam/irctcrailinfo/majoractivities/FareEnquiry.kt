package com.railway.venkatachalam.irctcrailinfo.majoractivities

import android.app.ProgressDialog
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.gms.ads.*
import com.railway.venkatachalam.irctcrailinfo.Interfaces.RailwayAPI
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.fareenquiry.FareEnquiryBean
import com.railway.venkatachalam.irctcrailinfo.broadcast.CustomBroadcast
import com.railway.venkatachalam.irctcrailinfo.fragments.FareEnquiryFragment
import com.railway.venkatachalam.irctcrailinfo.fragments.FirstFragment
import kotlinx.android.synthetic.main.activity_fare_enquiry.*
import kotlinx.android.synthetic.main.farenquiryfragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class FareEnquiry : AppCompatActivity() {

    var dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
    var trainNumb:String? = null
    var fromStation:String? = null
    var toStation:String? = null
    var quota:String? = null
    var classcode:String? = null
    var broadcastReceiver: CustomBroadcast = CustomBroadcast()
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fare_enquiry)
        MobileAds.initialize(this@FareEnquiry,resources.getString(R.string.appUnitID))

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = resources.getString(R.string.InterstitialAdId)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }

        var calen: Calendar = Calendar.getInstance()
        var faredate:String = dateFormat.format(calen.time)

        var progressDialog = ProgressDialog(this@FareEnquiry)
        progressDialog.setTitle("Loading...!")
        progressDialog.isIndeterminate=true
        progressDialog.setMessage("Please wait to fetch your requested data")
        progressDialog.setCancelable(false)

        var fm = supportFragmentManager
        var tx = fm.beginTransaction()
        var firstFragment = FirstFragment()
        tx.add(R.id.fare_enquiry_fragment,firstFragment)
        tx.commit()

        var trainadapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,resources.getStringArray(R.array.trains))
        var adapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,resources.getStringArray(R.array.stations))

        fare_trainumber!!.setAdapter(trainadapter)
        fare_trainumber!!.threshold = 1

        fare_fromstn!!.setAdapter(adapter)
        fare_fromstn!!.threshold = 1

        fare_tostn!!.setAdapter(adapter)
        fare_tostn!!.threshold = 1

        fare_trainumber!!.onItemClickListener = AdapterView.OnItemClickListener({
            parent,view,position,id->
            var str: String = fare_trainumber!!.text.toString()
            trainNumb = str.split(" ")[0]
            //Toast.makeText(applicationContext,fare_quota.selectedItem.toString().split(" ")[0],Toast.LENGTH_SHORT).show()
        })

        fare_fromstn!!.onItemClickListener = AdapterView.OnItemClickListener({
            parent,view,position,id->
            var str: String = fare_fromstn!!.text.toString()
            fromStation = str.split(" ")[0]
            //Toast.makeText(applicationContext,fare_quota.selectedItem.toString().split(" ")[0],Toast.LENGTH_SHORT).show()
        })

        fare_tostn!!.onItemClickListener = AdapterView.OnItemClickListener({
            parent,view,position,id->
            var str: String = fare_tostn!!.text.toString()
            toStation = str.split(" ")[0]
            //Toast.makeText(applicationContext,fare_pref.selectedItem.toString().split(" ")[0],Toast.LENGTH_SHORT).show()
        })

        fare_get_status.setOnClickListener({
        progressDialog.show()
            quota = fare_quota.selectedItem.toString().split(" ")[0]
            classcode = fare_pref.selectedItem.toString().split(" ")[0]

            var retro = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://api.railwayapi.com/").build()
            var api = retro.create(RailwayAPI::class.java)

            if(fare_trainumber.text.isEmpty() || fare_fromstn.text.isEmpty() || fare_tostn.text.isEmpty())
            {
                if(fare_trainumber.text.isEmpty()){
                    fare_train_number.setError("Enter Train Number")
                }else if(fare_fromstn.text.isEmpty()){
                    fare_fromstn.setError("Enter Source")
                }else{
                    fare_tostn.setError("Enter Destination")
                }
            }else
            {
                //var call = api.getFare(trainNumb,fromStation,toStation,classcode,quota)
                var call = api.getFare(quota,classcode,trainNumb,fromStation,toStation,faredate)
                Log.i(" values : "," train numb : "+trainNumb+" frmstn : "+fromStation+" tostn : "+toStation+" class code : "+classcode+" quota : "+quota)

                call.enqueue(object : Callback<FareEnquiryBean> {
                    override fun onFailure(call: Call<FareEnquiryBean>?, t: Throwable?) {
                        progressDialog.dismiss()
                        Snackbar.make(it,"Failure Cause "+t!!.message, Snackbar.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<FareEnquiryBean>?, response: Response<FareEnquiryBean>?) {
                        progressDialog.dismiss()
                        if (response!!.body()!!.responseCode == 200) {
                            var fareEnquiryFragment = FareEnquiryFragment()
                            fareEnquiryFragment.setFareEnquiryInfo(applicationContext, response!!.body())
                            var fm = supportFragmentManager
                            var tx = fm.beginTransaction()
                            tx.replace(R.id.fare_enquiry_fragment, fareEnquiryFragment)
                            tx.commit()
                        } else
                            dialogshow(response.body()!!.responseCode.toString())                    }
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

        var myDialog = BottomSheetDialog(this@FareEnquiry)
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