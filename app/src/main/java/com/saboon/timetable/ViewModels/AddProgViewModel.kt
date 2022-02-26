package com.saboon.timetable.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime

class AddProgViewModel: ViewModel() {

    val whichDay = MutableLiveData<String?>()
    val classRoom = MutableLiveData<String?>()
    val startTime = MutableLiveData<String?>()
    val finishTime = MutableLiveData<String?>()
    val typeLesson =  MutableLiveData<String?>()
    val reminder = MutableLiveData<String?>()



    fun refreshData(id: String){
        val timeProg = ModelTime("id0","monday","09:00","10:00","ders",null,"5 minute ago")
        val timeProg2 = ModelTime("id1","saturday","09:00","10:00","ders",null,"5 minute ago")

        val timeProgList = arrayListOf(timeProg,timeProg2)

        val lesson = ModelLesson("id","dateAdded","matematik", timeProgList,"ali","mavi","2")

        whichDay.value = timeProg.day
        classRoom.value = timeProg.classRoom
        startTime.value = timeProg.timeStart
        finishTime.value = timeProg.timeFinish
        typeLesson.value = timeProg.typeOfLesson
        reminder.value = timeProg.reminderTime
    }




}