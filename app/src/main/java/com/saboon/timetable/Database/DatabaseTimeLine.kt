package com.saboon.timetable.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelProgram
import com.saboon.timetable.Models.ModelTime


@Database(entities = arrayOf(ModelProgram::class, ModelLesson::class, ModelTime::class), version = 1)
abstract class DatabaseTimeLine: RoomDatabase() {

    abstract fun programDAO(): ProgramDAO
    abstract fun lessonDAO(): LessonDAO
    abstract fun timeDAO(): TimeDAO


    //\\--SINGLETON--//\\

    companion object{
       @Volatile
       private var instance : DatabaseTimeLine? = null

        private val lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }


        // TODO: burada hep patliyor
        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DatabaseTimeLine::class.java, "TimetableDatabase"
        ).build()

        //.enableMultiInstanceInvalidation()
    }

}