package com.saboon.timetable.Models

data class ModelTime(

    val day: String,
    val timeStart: String,
    val timeFinish: String,
    val typeOfLesson: String,
    val reminderTime: String,
    val absenteeism: Int

)
