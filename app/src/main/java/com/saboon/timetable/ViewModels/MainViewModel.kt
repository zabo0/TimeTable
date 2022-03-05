package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime
import kotlinx.coroutines.launch

class MainViewModel(application: Application): BaseViewModel(application){

    val lessonList = MutableLiveData<List<ModelLesson>?>()
    val lessonTimeList = MutableLiveData<List<ModelTime>?>()
    val error = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val empty = MutableLiveData<Boolean>()



    fun refreshData(belowProgID: String){
        getDataFromSQLite(belowProgID)
    }

    fun getDataFromSQLite(belowProgID: String){
        loading.value = true
        launch {
            //val lessons = DatabaseTimeLine(getApplication()).lessonDAO().getAllLessons(belowProgID)
            //val lessonsTimes = DatabaseTimeLine(getApplication()).timeDAO().getAllTime(belowProgID)
            showDataInUI(null, null)
        }
    }


    fun showDataInUI(lessons : List<ModelLesson>?, lessonsTimes: List<ModelTime>?){
        if (lessons != null){
            lessonList.value = lessons
        }
        if (lessonsTimes != null){
            lessonTimeList.value = lessonsTimes
        }
        error.value = false
        loading.value = false
        empty.value = true
    }



}