package com.saboon.timetable.ViewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.saboon.timetable.Database.DatabaseTimeLine
import com.saboon.timetable.Models.ModelProgram
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditProgViewModel(application: Application): BaseViewModel(application) {


    val program = MutableLiveData<ModelProgram?>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()


    fun getProgramFromSQLite(programId: String){
        loading.value = true
        launch {
            program.value = DatabaseTimeLine(getApplication()).programDAO().getProg(programId)
        }
    }

    fun deleteProgram(programId: String){
        launch {
            DatabaseTimeLine(getApplication()).programDAO().deleteProg(programId)
        }
    }

    fun updateProgramName(programId: String, newName: String){

        val dateEdited = SimpleDateFormat("dd.MM.yyyy-HH:mm:ss").format(Calendar.getInstance().time)

        launch {
            DatabaseTimeLine(getApplication()).programDAO().updateprogramName(programId, newName, dateEdited)
        }
    }



}