package com.saboon.timetable.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime

class MainViewModel: ViewModel() {



    val lessonList = MutableLiveData<List<ModelLesson>>()
    val error = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val empty = MutableLiveData<Boolean>()


    fun refreshData(){

        val timeProg = ModelTime("id0","monday","09:00","10:00","ders","203","5 minute ago","idLesson1")
        val timeProg2 = ModelTime("id1","saturday","10:00","11:00","lab","lab","5 minute ago","idLesson1")
        val timeProg3 = ModelTime("id2","wednesday","12:00","13:00","uygulama","lab","5 minute ago","idLesson1")

        val timeProgList = arrayListOf(timeProg,timeProg2, timeProg3)
        val timeProgList1 = arrayListOf(timeProg3)
        val timeProgList2 = arrayListOf(timeProg, timeProg3)

        val lesson = ModelLesson("id0","dateAdded","matematik","ali","#FFFF0000","2","idprogName")
        val lesson1 = ModelLesson("id0","dateAdded","matematik", "ali","#FFFF0000","2","idprogName")
        val lesson2 = ModelLesson("id0","dateAdded","matematik", "ali","#FFFF0000","2", "idprogName")

        val lesson3 = ModelLesson("id1","dateAdded","turkce", "veli","#79C610","2","idprogName")

        val lesson4 = ModelLesson("id2","dateAdded","fizik", "ayse","#5B84EA","2","idprogName")
        val lesson5 = ModelLesson("id2","dateAdded","fizik", "ayse","#5B84EA","2","idprogName")


        // TODO: burasi gunlere ve saatlere gore siralanip listeye eklenecek

        lessonList.value = arrayListOf(lesson, lesson1,lesson2, lesson3, lesson4,lesson5)
        error.value = false
        loading.value = false
        empty.value = false

    }

}