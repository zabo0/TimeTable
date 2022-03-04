package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelTime
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

    fun storeDataInSQLite(timeProg: ModelTime){

        launch {

            // TODO: databasede sorun var
            DatabaseTimeLine(getApplication()).timeDAO().insertTime(timeProg)
        }

    }


    fun getDataFromSQLite(){
        launch {

        }
    }

    fun showDataInUI(prog: ModelTime){
        timeProg.value = prog
    }




}