package com.saboon.timetable.Utils

import java.util.*

class IDGenerator {

    fun generateProgramID(programName : String): String{
        val name = programName.filter { !it.isWhitespace() }
        val random = UUID.randomUUID().toString()
        val id = name + "_" + random
        return id
    }


    fun generateLessonID(key: String): String{
        val random = UUID.randomUUID().toString()
        val id = key + "_" + random
        return id
    }

    fun generateTimeID(key: String): String{
        val random = UUID.randomUUID().toString()
        val id = key + "_" + random
        return id
    }





}