package com.railway.venkatachalam.irctcrailinfo.majoractivities

import android.app.ProgressDialog
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.widget.CircularProgressDrawable
import android.widget.*
import com.google.android.gms.ads.*
import com.railway.venkatachalam.irctcrailinfo.Interfaces.RailwayAPI
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.R.drawable.ic_train_black_24dp
import com.railway.venkatachalam.irctcrailinfo.beans.trainarrivals.TrainArrivalsBean
import com.railway.venkatachalam.irctcrailinfo.broadcast.CustomBroadcast
import com.railway.venkatachalam.irctcrailinfo.fragments.FirstFragment
import com.railway.venkatachalam.irctcrailinfo.fragments.TrainArrivalStatusFragment
import kotlinx.android.synthetic.main.activity_train_arrivals.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrainArrivals : AppCompatActivity() {
    var trainCode:String? = null
    var hours:String? = "2"
    private lateinit var mInterstitialAd: InterstitialAd
    var broadcastReceiver: CustomBroadcast = CustomBroadcast()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_arrivals)
        MobileAds.initialize(this@TrainArrivals,resources.getString(R.string.appUnitID))

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
        tx.add(R.id.train_arrival_status_fragment,firstFragment)
        tx.commit()

        var progressDialog = ProgressDialog(this@TrainArrivals)
        progressDialog.setTitle("Loading...!")
        progressDialog.isIndeterminate=true
        progressDialog.setMessage("Please wait to fetch your requested data")
        progressDialog.setCancelable(false)

        var autoCompleteTextView:AutoCompleteTextView? = null
        autoCompleteTextView = findViewById(R.id.train_arrival_stn_code)
        var adapter:ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,resources.getStringArray(R.array.stations))
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.threshold = 1


        autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener({
            parent,view,position,id->
            var str: String = autoCompleteTextView.text.toString()
            trainCode = str.split(" ")[0]
            hours = spinner_hours.selectedItem.toString().split(" ")[0]
            //Toast.makeText(this@TrainArrivals,hours,Toast.LENGTH_SHORT).show()
        })

        get_status.setOnClickListener({
            progressDialog.show()
            var retro = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://api.railwayapi.com/").build()
            var api = retro.create(RailwayAPI::class.java)

            if(autoCompleteTextView.text.isEmpty())
            {
                autoCompleteTextView.setError("Enter Station Code")
            }else
            {
                var call = api.getTrainArrivals(trainCode!!, hours!!)
                call.enqueue(object : Callback<TrainArrivalsBean> {
                    override fun onFailure(call: Call<TrainArrivalsBean>?, t: Throwable?) {
                        Snackbar.make(it,"Failure Cause "+t!!.message, Snackbar.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }

                    override fun onResponse(call: Call<TrainArrivalsBean>?, response: Response<TrainArrivalsBean>?) {
                        progressDialog.dismiss()
                        if (response!!.body()!!.responseCode == 200) {
                            var trainArrivals = TrainArrivalStatusFragment()
                            trainArrivals.setTrainArrivals(applicationContext, response!!.body())
                            var fm = supportFragmentManager
                            var tx = fm.beginTransaction()
                            tx.replace(R.id.train_arrival_status_fragment, trainArrivals)
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

        var myDialog = BottomSheetDialog(this@TrainArrivals)
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