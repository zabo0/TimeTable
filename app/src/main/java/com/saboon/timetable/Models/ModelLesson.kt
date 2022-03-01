package com.saboon.timetable.Models

import android.graphics.Color

data class ModelLesson(

    val id: String,
    val dateAdded: String,
    val lessonName: String?,
    val lecturerName: String?,
    val color: String?,
    val absenteeism: String?,
    val below: String

)
