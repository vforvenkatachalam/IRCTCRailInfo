package com.railway.venkatachalam.irctcrailinfo.majoractivities
import android.app.ProgressDialog
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentTransaction
import android.widget.TextView
import com.google.android.gms.ads.*
import com.railway.venkatachalam.irctcrailinfo.Interfaces.RailwayAPI
import com.railway.venkatachalam.irctcrailinfo.R
import com.railway.venkatachalam.irctcrailinfo.beans.pnrstatus.PnrStatusBean
import com.railway.venkatachalam.irctcrailinfo.broadcast.CustomBroadcast
import com.railway.venkatachalam.irctcrailinfo.fragments.FirstFragment
import com.railway.venkatachalam.irctcrailinfo.fragments.PnrStatusFragment
import kotlinx.android.synthetic.main.activity_pnr_status.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PnrStatus : AppCompatActivity() {
    var pnrnumber:String? = null
    private lateinit var mInterstitialAd: InterstitialAd
    var broadcastReceiver: CustomBroadcast = CustomBroadcast()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pnr_status)
        MobileAds.initialize(this@PnrStatus,resources.getString(R.string.appUnitID))

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
        tx.add(R.id.pnr_status_fragment,firstFragment)
        tx.commit()


        var progressDialog = ProgressDialog(this@PnrStatus)
        progressDialog.setTitle("Loading...!")
        progressDialog.isIndeterminate=true
        progressDialog.setMessage("Please wait to fetch your requested data")
        progressDialog.setCancelable(false)

        get_status.setOnClickListener({
            progressDialog.show()
            //Getting data from retrofit
            var retro = Retrofit.Builder().baseUrl("https://api.railwayapi.com/").addConverterFactory(GsonConverterFactory.create()).build()
            var api = retro.create(RailwayAPI::class.java)

            pnrnumber = pnr_number.text.toString()

            if(pnrnumber != "")
            {
                var call = api.getpnrstatus(pnr_number.text.toString())
                call!!.enqueue(object : Callback<PnrStatusBean> {
                    override fun onFailure(call: Call<PnrStatusBean>?, t: Throwable?) {
                        progressDialog.dismiss()
                        Snackbar.make(it,"Failure Cause "+t!!.message, Snackbar.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<PnrStatusBean>?, response: Response<PnrStatusBean>?) {
                        progressDialog.dismiss()
                        if (response!!.body()!!.responseCode == 200)
                        {
                            var pnrStatusFragment: PnrStatusFragment? = PnrStatusFragment()
                            pnrStatusFragment!!.setPnrStatusFragment(response!!.body()!!, applicationContext)
                            var tx: FragmentTransaction = fm.beginTransaction()
                            tx.replace(R.id.pnr_status_fragment, pnrStatusFragment!!)
                            tx.commit()
                        } else {
                            dialogshow(response.body()!!.responseCode.toString())
                        }
                    }
                })
            }else{
                pnr_number.setError("Enter PNR")
            }
        })
    }

    override fun onBackPressed() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            //Log.d("TAG", "The interstitial wasn't loaded yet.")
        }
        super.onBackPressed()
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

        var myDialog = BottomSheetDialog(this@PnrStatus)
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