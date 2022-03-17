package com.saboon.timetable.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.saboon.timetable.Models.ModelLesson
import com.saboon.timetable.Models.ModelTime
import com.saboon.timetable.R
import com.saboon.timetable.Utils.IDGenerator
import com.saboon.timetable.ViewModels.AddTimeViewModel
import com.saboon.timetable.databinding.FragmentAddTimeBinding


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



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
                //val timeText = SimpleDateFormat("hh:mm").format("${hour}:${min}")
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
                val id = IDGenerator().generateTimeID()
                //burada "day" databaseden cekerken gunleri oncelik sirasina gore cekebilmesi icin gunlerin karsilik geldigi sayiyi database e kaydediyor
                //eger duz "pazartesi" olarak kaydediliyor olsaydi "carsamba" daha once cekiliyor olacakti(alfabede daha onde)
                //ancak "pazartesi" string klasorundeki array da indexi "0" carsamba ise "2" boylece "pazartesi"nin onceligi daha fazla oluyor
                val day = requireActivity().resources.getStringArray(R.array.Days).indexOf(binding.autoCompleteTextView.text.toString()).toString()
                val classRoom = binding.fragmentAddProgEditTextClassroom.text.toString()
                val timeStart = binding.editTextStartTimePicker.text.toString()
                val timeFinish = binding.editTextFinishTimePicker.text.toString()
                val type = binding.autoCompleteTextViewTypeLesson.text.toString()
                val reminder = binding.autoCompleteTextViewReminderPicker.text.toString()

                val lessonTimeProg = ModelTime(id,day,timeStart,timeFinish,type,classRoom,reminder,belowLessonID, belowProgramID)

                viewModel.storeTimeInDatabase(lessonTimeProg)

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
                                it.id,newDay,
                                newStartTime,
                                newFinishTime,
                                newTypeOfLesson,
                                newClasroom,
                                newFinishTime,
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

}