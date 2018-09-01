package com.railway.venkatachalam.irctcrailinfo.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.railway.venkatachalam.irctcrailinfo.R

class FirstFragment:Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:View = inflater.inflate(R.layout.firstfragment,container,false)
        return view
    }
}