package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelProgram
import kotlinx.coroutines.launch

class ManageProgViewModel(application: Application): BaseViewModel(application) {

    val programs = MutableLiveData<List<ModelProgram>?>()
    val error = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val empty = MutableLiveData<Boolean>()

    fun storeProgramInDatabase(program: ModelProgram){
        loading.value = true
        launch {
            DatabaseTimeLine(getApplication()).programDAO().insertProgram(program)
            //showDataInUI(arrayListOf(program))
        }

    }

    fun getAllProgramsFromDatabase(){

        loading.value = true
        launch {
            val progList = DatabaseTimeLine(getApplication()).programDAO().getAllProg()
            showDataInUI(progList)
        }
    }



    fun showDataInUI(prog: List<ModelProgram>){
        if (prog.size != 0){
            programs.value = prog
            empty.value = false
            loading.value = false
            error.value = false
        }else{
            empty.value = true
            error.value = false
        }
    }
}