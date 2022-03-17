package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailsViewModel(application: Application): BaseViewModel(application) {

    val lessonName =MutableLiveData<String?>()
    val lecturerName = MutableLiveData<String?>()
    val programTimes = MutableLiveData<List<ModelTime>?>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()
    val empty = MutableLiveData<Boolean>()

    fun refreshData(){



    }

    fun getLessonFromSQLite(lessonID: String, response: (ModelLesson)-> Unit ){
        launch {
            val lesson = DatabaseTimeLine(getApplication()).lessonDAO().getLesson(lessonID)
            response(lesson)
        }
    }

    fun getDataFromSQLite(idLesson:String?){
        loading.value = true
        if (idLesson != null){
            launch {
                val lesson = DatabaseTimeLine(getApplication()).lessonDAO().getLesson(idLesson)
                val times = DatabaseTimeLine(getApplication()).timeDAO().getLessonTimes(idLesson)

                showDataInUI(lesson, times)
            }
        }else{
            showDataInUI(null, null)
        }
    }

    fun storeLessonInDatabase(lesson: ModelLesson){
        launch {
            DatabaseTimeLine(getApplication()).lessonDAO().insertLesson(lesson)
            showDataInUI(lesson,null)
        }
    }

    fun updateLesson(lesson: ModelLesson, response: (Boolean)-> Unit){
        try {
            launch {
                DatabaseTimeLine(getApplication()).lessonDAO().updateLesson(
                    lesson.id,
                    lesson.lessonName!!,
                    lesson.lecturerName!!,
                    lesson.absenteeism!!,
                    lesson.color!!
                )
            }
            response(true)
        }catch (error:Exception){
            response(false)
            throw error
        }
    }

    fun deleteLesson(lessonID: String){
        launch {
            DatabaseTimeLine(getApplication()).timeDAO().deleteAllLessonTimes(lessonID)
            DatabaseTimeLine(getApplication()).lessonDAO().deleteLesson(lessonID)
        }
    }


    fun showDataInUI(lesson: ModelLesson?, time: List<ModelTime>?){

        if (lesson != null) {
            lessonName.value = lesson.lessonName!!
        }


        if (lesson != null) {
            lecturerName.value = lesson.lecturerName!!
        }

        time?.let {
            if (time.isNotEmpty()){
                programTimes.value = time
                loading.value = false
                empty.value = false
            }else{
                empty.value = true
            }
            error.value = false
        }


    }
}