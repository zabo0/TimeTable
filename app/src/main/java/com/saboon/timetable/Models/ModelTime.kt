package com.saboon.timetable.Models

data class ModelTime(

    val id: String,
    val day: String?,
    val timeStart: String?,
    val timeFinish: String?,
    val typeOfLesson: String?,
    val classRoom: String?,
    val reminderTime: String?,
    val below: String

)
