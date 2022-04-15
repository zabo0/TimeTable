package com.saboon.timetable.Utils

import java.util.*

class IDGenerator {

    fun generateProgramID(programName : String): String{
        val progName = programName.filter { !it.isWhitespace() }
        val randomID = UUID.randomUUID().toString()
        val id = progName + "_" + randomID
        return id
    }


    fun generateLessonID(programName: String, lessonName: String): String{
        val progName = programName.filter { !it.isWhitespace() }
        val lessonName = lessonName.filter { !it.isWhitespace() }
        val randomID = UUID.randomUUID().toString()
        val id = "${progName}_${lessonName}_${randomID}"
        return id

    }

    fun generateTimeID(programName: String, lessonName: String, day_timeStart: String): String{
        val progName = programName.filter { !it.isWhitespace() }
        val lessonName = lessonName.filter { !it.isWhitespace() }
        val time = day_timeStart.filter { !it.isWhitespace() }
        val randomID = UUID.randomUUID().toString()
        val id = "${progName}_${lessonName}_${time}_${randomID}"
        return id
    }

    fun generateNotificationID(timeID: String): Int{

        val sizeOfTimeID = timeID.length
        val random = (0..1000).random()
        val id = sizeOfTimeID + random

        return id
    }





}