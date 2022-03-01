package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime
import kotlinx.coroutines.launch

class MainViewModel(application: Application): BaseViewModel(application){

    val lessonList = MutableLiveData<List<ModelLesson>>()
    val lessonTimeList = MutableLiveData<List<ModelTime>>()
    val error = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val empty = MutableLiveData<Boolean>()



    fun refreshData(){
        getDataFromSQLite()
    }

    fun getDataFromSQLite(){
        loading.value = true
        launch {
            val lessons = DatabaseTimeLine(getApplication()).lessonDAO().getAllLessons("progID01")
            val lessonsTimes = DatabaseTimeLine(getApplication()).timeDAO().getAllTime("progID01")
            showDataInUI(lessons, lessonsTimes)
        }
    }


    fun showDataInUI(lessons : List<ModelLesson>, lessonsTimes: List<ModelTime>){

        lessonTimeList.value = lessonsTimes
        lessonList.value = lessons
        error.value = false
        loading.value = false
        empty.value = false
    }



}