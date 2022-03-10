package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelTime
import kotlinx.coroutines.launch

class AddTimeViewModel(application: Application): BaseViewModel(application) {

    val timeProg = MutableLiveData<ModelTime>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()


    fun storeTimeInDatabase(timeProg: ModelTime){
        launch {
            DatabaseTimeLine(getApplication()).timeDAO().insertTime(timeProg)
        }

    }


    fun getDataFromSQLite(timeID: String){
        loading.value = true
        launch {
            val time = DatabaseTimeLine(getApplication()).timeDAO().getTime(timeID)
            showDataInUI(time)
        }
    }

    fun showDataInUI(prog: ModelTime?){
        prog.let {
            timeProg.value = it
            loading.value = false
            error.value = false
        }?: run {
            loading.value = false
            error.value = true
        }
    }




}