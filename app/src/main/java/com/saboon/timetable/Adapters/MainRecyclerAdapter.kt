package com.saboon.timetable.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.R

class MainRecyclerAdapter(val lessonsList: ArrayList<ModelLesson>):RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    class MainViewHolder(view : View):RecyclerView.ViewHolder(view) {

        val startTime: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_startTime)
        val finisTime: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_finishTime)
        val colorDivider: View = view.findViewById(R.id.fragmentMain_recycler_view_divider)
        val lessonName:TextView = view.findViewById(R.id.fragmentMain_recycler_textView_lessonName)
        val lecturerName: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_lecturerName)
        val roomText: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_classRoom)
        val absenteeismText: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_absenteeism)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view_ = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycler_row_lessons,parent,false)
        return MainViewHolder(view_)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        holder.startTime.text = lessonsList[position].timeProgram[position].timeStart
        holder.finisTime.text = lessonsList[position].timeProgram[position].timeFinish
        holder.colorDivider.setBackgroundColor(Color.parseColor(lessonsList[position].color))
        holder.lessonName.text = lessonsList[position].lessonName
        holder.lecturerName.text = lessonsList[position].lecturerName
        holder.roomText.text= lessonsList[position].classRoom
        holder.absenteeismText.text = lessonsList[position].absenteeism
    }

    override fun getItemCount(): Int {
        return lessonsList.size
    }
}