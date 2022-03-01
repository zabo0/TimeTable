package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saboon.timetable.Models.ModelTime
import com.saboon.timetable.Utils.*
import kotlinx.coroutines.launch

class AddProgViewModel(application: Application): BaseViewModel(application) {

    val timeProg = MutableLiveData<ModelTime>()
    val whichDay = MutableLiveData<String?>()
    val classRoom = MutableLiveData<String?>()
    val startTime = MutableLiveData<String?>()
    val finishTime = MutableLiveData<String?>()
    val typeLesson =  MutableLiveData<String?>()
    val reminder = MutableLiveData<String?>()


    fun refreshData(){

    }

    fun getDataFromSQLite(){
        launch {

        }
    }

    fun showDataInUI(prog: ModelTime){
        timeProg.value = prog
    }




}