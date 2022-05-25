package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailsViewModel(application: Application): BaseViewModel(application) {

    val lesson = MutableLiveData<ModelLesson?>()
    val programTimes = MutableLiveData<List<ModelTime>?>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()
    val empty = MutableLiveData<Boolean>()

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


    fun deleteLesson(lessonID: String){
        launch {
            DatabaseTimeLine(getApplication()).timeDAO().deleteAllLessonTimes(lessonID)
            DatabaseTimeLine(getApplication()).lessonDAO().deleteLesson(lessonID)
        }
    }


    fun showDataInUI(lss: ModelLesson?, time: List<ModelTime>?){

        lesson.value = lss

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