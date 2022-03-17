package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelTime
import kotlinx.coroutines.launch
import java.lang.Exception

class AddTimeViewModel(application: Application): BaseViewModel(application) {

    val timeProg = MutableLiveData<ModelTime?>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()


    fun storeTimeInDatabase(timeProg: ModelTime){
        launch {
            DatabaseTimeLine(getApplication()).timeDAO().insertTime(timeProg)
        }

    }

    fun getTimeFromSQLite(timeID: String, response:(ModelTime) -> Unit){
        launch {
            val lesson = DatabaseTimeLine(getApplication()).timeDAO().getTime(timeID)
            response(lesson)
        }
    }

    fun deleteTime(timeID:String){
        launch {
            DatabaseTimeLine(getApplication()).timeDAO().deleteTime(timeID)
        }
    }

    fun updateTime(time: ModelTime, response: (Boolean)-> Unit){
        try {
            launch {
                DatabaseTimeLine(getApplication()).timeDAO().updateTime(
                    time.id,
                    time.day!!,
                    time.timeStart!!,
                    time.timeFinish!!,
                    time.typeOfLesson!!,
                    time.classRoom!!,
                    time.reminderTime!!,
                    time.belowLesson,
                    time.belowProgram
                )
            }
            response(true)
        }catch (error: Exception){
            response(false)
            throw error
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
        if (prog != null){
            timeProg.value = prog
            loading.value = false
            error.value = false
        }else{
            loading.value = false
            error.value = true
        }
    }




}