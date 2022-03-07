package com.saboon.timetable.Fragments

import android.os.Bundle
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


    private lateinit var belowProgramID : String
    private lateinit var belowLessonID: String



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
                AddTimeFragmentArgs.fromBundle(it).belowLessonID?.let {
                    belowLessonID = it
                }
                AddTimeFragmentArgs.fromBundle(it).belowProgramID?.let {
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
            val actionToBack = AddTimeFragmentDirections.actionAddProgramFragmentToDetailsFragment(null, belowProgramID)
            it.findNavController().navigate(actionToBack)
        }


        binding.fragmentAddProgButtonSave.setOnClickListener{
            val id = IDGenerator().generateTimeID()
            val day = binding.autoCompleteTextView.text.toString()
            val classRoom = binding.fragmentDetailsEditTextClassroom.text.toString()
            val timeStart = binding.editTextStartTimePicker.text.toString()
            val timeFinish = binding.editTextFinishTimePicker.text.toString()
            val type = binding.autoCompleteTextViewTypeLesson.text.toString()
            val reminder = binding.autoCompleteTextViewReminderPicker.text.toString()

            val lessonTimeProg = ModelTime(id,day,timeStart,timeFinish,type,classRoom,reminder,belowLessonID, belowProgramID)

            viewModel.storeTimeInDatabase(lessonTimeProg)

            val actionToBack = AddTimeFragmentDirections.actionAddProgramFragmentToDetailsFragment(belowLessonID, belowProgramID)
            it.findNavController().navigate(actionToBack)

        }

        observeData()



    }



    fun observeData(){

        viewModel.whichDay.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.autoCompleteTextView.setText(arrayAdapterDays.getPosition(it))
            }
        })
        viewModel.classRoom.observe(viewLifecycleOwner, Observer{
            it?.let {
                binding.fragmentDetailsEditTextClassroom.setText(it)
            }
        })
        viewModel.startTime.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.editTextStartTimePicker.setText(it)
            }
        })
        viewModel.finishTime.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.editTextFinishTimePicker.setText(it)
            }
        })
        viewModel.typeLesson.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.autoCompleteTextViewTypeLesson.setText(arrayAdapterTypeOfLesson.getPosition(it))
            }
        })
        viewModel.reminder.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.autoCompleteTextViewReminderPicker.setText(arrayAdapterReminder.getPosition(it))
            }
        })



    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}