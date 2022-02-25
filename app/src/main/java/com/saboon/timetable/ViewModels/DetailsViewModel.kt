package com.saboon.timetable.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime

class DetailsViewModel: ViewModel() {

    val lessonName =MutableLiveData<String>()
    val lecturerName = MutableLiveData<String>()
    val classRoom = MutableLiveData<String>()
    val programTimes = MutableLiveData<List<ModelTime>?>()
    val loading = MutableLiveData<Boolean>()

    fun refreshData(){

        val timeProg = ModelTime("id0","pazartesi","09:00","10:00","ders","5 minute ago")
        val timeProg2 = ModelTime("id1","sali","09:00","10:00","ders","5 minute ago")
        val timeProgList = arrayListOf(timeProg,timeProg2)
        val lesson = ModelLesson("id","dateAdded","matematik","203", timeProgList,"ali","mavi","2")


        lessonName.value = lesson.lessonName
        lecturerName.value = lesson.lecturerName!!
        classRoom.value = lesson.classRoom
        programTimes.value = lesson.timeProgram
        loading.value = false


    }




}