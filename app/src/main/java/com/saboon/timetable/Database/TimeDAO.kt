package com.saboon.timetable.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.saboon.timetable.Models.ModelTime

@Dao
interface TimeDAO {

    @Insert
    fun insertTime(vararg time: ModelTime): List<ModelTime>

    @Query("SELECT * FROM ModelTime WHERE belowProgram = :belowProgID ORDER BY day ASC, timeStart ASC")
    fun getAllTime(belowProgID: String): List<ModelTime>

    @Query("SELECT * FROM ModelTime WHERE id = :timeID" )
    fun getTime(timeID: String): ModelTime

    @Query("SELECT * FROM ModelTime WHERE belowLesson = :belowLessID ORDER BY day ASC, timeStart ASC")
    fun getLessonTimes(belowLessID: String): List<ModelTime>

    @Query("DELETE FROM ModelTime WHERE id= :timeID")
    fun deleteTime(timeID: String)


}