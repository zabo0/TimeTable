package com.saboon.timetable.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime

class DetailsViewModel: ViewModel() {

    val lessonName =MutableLiveData<String>()
    val lecturerName = MutableLiveData<String>()
    val programTimes = MutableLiveData<List<ModelTime>?>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()
    val empty = MutableLiveData<Boolean>()

    fun refreshData(){

        val timeProg = ModelTime("id0","monday","09:00","10:00","ders","203","5 minute ago","idlessonManth")
        val timeProg2 = ModelTime("id1","saturday","09:00","10:00","ders","203","5 minute ago","idlessonManth")

        val timeProgList = arrayListOf(timeProg,timeProg2)

        val lesson = ModelLesson("id","dateAdded","matematik", "ali","mavi","2","idprogName")


        lessonName.value = lesson.lessonName!!
        lecturerName.value = lesson.lecturerName!!
        programTimes.value = timeProgList!!
        loading.value = false
        error.value = false
        empty.value = false


    }




}