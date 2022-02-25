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
import com.saboon.timetable.R

class MainRecyclerAdapter(val lessonsList: ArrayList<ModelLesson>):RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

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


    var pos = -1

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        //bir dersin farkli gun ve saatlerde tekrarlanmasi ModelLesson da o dersi tekrarlamaz
        //bunlar sadece ModelLesson icerisindeki ModelTime tipindeki veri listesine eklenir

        //gelen ModelLesson tipindeki lessonList degiskenlerinden birinde ModelTime tipinde timeProgram diye ayri bir liste bulunuyor
        //bu liste icerisindeki her bir itemide recycler viewde gostermek icin asagidaki kontroller saglaniyor
        //gelen lessonList degiskeninde bazi itemler birden fazla kez listenin icerisine konulmus(tam olarak timeProgram.size kadar)
        //boylece gelen sonraki itemin ID'si bir onceki itemin ID'si ile esit ise ModelLesson tipindeki degisken tekrarlaniyor
        //ancak bu degiskenin icerisindeki ModelTime tipindeki listenin icerisindeki itemler sira ile tekrarlanan itemlere yaziliyor


        var poss = position-1 //bir onceki itemin pozisyonunu belirlemek icin
        if (poss < 0)poss = 0 //arraylistler 0 dan kucuk index alamadiklari icin

        val oldId = lessonsList[poss].id //bir onceki itemin id'si
        val currentId = lessonsList[position].id //simdiki itemin id'si

        if (oldId == currentId){
            pos++ //eger id'ler esit ise pos degeri bir arttirilir. bu deger ModelLesson tipinde arka arkaya gelen ayni degere ragmen icerisindeki listenin indexsini arttirir
            //boylece ayni dersin baska gun veya saatlerde tekrarlayan programlarida recyclerview icerisinde gosterilebilir
        }else{
            pos = 0 //burada id'ler esit degil ise yeni bir derse gecilmistir. yani yeni itemin icerisinde gelen listenin indexini 0dan baslatir ki verileri dogru alabilsin
        }
        holder.startTime.text = lessonsList[position].timeProgram!![pos].timeStart
        holder.finisTime.text = lessonsList[position].timeProgram!![pos].timeFinish
        holder.colorDivider.setBackgroundColor(Color.parseColor(lessonsList[position].color))
        holder.lessonName.text = lessonsList[position].lessonName
        holder.lecturerName.text = lessonsList[position].lecturerName
        holder.roomText.text= "Classroom: ${lessonsList[position].timeProgram!![pos].classRoom}"
        holder.typeText.text = lessonsList[position].timeProgram!![pos].typeOfLesson






        holder.itemView.setOnClickListener{
            val action = MainFragmentDirections.actionMainFragmentToDetailsFragment()
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return lessonsList.size
    }


    fun updateList(newList: List<ModelLesson>){
        lessonsList.clear()
        lessonsList.addAll(newList)
        notifyDataSetChanged()
    }
}