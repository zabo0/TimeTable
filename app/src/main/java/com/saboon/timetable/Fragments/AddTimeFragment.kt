package com.saboon.timetable.Fragments

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.saboon.timetable.Models.ModelTime
import com.saboon.timetable.Notifications.ReminderBroadcast
import com.saboon.timetable.R
import com.saboon.timetable.Utils.IDGenerator
import com.saboon.timetable.ViewModels.AddTimeViewModel
import com.saboon.timetable.databinding.FragmentAddTimeBinding
import java.util.*


class AddTimeFragment : Fragment() {

    private var _binding: FragmentAddTimeBinding?=null
    private val binding get() = _binding!!

    lateinit var viewModel :AddTimeViewModel

    lateinit var arrayAdapterDays: ArrayAdapter<String>
    lateinit var arrayAdapterReminder: ArrayAdapter<String>
    lateinit var arrayAdapterTypeOfLesson: ArrayAdapter<String>

    private lateinit var selectedTimeID: String
    private lateinit var belowProgramID : String
    private lateinit var belowLessonID: String


    private var isNewTime = true


    private lateinit var calendarStartTime: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //eger geri tusuna basilirsa burasi calisir
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                checkTheChanges {
                    if(it){
                        val actionToBack = AddTimeFragmentDirections.actionAddProgramFragmentToDetailsFragment(belowLessonID, belowProgramID)
                        findNavController().navigate(actionToBack)
                    }
                }

            }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddTimeBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNotificationChannel()

        viewModel = ViewModelProvider(this).get(AddTimeViewModel::class.java)

        arguments?.let {
            if (it != null){
                AddTimeFragmentArgs.fromBundle(it).selectedTimeID?.let {
                    selectedTimeID = it
                    isNewTime = false
                    viewModel.getDataFromSQLite(selectedTimeID)
                }
                AddTimeFragmentArgs.fromBundle(it).belowLessonID?.let {
                    belowLessonID = it
                }
                AddTimeFragmentArgs.fromBundle(it).belowProgramID?.let {
                    //bu verinin alinmasinin sebebi database e kayit yapilirken kullanilacak olmasi
                    belowProgramID = it
                }
            }
        }

        val DayItems = resources.getStringArray(R.array.Days)
        arrayAdapterDays = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, DayItems)
        binding.autoCompleteTextView.setAdapter(arrayAdapterDays)


        val RemindersItem = resources.getStringArray(R.array.reminder)
        arrayAdapterReminder = ArrayAdapter(requireContext(),R.layout.dropdown_list_item,RemindersItem)
        binding.autoCompleteTextViewReminderPicker.setAdapter(arrayAdapterReminder)


        val TypeOfLessonItem = resources.getStringArray(R.array.typeOfLesson)
        arrayAdapterTypeOfLesson = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, TypeOfLessonItem)
        binding.autoCompleteTextViewTypeLesson.setAdapter(arrayAdapterTypeOfLesson)

        //baslagicta gun ve hatirlaticiya default deger verilir
        binding.autoCompleteTextView.setText(arrayAdapterDays.getItem(0), false)
        binding.autoCompleteTextViewReminderPicker.setText(arrayAdapterReminder.getItem(0), false)

        binding.editTextStartTimePicker.setOnClickListener {
            val isSystem24Hour = is24HourFormat(requireContext())
            val clockFormat = if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(0)
                .setMinute(0)
                .setTitleText("Start Time")
                .build()
            picker.show(childFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener{
                val hour = picker.hour
                val min = picker.minute
                val timeText = String.format("%02d:%02d", hour, min)

                //burasi notification icin
                val day_of_week = requireActivity().resources.getStringArray(R.array.Days).indexOf(binding.autoCompleteTextView.text.toString()) + 2
                calendarStartTime = Calendar.getInstance()//calendar da default olarak pazar gunu birinci gundur
                calendarStartTime.set(Calendar.DAY_OF_WEEK, day_of_week)
                calendarStartTime.set(Calendar.HOUR_OF_DAY,hour)
                calendarStartTime.set(Calendar.MINUTE,min)
                calendarStartTime.set(Calendar.SECOND,0)
                calendarStartTime.set(Calendar.MILLISECOND, 0)
                //////////////////////////
                binding.fragmentAddProgEditTextStartTimePicker.editText?.setText(timeText)
            }

        }

        binding.editTextFinishTimePicker.setOnClickListener {
            val isSystem24Hour = is24HourFormat(requireContext())
            val clockFormat = if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(0)
                .setMinute(0)
                .setTitleText("Start Time")
                .build()
            picker.show(childFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener{
                val hour = picker.hour
                val min = picker.minute
                val timeText = String.format("%02d:%02d", hour, min)
                binding.fragmentAddProgEditTextFinishTimePicker.editText?.setText(timeText)
            }

        }

        binding.fragmentAddTimeTextViewAddTime.setOnClickListener{
            checkTheChanges { response ->
                if (response){
                    val actionToBack = AddTimeFragmentDirections.actionAddProgramFragmentToDetailsFragment(belowLessonID, belowProgramID)
                    it.findNavController().navigate(actionToBack)
                }
            }
        }


        binding.fragmentAddProgButtonSave.setOnClickListener{

            if (isNewTime){

                val programName = belowProgramID.split("_")[0]//id icin
                val lessonName = belowLessonID.split("_")[1]//id icin
                val dayString = binding.autoCompleteTextView.text//id icin

                //burada "day" databaseden cekerken gunleri oncelik sirasina gore cekebilmesi icin gunlerin karsilik geldigi sayiyi database e kaydediyor
                //eger duz "pazartesi" olarak kaydediliyor olsaydi "carsamba" daha once cekiliyor olacakti(alfabede daha onde)
                //ancak "pazartesi" string klasorundeki array da indexi "0" carsamba ise "2" boylece "pazartesi"nin onceligi daha fazla oluyor
                val dayInt = requireActivity().resources.getStringArray(R.array.Days).indexOf(binding.autoCompleteTextView.text.toString()).toString()

                val classRoom = binding.fragmentAddProgEditTextClassroom.text?.trimEnd().toString()
                val timeStart = binding.editTextStartTimePicker.text.toString()
                val timeFinish = binding.editTextFinishTimePicker.text.toString()
                val type = binding.autoCompleteTextViewTypeLesson.text?.trimEnd().toString()
                val reminder = binding.autoCompleteTextViewReminderPicker.text.toString()

                val day_timeStart = "${dayString}_${timeStart}"//id icin
                val id = IDGenerator().generateTimeID(programName,lessonName,day_timeStart)
                val notificationID = IDGenerator().generateNotificationID(id)

                val lessonTimeProg = ModelTime(id,dayInt,timeStart,timeFinish,type,classRoom,reminder,notificationID,belowLessonID, belowProgramID)

                viewModel.storeTimeInDatabase(lessonTimeProg)

                // TODO: notification calismiyor
                //////////////////
                //val time = getStartTime(reminder,timeStart,timeFinish)
                sheduleNotification(notificationID, lessonName, "${dayString} ders baslamak uzere")
                //////////////////


                val actionToBack = AddTimeFragmentDirections.actionAddProgramFragmentToDetailsFragment(belowLessonID, belowProgramID)
                it.findNavController().navigate(actionToBack)
            }else{
                checkTheChanges { response ->
                    if (response){
                        val actionToBack = AddTimeFragmentDirections.actionAddProgramFragmentToDetailsFragment(belowLessonID, belowProgramID)
                        it.findNavController().navigate(actionToBack)
                    }
                }
            }
        }

        binding.fragmentAddProgButtonCancel.setOnClickListener {
            val actionToBack = AddTimeFragmentDirections.actionAddProgramFragmentToDetailsFragment(belowLessonID, belowProgramID)
            it.findNavController().navigate(actionToBack)
        }

        binding.fragmentAddProgImageViewDelete.setOnClickListener {
            if(!isNewTime){
                viewModel.deleteTime(selectedTimeID)
                val actionToBack = AddTimeFragmentDirections.actionAddProgramFragmentToDetailsFragment(belowLessonID, belowProgramID)
                it.findNavController().navigate(actionToBack)
            }
        }

        observeData()



    }




    fun observeData(){

        viewModel.timeProg.observe(viewLifecycleOwner, Observer {
            it?.let {
                setValuesToView(it)
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.addProgramContent.visibility = View.GONE
                    binding.addProgramLoadingProgressBar.visibility = View.VISIBLE
                    binding.addProgramErrorText.visibility = View.GONE
                }else{
                    binding.addProgramLoadingProgressBar.visibility = View.GONE
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it){
                    binding.addProgramContent.visibility = View.GONE
                    binding.addProgramLoadingProgressBar.visibility = View.GONE
                    binding.addProgramErrorText.visibility = View.VISIBLE
                }else{
                    binding.addProgramErrorText.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


    fun setValuesToView(time: ModelTime){

        binding.addProgramContent.visibility = View.VISIBLE
        binding.fragmentAddProgEditTextClassroom.setText( time.classRoom.toString())
        binding.editTextStartTimePicker.setText(time.timeStart.toString())
        binding.editTextFinishTimePicker.setText(time.timeFinish.toString())
        binding.autoCompleteTextViewTypeLesson.setText(time.typeOfLesson.toString(), false)
        binding.autoCompleteTextView.setText(arrayAdapterDays.getItem(time.day!!.toInt()), false)
        binding.autoCompleteTextViewReminderPicker.setText(time.reminderTime, false)
    }


    fun showAlert(title: String,message: String, response: (Boolean) -> Unit){

        val alertDialogBuilder = AlertDialog.Builder(activity)

        val string = requireActivity().resources

        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setPositiveButton(string.getString(R.string.alertDialog_posButton)) { dialog, id ->
            response(true)
        }
        alertDialogBuilder.setNegativeButton(string.getString(R.string.alertDialog_negButton)){dialog, id ->
            response(false)
        }

        alertDialogBuilder.show()
    }


    fun checkTheChanges(response: (Boolean) -> Unit){

        //burasi kullanici degisiklik yapip yapmadigini kotrol eder
        //eger yapmis ise kullaniciya degisiklikleri kaydetmek isteyip istemedigi sorulur
        //kullanici evet yanitini verirse veri tabani degisen degerler ile guncellenir
        //kullanici hayir yanitini verirse hic bir sey yapilmadan gecilir
        if (!isNewTime){

            val newDay = binding.autoCompleteTextView.text.toString()
            val newClasroom = binding.fragmentAddProgEditTextClassroom.text.toString()
            val newStartTime = binding.fragmentAddProgEditTextStartTimePicker.editText?.text.toString()
            val newFinishTime = binding.fragmentAddProgEditTextFinishTimePicker.editText?.text.toString()
            val newTypeOfLesson = binding.autoCompleteTextViewTypeLesson.text.toString()
            val newReminder = binding.autoCompleteTextViewReminderPicker.text.toString()

            viewModel.getTimeFromSQLite(selectedTimeID){
                if (arrayAdapterDays.getItem(it.day!!.toInt()) != newDay
                    || it.classRoom != newClasroom
                    || it.timeStart != newStartTime
                    || it.timeFinish != newFinishTime
                    || it.typeOfLesson != newTypeOfLesson
                    || it.reminderTime != newReminder){
                    showAlert("kaydet","degisiklikleri kaydetmek istiyormusunuz"){ wantSave ->
                        if(wantSave){
                            val updatedTime = ModelTime(
                                it.id,
                                requireActivity().resources.getStringArray(R.array.Days).indexOf(newDay).toString(),
                                newStartTime,
                                newFinishTime,
                                newTypeOfLesson,
                                newClasroom,
                                newReminder,
                                it.notificationID,
                                it.belowLesson,
                                it.belowProgram
                            )
                            viewModel.updateTime(updatedTime){
                                if (it){
                                    //eger kayit basarili ise true gonder
                                    response(true)
                                }else{
                                    //eger kayit basarisiz ise false gonder
                                    response(false)
                                }
                            }
                        }else{
                            //eger kullanici iptale bastiysa true gonder (kullanici hayira bastiysa)
                            response(true)
                        }
                    }
                }else{
                    //eger degisiklik yok true gonder
                    response(true)
                }
            }
        }else{
            //eger henuz yeni kayit yapiliyorsa true gonder
            response(true)
        }
    }


    // TODO: notification bitir
    /**
     * https://www.youtube.com/watch?v=_Z2S63O-1HE
     * https://www.youtube.com/watch?v=nl-dheVpt8o
     * notification ile ilgili videolar
     *
     * https://riptutorial.com/android/example/11495/scheduling-notifications
     * https://martian.ventures/mantra/insights/schedule-local-notifications-on-android/
     * notification ile ilgili siteler
     */

    fun sheduleNotification(notificationID: Int, lessonName: String, message: String){

        val titleExtra = lessonName
        val messageExtra = message

        val intent = Intent(context, ReminderBroadcast::class.java)
        intent.putExtra("TitleExtra",titleExtra)
        intent.putExtra("MessageExtra", messageExtra)
        intent.putExtra("notificationID", notificationID)


        val pendingIntent = PendingIntent.getBroadcast(context, notificationID,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendarStartTime.timeInMillis,1000*60*60*24*7, pendingIntent)
    }

    fun cancelNotification(notificationID: Int, lessonName: String, message: String){
        // TODO: gerekli yerlerde burayi cagir
        val intent = Intent(context, ReminderBroadcast::class.java)
//        intent.putExtra("TitleExtra",titleExtra)
//        intent.putExtra("MessageExtra", messageExtra)
//        intent.putExtra("notificationID", notificationID)


        val pendingIntent = PendingIntent.getBroadcast(context, notificationID,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }


    fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Ders Hatirlatma"
            val descriptionText = "Cahnel for time table reminder test"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("HaftalikDersProgrami", name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }


            val notificationManager:NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }



}