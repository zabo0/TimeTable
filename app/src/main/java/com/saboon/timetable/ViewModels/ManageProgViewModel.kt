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
        }

    }

    fun getAllProgramsFromDatabase(){

        launch {
            DatabaseTimeLine(getApplication()).programDAO().getAllProg()

        }
    }



    fun showDataInUI(prog: List<ModelProgram>){
        if (prog != null){
            programs.value = prog
        }else{
            empty.value = true
        }


        error.value = false
    }
}