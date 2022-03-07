package com.saboon.timetable.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.saboon.timetable.Fragments.MainFragmentDirections
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime
import com.saboon.timetable.R

class MainRecyclerAdapter(val lessonsList: ArrayList<ModelLesson>, val lessonTimeList: ArrayList<ModelTime>):RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    class MainViewHolder(view : View):RecyclerView.ViewHolder(view) {

        val startTime: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_startTime)
        val finisTime: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_finishTime)
        val colorDivider: View = view.findViewById(R.id.fragmentMain_recycler_view_divider)
        val lessonName:TextView = view.findViewById(R.id.fragmentMain_recycler_textView_lessonName)
        val lecturerName: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_lecturerName)
        val roomText: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_classRoom)
        val typeText: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view_ = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycler_row_lessons,parent,false)
        return MainViewHolder(view_)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {


        //eger ModelLessonun id si ModelTimedaki below lessona esit ise
        val indexLesson = lessonsList.indexOfFirst {
            it.id == lessonTimeList[position].belowLesson
        }


        holder.lessonName.text = lessonsList[indexLesson].lessonName
        holder.lecturerName.text = lessonsList[indexLesson].lecturerName
        holder.startTime.text = lessonTimeList[position].timeStart
        holder.finisTime.text = lessonTimeList[position].timeFinish
        holder.colorDivider.setBackgroundColor(Color.parseColor(lessonsList[indexLesson].color))
        holder.roomText.text= "Classroom: ${lessonTimeList[position].classRoom}"
        holder.typeText.text = lessonTimeList[position].typeOfLesson.toString()



        holder.itemView.setOnClickListener{
            val selectedItem = lessonsList[position].id
            val belowProgram = lessonsList[position].belowProgram
            val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(selectedItem, belowProgram)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return lessonTimeList.size
    }

    override fun getItemViewType(position: Int): Int {

        when(lessonTimeList[position].day){
            "Pazartesi" -> return 0
            "Salı" -> return 1
            "Çarşamba" -> return 2
            "Perşembe" -> return 3
            "Cuma" -> return 4
            "Cumartesi" -> return 5
            "Pazar" -> return 6
        }
        return super.getItemViewType(position)

    }


    fun updateList(newLessonsList: List<ModelLesson>, newTimeList: List<ModelTime>){
        lessonsList.clear()
        lessonsList.addAll(newLessonsList)
        lessonTimeList.clear()
        lessonTimeList.addAll(newTimeList)
        notifyDataSetChanged()
    }
}