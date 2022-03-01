package com.saboon.timetable.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saboon.timetable.Models.ModelTime
import com.saboon.timetable.Utils.*

class AddProgViewModel: ViewModel() {

    val whichDay = MutableLiveData<Int?>()
    val classRoom = MutableLiveData<String?>()
    val startTime = MutableLiveData<String?>()
    val finishTime = MutableLiveData<String?>()
    val typeLesson =  MutableLiveData<Int?>()
    val reminder = MutableLiveData<Int?>()



    fun refreshData(){
        val timeProg = ModelTime("id0",daysMap.get("MONDAY"), "09:00", "10:00", typesMap.get("LESSON"), null, reminderMap.get("5_MINUTE_AGO"), "idLesson1", "id")

        whichDay.value = timeProg.day
        classRoom.value = timeProg.classRoom
        startTime.value = timeProg.timeStart
        finishTime.value = timeProg.timeFinish
        typeLesson.value = timeProg.typeOfLesson
        reminder.value = timeProg.reminderTime
    }




}