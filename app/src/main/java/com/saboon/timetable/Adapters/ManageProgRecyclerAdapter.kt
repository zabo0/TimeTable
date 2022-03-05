package com.saboon.timetable.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saboon.timetable.Models.ModelProgram
import com.saboon.timetable.R

class ManageProgRecyclerAdapter(val programList: ArrayList<ModelProgram>):RecyclerView.Adapter<ManageProgRecyclerAdapter.ManageProgViewHolder>() {


    class ManageProgViewHolder(view: View):RecyclerView.ViewHolder(view) {

        val programName:TextView = view.findViewById(R.id.fragmentAddProg_recyclerRow_progName)
        val dateAdded: TextView = view.findViewById(R.id.fragmentAddProg_recyclerRow_dateAdded)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageProgViewHolder {
        val view_ = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycler_row_lessons, parent, false)
        return ManageProgViewHolder(view_)
    }

    override fun onBindViewHolder(holder: ManageProgViewHolder, position: Int) {
        holder.programName.text = programList[position].name
        holder.dateAdded.text =programList[position].dateCreated
    }

    override fun getItemCount(): Int {
        return programList.size
    }



    fun updateList(newProgramList: List<ModelProgram>){
        programList.clear()
        programList.addAll(newProgramList)
        notifyDataSetChanged()

    }

}