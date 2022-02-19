package com.saboon.timetable.Models

data class ModelProgram(
    val id: String,
    val dateCreated: String,
    val dateEdited: String,
    val lessons: List<ModelLesson>
)
