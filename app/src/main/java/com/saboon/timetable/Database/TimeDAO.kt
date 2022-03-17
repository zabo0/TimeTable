package com.saboon.timetable.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.saboon.timetable.Models.ModelTime

@Dao
interface TimeDAO {

    @Insert
    suspend fun insertTime(time: ModelTime)

    @Query("SELECT * FROM ModelTime WHERE belowProgram = :belowProgID ORDER BY day ASC,  timeStart ASC")
    suspend fun getAllTime(belowProgID: String): List<ModelTime>

    @Query("SELECT * FROM ModelTime WHERE id = :timeID")
    suspend fun getTime(timeID: String): ModelTime

    @Query("SELECT * FROM ModelTime WHERE belowLesson = :belowLessID ORDER BY day ASC,  timeStart ASC")
    suspend fun getLessonTimes(belowLessID: String): List<ModelTime>

    @Query("DELETE FROM ModelTime WHERE belowLesson= :belowLessID")
    suspend fun deleteTimes(belowLessID: String)

    @Query("UPDATE ModelTime SET " +
            "day = :day, " +
            "timeStart= :timeStart, " +
            "timeFinish = :timeFinish, " +
            "typeLesson = :typeTime, " +
            "classRoom = :classRoom, " +
            "reminder = :reminder, " +
            "belowLesson =:belowLesson, " +
            "belowProgram = :belowProgram  WHERE id = :id"
    )
    suspend fun updateTime(
        id: String,
        day: String,
        timeStart: String,
        timeFinish: String,
        typeTime: String,
        classRoom: String,
        reminder: String,
        belowLesson: String,
        belowProgram: String
    )
}