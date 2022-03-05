package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application): BaseViewModel(application) {

    val lessonName =MutableLiveData<String?>()
    val lecturerName = MutableLiveData<String?>()
    val programTimes = MutableLiveData<List<ModelTime>?>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()
    val empty = MutableLiveData<Boolean>()

    fun refreshData(){



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

    fun updateLesson(){

    }


    fun showDataInUI(lesson: ModelLesson?, time: List<ModelTime>?){

        if (lesson != null) {
            lessonName.value = lesson.lessonName!!
        }



        if (lesson != null) {
            lecturerName.value = lesson.lecturerName!!
        }



        if (time != null){
            programTimes.value = time!!
            loading.value = false
            empty.value = false
        }else{
            empty.value = true
        }

        error.value = false

    }
}