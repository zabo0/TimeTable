package com.saboon.timetable.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ModelProgram(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "dateAdded")
    val dateCreated: String,

    @ColumnInfo(name = "dateEdited")
    val dateEdited: String,

)
