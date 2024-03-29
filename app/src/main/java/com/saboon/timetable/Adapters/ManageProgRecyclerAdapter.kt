package com.saboon.timetable.Adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.saboon.timetable.Fragments.ManageProgramFragmentDirections
import com.saboon.timetable.Models.ModelProgram
import com.saboon.timetable.R
import com.saboon.timetable.Utils.SharedPref
import java.text.SimpleDateFormat

class ManageProgRecyclerAdapter(
    val programList: ArrayList<ModelProgram>,
    val activity: Activity
    ):RecyclerView.Adapter<ManageProgRecyclerAdapter.ManageProgViewHolder>() {



    class ManageProgViewHolder(view: View):RecyclerView.ViewHolder(view) {

        val programName:TextView = view.findViewById(R.id.fragmentAddProg_recyclerRow_progName)
        val dateAdded: TextView = view.findViewById(R.id.fragmentAddProg_recyclerRow_dateAdded)
        val edit: ImageView = view.findViewById(R.id.fragmentProgManage_recyclerRow_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageProgViewHolder {
        val view_ = LayoutInflater.from(parent.context).inflate(R.layout.fragment_manage_program_recycler_row, parent, false)
        return ManageProgViewHolder(view_)
    }

    override fun onBindViewHolder(holder: ManageProgViewHolder, position: Int) {
        holder.programName.text = programList[position].name

        //gelen tarih verisi soyle dd.MM.yyyy-HH:mm:ss
        //sadece tarih kismini almak icin parcalayip dd MMMM yyyy formatina ceviriyoruz

        val dateCreated = SimpleDateFormat("dd MMMM yyyy")
                .format(SimpleDateFormat("dd.MM.yyyy")
                .parse(programList[position].dateCreated.split("-")[0]))
        holder.dateAdded.text = dateCreated


        holder.itemView.setOnClickListener{
            //secilen program shared prefe kaydedilir
            //boylece main fragment her acildiginda dogrudan son acilan programla devam eder
            SharedPref(activity).saveIDToSharedPref(programList[position].id)
            val actionToManageProg = ManageProgramFragmentDirections.actionManageProgramFragmentToMainFragment()
            it.findNavController().navigate(actionToManageProg)
        }

        holder.edit.setOnClickListener {
            val action = ManageProgramFragmentDirections.actionManageProgramFragmentToEditProgramFragment(programList[position].id)
            it.findNavController().navigate(action)
            println("pressed edit: ${position}")
        }


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