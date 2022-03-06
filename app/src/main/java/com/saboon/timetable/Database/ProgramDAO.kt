package com.saboon.timetable.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.saboon.timetable.Models.ModelProgram


@Dao
interface ProgramDAO {

    @Insert
    suspend fun insertProgram(program: ModelProgram)

    @Query("SELECT * FROM ModelProgram")
    suspend fun getAllProg(): List<ModelProgram>

    @Query("SELECT * FROM ModelProgram WHERE id = :progID")
    suspend fun getProg(progID: String): ModelProgram

    @Query("SELECT name FROM ModelProgram WHERE id = :progID")
    suspend fun getProgramName(progID: String): String

    @Query("DELETE FROM ModelProgram WHERE id = :progID")
    suspend fun deleteProg(progID: String)

}