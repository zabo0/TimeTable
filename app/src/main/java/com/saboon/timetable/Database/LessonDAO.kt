package com.saboon.timetable.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.saboon.timetable.Models.ModelLesson

@Dao
interface LessonDAO {

    @Insert
    suspend fun insertLesson(lesson: ModelLesson)

    @Query("SELECT * FROM ModelLesson WHERE below = :belowProgID")
    suspend fun getAllLessons(belowProgID: String): List<ModelLesson>

    @Query("SELECT * FROM ModelLesson WHERE id = :lessonID")
    suspend fun getLesson(lessonID: String): ModelLesson

    @Query("DELETE FROM ModelLesson WHERE id = :lessonID")
    suspend fun deleteLesson(lessonID: String)

    @Query("UPDATE ModelLesson SET lessonName = :newLessonName , lecturerName = :newLecturerName, absenteeism = :newAbsenteeism, color = :newColor WHERE id = :lessonID")
    suspend fun updateLesson(lessonID: String, newLessonName:String, newLecturerName: String, newAbsenteeism:String, newColor: String)

}