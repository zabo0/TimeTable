package com.saboon.timetable.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saboon.timetable.Models.ModelTime
import com.saboon.timetable.R

class DetailsRecyclerAdapter(val programTimesList: ArrayList<ModelTime>):RecyclerView.Adapter<DetailsRecyclerAdapter.DetailsViewHolder>() {


    class DetailsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val countText: TextView = view.findViewById(R.id.fragmetAddProg_recyclerRow_textView_count)
        val dayText: TextView = view.findViewById(R.id.fragmetAddProg_recyclerRow_textView_day)
        val timeText: TextView = view.findViewById(R.id.fragmetAddProg_recyclerRow_textView_time)
        val lessonTypeText: TextView = view.findViewById(R.id.fragmetAddProg_recyclerRow_textView_typeOfLesson)
        val reminderText: TextView = view.findViewById(R.id.fragmetAddProg_recyclerRow_textView_reminder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val view_ = LayoutInflater.from(parent.context).inflate(R.layout.fragment_add_prog_recycler_row_prog,parent,false)
        return DetailsViewHolder(view_)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.countText.text = position.toString()
        holder.dayText.text = programTimesList[position].day
        holder.timeText.text = "${programTimesList[position].timeStart}-${programTimesList[position].timeFinish}"
        holder.lessonTypeText.text = programTimesList[position].typeOfLesson
        holder.reminderText.text = "Remind ${programTimesList[position].reminderTime}"
    }

    override fun getItemCount(): Int {
        return programTimesList.size
    }
}