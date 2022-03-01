package com.saboon.timetable.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.saboon.timetable.Models.ModelLesson

@Dao
interface LessonDAO {

    @Insert
    fun insertLesson(vararg lesson: ModelLesson): List<String>

    @Query("SELECT * FROM ModelLesson WHERE below = :belowProgID")
    fun getAllLessons(belowProgID: String): List<ModelLesson>

    @Query("SELECT * FROM ModelLesson WHERE id = :lessonID")
    fun getLesson(lessonID: String): ModelLesson

    @Query("DELETE FROM ModelLesson WHERE id = :lessonID")
    fun deleteLesson(lessonID: String)

}