package com.railway.venkatachalam.irctcrailinfo.beans.livetrainstatus

import com.google.gson.annotations.SerializedName

data class Train(@SerializedName("number")
                 val number: String = "",
                 @SerializedName("classes")
                 val classes: List<ClassesItem>?,
                 @SerializedName("name")
                 val name: String = "",
                 @SerializedName("days")
                 val days: List<DaysItem>?)