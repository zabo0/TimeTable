package com.saboon.timetable.Models

import android.graphics.Color

data class ModelLesson(

    val id: String,
    val dateAdded: String,
    val lesson: String,
    val classRoom: String,
    val time: List<ModelTime>,
    val lecturer: String,
    val color: Color,
    val absenteeism: Int
)
