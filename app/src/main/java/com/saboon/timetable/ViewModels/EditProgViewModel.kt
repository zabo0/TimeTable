package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelProgram
import kotlinx.coroutines.launch

class EditProgViewModel(application: Application): BaseViewModel(application) {


    val program = MutableLiveData<ModelProgram?>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()


    fun getProgramFromSQLite(programId: String){
        loading.value = true
        launch {
            DatabaseTimeLine(getApplication()).programDAO().getProg(programId)
        }
    }

    fun deleteProgram(programId: String){
        launch {
            DatabaseTimeLine(getApplication()).programDAO().deleteProg(programId)
        }
    }



}