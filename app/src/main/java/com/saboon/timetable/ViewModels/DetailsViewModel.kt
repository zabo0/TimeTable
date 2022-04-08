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
            //showDataInUI(lesson,null)
        }
    }

    fun updateLessonName(lessonID: String, newLessonName:String?){
        launch {
            DatabaseTimeLine(getApplication()).lessonDAO().updateLessonName(lessonID,newLessonName)
        }
    }

    fun updateLecturerName(lessonID: String, newLecturerName:String?){
        launch {
            DatabaseTimeLine(getApplication()).lessonDAO().updateLecturerName(lessonID,newLecturerName)
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
            lesson.lessonName?.let {
                lessonName.value = it
            }
        }
        if (lesson != null) {
            lesson.lecturerName?.let {
                lecturerName.value = it
            }
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