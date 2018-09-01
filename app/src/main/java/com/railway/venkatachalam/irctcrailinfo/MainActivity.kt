package com.railway.venkatachalam.irctcrailinfo

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.railway.venkatachalam.irctcrailinfo.broadcast.CustomBroadcast
import com.railway.venkatachalam.irctcrailinfo.majoractivities.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener{


    var broadcastReceiver:CustomBroadcast = CustomBroadcast()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pnr.setOnClickListener(this)
        trainbwstations.setOnClickListener(this)
        seatavail.setOnClickListener(this)
        farenquiry.setOnClickListener(this)
        livetrainstatus.setOnClickListener(this)
        rescheduletrains.setOnClickListener(this)
        cancelledtrains.setOnClickListener(this)
        trainarrivals.setOnClickListener(this)
        trainroute.setOnClickListener(this)

    }

    override fun onBackPressed() {
        var dialogBuilder = AlertDialog.Builder(this).setTitle("Exit !")
                .setCancelable(false)
                .setMessage("Are you sure wanna leave ?")
                .setPositiveButton("Yes",DialogInterface.OnClickListener({
                    dialog, id -> finish()
                }))
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
                })
        var alert = dialogBuilder.create()
        alert.show()
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

    override fun onClick(p0: View?) {
        if(p0!!.id == R.id.pnr)
        {
            var intent = Intent(this@MainActivity,PnrStatus::class.java)
            startActivity(intent)
        }
        if(p0.id == R.id.trainbwstations)
        {
            var intent = Intent(this@MainActivity,TrainbwStations::class.java)
            startActivity(intent)
        }
        if(p0.id == R.id.seatavail)
        {
            var intent = Intent(this@MainActivity,SeatAvailability::class.java)
            startActivity(intent)
        }
        if(p0.id == R.id.farenquiry)
        {
            var intent = Intent(this@MainActivity,FareEnquiry::class.java)
            startActivity(intent)
        }
        if(p0.id == R.id.livetrainstatus)
        {
            Snackbar.make(p0,"Restricted",Snackbar.LENGTH_LONG).show()
            //var intent = Intent(this@MainActivity,LiveTrainStatus::class.java)
            //startActivity(intent)
        }
        if(p0.id == R.id.rescheduletrains)
        {
            var intent = Intent(this@MainActivity,RescheduleTrains::class.java)
            startActivity(intent)
        }
        if(p0.id == R.id.cancelledtrains)
        {
            var intent = Intent(this@MainActivity,CancelledTrains::class.java)
            startActivity(intent)
        }
        if(p0.id == R.id.trainarrivals)
        {
            var intent = Intent(this@MainActivity,TrainArrivals::class.java)
            startActivity(intent)
        }
        if(p0.id == R.id.trainroute)
        {
            var intent = Intent(this@MainActivity,TrainRoute::class.java)
            startActivity(intent)
        }
    }

}
