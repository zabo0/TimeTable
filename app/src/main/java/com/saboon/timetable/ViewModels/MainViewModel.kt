package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime
import kotlinx.coroutines.launch

class MainViewModel(application: Application): BaseViewModel(application){

    val programName = MutableLiveData<String?>()
    val lessonList = MutableLiveData<List<ModelLesson>?>()
    val lessonTimeList = MutableLiveData<List<ModelTime>?>()
    val error = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val empty = MutableLiveData<Boolean>()



    fun refreshData(progID: String){
        getProgName(progID)
        getDataFromSQLite(progID)
    }

    fun getDataFromSQLite(progID: String){
        loading.value = true
        launch {
            val lessons = DatabaseTimeLine(getApplication()).lessonDAO().getAllLessons(progID)
            val lessonsTimes = DatabaseTimeLine(getApplication()).timeDAO().getAllTime(progID)
            showDataInUI(lessons, lessonsTimes)
        }
    }

    fun getProgName(progID: String){
        launch {
            val progName = DatabaseTimeLine(getApplication()).programDAO().getProgramName(progID)
            programName.value = progName
        }

    }


    fun showDataInUI(lessons : List<ModelLesson>?, lessonsTimes: List<ModelTime>?){

        if(lessons.isNullOrEmpty()){
            empty.value = true
        }else{
            lessonList.value = lessons
            if (lessonsTimes.isNullOrEmpty()){
                empty.value = true
            }else{
                lessonTimeList.value = lessonsTimes
                error.value = false
                loading.value = false
                empty.value = false
            }
        }

    }



}