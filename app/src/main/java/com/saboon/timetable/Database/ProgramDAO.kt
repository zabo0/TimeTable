package com.saboon.timetable.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.saboon.timetable.Models.ModelProgram


@Dao
interface ProgramDAO {

    @Insert
    fun insertProgram(program: ModelProgram): List<String>

    @Query("SELECT * FROM ModelProgram")
    fun getAllProg(): List<ModelProgram>

    @Query("SELECT * FROM ModelProgram WHERE id = :progID")
    fun getProg(progID: String): ModelProgram

    @Query("DELETE FROM ModelProgram WHERE id = :progID")
    fun deleteProg(progID: String)

}