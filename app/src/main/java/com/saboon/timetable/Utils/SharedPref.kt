package com.saboon.timetable.Utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPref(activity: Activity) {

    private val SHARED_PREF_PROG_ID = "progID"
    private var sharedPref: SharedPreferences

    init {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    }

    fun saveIDToSharedPref(currentProgID: String){
        sharedPref.edit()
            .putString(SHARED_PREF_PROG_ID, currentProgID)
            .apply()
    }

    fun getIDFromSharedPref(response: (String) -> Unit ){
        val id = sharedPref.getString(SHARED_PREF_PROG_ID, null)

        if (id == null || id == "null"){
            response("null")
        }else{
            response(id)
        }
    }

}