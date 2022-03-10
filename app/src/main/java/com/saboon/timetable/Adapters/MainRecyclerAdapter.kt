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

class MainRecyclerAdapter(val lessonsList: ArrayList<ModelLesson>, val lessonTimeList: ArrayList<ModelTime>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    companion object {

        const val VIEW_TYPE_MAIN = 1
        const val VIEW_TYPE_DAY = 2
    }

    var listOfLesson = lessonsList


    private inner class MainViewHolder(view : View):RecyclerView.ViewHolder(view) {

        val startTime: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_startTime)
        val finisTime: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_finishTime)
        val colorDivider: View = view.findViewById(R.id.fragmentMain_recycler_view_divider)
        val lessonName:TextView = view.findViewById(R.id.fragmentMain_recycler_textView_lessonName)
        val lecturerName: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_lecturerName)
        val roomText: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_classRoom)
        val typeText: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_type)

        fun bind(position: Int){
            //bir derste birden fazla time olabiliyor bu yuzden mesela lesson listesinde 3 item var ise bazen time listesinde 5 item olabiliyor
            //recycler item countu ise time iteme bakilarak aliniyor.
            //burada yapilan islem su sekilde;
            // siradaki timein hangi derse ait oldugunu bulmamiz gerek bunu da belowLesson a bakarak yapabiliriz
            //ancak siradaki time liste icerisinde 4. sirada olabilir. bu da position degerinin 4 olmasi demek
            //ancak lessonList icerisinde 4. positionda item yok o yuzden dogrudan lessonsList icerisinde position degeri ile arama yapamayiz
            //bizde lessonsList icerisinde id si timeList.belowLesson a esit olanin indexsini aliyoruz ve lessonsList icerisinden o sekilde veri aliyoruz
            val indexLesson = lessonsList.indexOfFirst {
                //bu islem lessonsList icerisinde id si lessonTimeList.belowLesson a esit olan index bulunur ve indexLesson a atanir
                it.id == lessonTimeList[position].belowLesson
            }

            lessonName.text = lessonsList[indexLesson].lessonName
            lecturerName.text = lessonsList[indexLesson].lecturerName
            startTime.text = lessonTimeList[position].timeStart
            finisTime.text = lessonTimeList[position].timeFinish
            colorDivider.setBackgroundColor(Color.parseColor(lessonsList[indexLesson].color))
            roomText.text= lessonTimeList[position].classRoom
            typeText.text = lessonTimeList[position].typeOfLesson.toString()

            itemView.setOnClickListener{
                val selectedItem = lessonTimeList[position].belowLesson
                val belowProgram = lessonTimeList[position].belowProgram
                val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(selectedItem, belowProgram)
                it.findNavController().navigate(action)
            }

        }


    }

    private inner class DayViewHolder(view: View):RecyclerView.ViewHolder(view){

        val day: TextView = view.findViewById(R.id.recyclerRow_days)

        val startTime: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_startTime)
        val finisTime: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_finishTime)
        val colorDivider: View = view.findViewById(R.id.fragmentMain_recycler_view_divider)
        val lessonName:TextView = view.findViewById(R.id.fragmentMain_recycler_textView_lessonName)
        val lecturerName: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_lecturerName)
        val roomText: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_classRoom)
        val typeText: TextView = view.findViewById(R.id.fragmentMain_recycler_textView_type)

        fun bind(position: Int){

            day.text = itemView.context.resources.getStringArray(R.array.Days)[lessonTimeList[position].day!!.toInt()].toString()

            //bir derste birden fazla time olabiliyor bu yuzden mesela lesson listesinde 3 item var ise bazen time listesinde 5 item olabiliyor
            //recycler item countu ise time iteme bakilarak aliniyor.
            //burada yapilan islem su sekilde;
            // siradaki timein hangi derse ait oldugunu bulmamiz gerek bunu da belowLesson a bakarak yapabiliriz
            //ancak siradaki time liste icerisinde 4. sirada olabilir. bu da position degerinin 4 olmasi demek
            //ancak lessonList icerisinde 4. positionda item yok o yuzden dogrudan lessonsList icerisinde position degeri ile arama yapamayiz
            //bizde lessonsList icerisinde id si timeList.belowLesson a esit olanin indexsini aliyoruz ve lessonsList icerisinden o sekilde veri aliyoruz
            val indexLesson = lessonsList.indexOfFirst {
                //bu islem lessonsList icerisinde id si lessonTimeList.belowLesson a esit olan index bulunur ve indexLesson a atanir
                it.id == lessonTimeList[position].belowLesson
            }

            lessonName.text = lessonsList[indexLesson].lessonName
            lecturerName.text = lessonsList[indexLesson].lecturerName
            startTime.text = lessonTimeList[position].timeStart
            finisTime.text = lessonTimeList[position].timeFinish
            colorDivider.setBackgroundColor(Color.parseColor(lessonsList[indexLesson].color))
            roomText.text= lessonTimeList[position].classRoom
            typeText.text = lessonTimeList[position].typeOfLesson.toString()

            itemView.setOnClickListener{
                val selectedItem = lessonTimeList[position].belowLesson
                val belowProgram = lessonTimeList[position].belowProgram
                val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(selectedItem, belowProgram)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_DAY){
            val viewDayItem = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycler_row_days,parent,false)
            return DayViewHolder(viewDayItem)
        }
        val viewLessonItem = LayoutInflater.from(parent.context).inflate(R.layout.fragment_main_recycler_row_lessons,parent,false)
        return MainViewHolder(viewLessonItem)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        when(holder.itemViewType){
            VIEW_TYPE_MAIN -> (holder as MainViewHolder) .bind(position)
            VIEW_TYPE_DAY -> (holder as DayViewHolder) .bind(position)
        }


    }

    override fun getItemViewType(position: Int): Int {
        //return super.getItemViewType(position)


        //burada eger listede gelen gun bir enceki ile ayni degil ise day gorunumunu dondurur
        //bu gorunumde recycler viewin icinde gunu de yazar
        //ilk positionda dogrudan gun viewi dondurmesinin sebebi ilk gunu direk en ustte yazmasi icin
        if(position == 0){
            return VIEW_TYPE_DAY
        }else if(lessonTimeList[position].day != lessonTimeList[position-1].day){
            return VIEW_TYPE_DAY
        }
        return VIEW_TYPE_MAIN

    }

    override fun getItemCount(): Int {
        return lessonTimeList.size
    }



    fun updateList(newLessonsList: List<ModelLesson>, newTimeList: List<ModelTime>){
        lessonsList.clear()
        lessonsList.addAll(newLessonsList)
        lessonTimeList.clear()
        lessonTimeList.addAll(newTimeList)
        notifyDataSetChanged()
    }

}