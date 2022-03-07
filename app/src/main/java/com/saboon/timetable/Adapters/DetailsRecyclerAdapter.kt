package com.saboon.timetable.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.saboon.timetable.Fragments.DetailsFragmentDirections
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
        val view_ = LayoutInflater.from(parent.context).inflate(R.layout.fragment_add_time_recycler_row_prog,parent,false)
        return DetailsViewHolder(view_)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val pos = position+1
        holder.countText.text = pos.toString()
        holder.dayText.text = holder.itemView.context.resources.getStringArray(R.array.Days)[programTimesList[position].day!!.toInt()]
        holder.timeText.text = "${programTimesList[position].timeStart}-${programTimesList[position].timeFinish}"
        holder.lessonTypeText.text = programTimesList[position].typeOfLesson
        holder.reminderText.text = "Remind ${programTimesList[position].reminderTime}"



        holder.itemView.setOnClickListener{
            val selectedItem = programTimesList[position].id
            val belowLesson = programTimesList[position].belowLesson
            val belowProgram = programTimesList[position].belowProgram
            val action = DetailsFragmentDirections.actionDetailsFragmentToAddProgramFragment(selectedItem, belowLesson, belowProgram)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return programTimesList.size
    }


    fun updateList(newList: List<ModelTime>){
        programTimesList.clear()
        programTimesList.addAll(newList)
        notifyDataSetChanged()
    }
}