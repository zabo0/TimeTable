package com.saboon.timetable.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saboon.timetable.Models.ModelProgram


@Database(entities = arrayOf(ModelProgram::class), version = 1)
abstract class DatabaseTimeLine: RoomDatabase() {

    abstract fun programDAO(): ProgramDAO


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

        private fun makeDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, DatabaseTimeLine::class.java, "TimeLineDatabase").build()

    }

}