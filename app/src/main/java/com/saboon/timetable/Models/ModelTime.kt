package com.saboon.timetable.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ModelTime(

    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "day")
    val day: Int?,

    @ColumnInfo(name ="timeStart")
    val timeStart: String?,

    @ColumnInfo(name ="timeFinish")
    val timeFinish: String?,

    @ColumnInfo(name = "typeLesson")
    val typeOfLesson: Int?,

    @ColumnInfo(name ="classRoom")
    val classRoom: String?,

    @ColumnInfo(name ="reminder")
    val reminderTime: Int?,

    @ColumnInfo(name = "belowLesson")
    val belowLesson: String,

    @ColumnInfo(name = "belowProgram")
    val belowProgram: String

)
