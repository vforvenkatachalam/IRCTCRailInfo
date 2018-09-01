package com.railway.venkatachalam.irctcrailinfo.broadcast

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class CustomBroadcast:BroadcastReceiver() {

    var alertDialog:AlertDialog? = null
    override fun onReceive(context: Context, arg1: Intent) {
        var dialogBuilder = AlertDialog.Builder(context).setTitle("Network Error !")
                .setCancelable(false)
                .setMessage("Check your internet connection...")

            if (isConnectedOrConnecting(context)) {
                if(alertDialog!=null && alertDialog!!.isShowing) {
                    alertDialog!!.dismiss()
                }
            }else{
                alertDialog = dialogBuilder.create()
                alertDialog!!.show()
            }
    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}