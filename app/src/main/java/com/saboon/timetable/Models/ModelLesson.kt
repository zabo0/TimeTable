package com.saboon.timetable.Models

import android.graphics.Color

data class ModelLesson(

    val id: String,
    val dateAdded: String,
    val lessonName: String?,
    val timeProgram: List<ModelTime>?,
    val lecturerName: String?,
    val color: String?,
    val absenteeism: String?

)
