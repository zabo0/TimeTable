package com.saboon.timetable.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ModelTime(

    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "day")
    val day: String?,

    @ColumnInfo(name ="timeStart")
    val timeStart: String?,

    @ColumnInfo(name ="timeFinish")
    val timeFinish: String?,

    @ColumnInfo(name = "typeLesson")
    val typeOfLesson: String?,

    @ColumnInfo(name ="classRoom")
    val classRoom: String?,

    @ColumnInfo(name ="reminder")
    val reminderTime: String?,

    @ColumnInfo(name = "belowLesson")
    val belowLesson: String,

    @ColumnInfo(name = "belowProgram")
    val belowProgram: String

)
