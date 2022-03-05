package com.saboon.timetable.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ModelLesson(

    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "dateAdded")
    val dateAdded: String,

    @ColumnInfo(name = "lessonName")
    val lessonName: String?,

    @ColumnInfo(name = "lecturerName")
    val lecturerName: String?,

    @ColumnInfo(name = "color")
    val color: String?,

    @ColumnInfo(name = "absenteeism")
    val absenteeism: String?,

    @ColumnInfo(name = "below")
    val belowProgram: String

)
