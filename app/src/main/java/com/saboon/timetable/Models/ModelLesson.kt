package com.saboon.timetable.Models

import android.graphics.Color

data class ModelLesson(

    val id: String,
    val dateAdded: String,
    val lessonName: String,
    val classRoom: String,
    val timeProgram: List<ModelTime>,
    val lecturerName: String,
    val color: Color

)
