package com.saboon.timetable.Utils

import java.util.*

class IDGenerator {

    fun generateProgramID(programName : String): String{
        val progName = programName.filter { !it.isWhitespace() }
        val uuid = UUID.randomUUID().toString()
        val id = progName + "_" + uuid
        return id
    }


    fun generateLessonID(): String{
        return UUID.randomUUID().toString()
    }

    fun generateTimeID(): String{
        val uuid = UUID.randomUUID().toString()
        val id = uuid
        return id
    }





}