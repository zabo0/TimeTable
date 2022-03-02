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
import com.saboon.timetable.R
import com.saboon.timetable.ViewModels.AddProgViewModel
import com.saboon.timetable.databinding.FragmentAddProgramBinding
import java.text.SimpleDateFormat

// TODO: veri tabanina kaydetme islemleri


class AddProgramFragment : Fragment() {

    private var _binding: FragmentAddProgramBinding?=null
    private val binding get() = _binding!!

    lateinit var viewModel :AddProgViewModel

    val DayItems = resources.getStringArray(R.array.Days)
    val arrayAdapterDays = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, DayItems)

    val RemindersItem = resources.getStringArray(R.array.reminder)
    val arrayAdapterReminder = ArrayAdapter(requireContext(),R.layout.dropdown_list_item,RemindersItem)

    val TypeOfLessonItem = resources.getStringArray(R.array.typeOfLesson)
    val arrayAdapterTypeOfLesson = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, TypeOfLessonItem)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddProgramBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.autoCompleteTextView.setAdapter(arrayAdapterDays)


        binding.autoCompleteTextViewReminderPicker.setAdapter(arrayAdapterReminder)


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
                //val timeText = String.format("%02d:%02d", hour, min)
                val timeText = SimpleDateFormat("hh:mm").format("${hour}:${min}")
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

        binding.fragmentAddProgramTextViewAddProgram.setOnClickListener{
            val actionToBack = AddProgramFragmentDirections.actionAddProgramFragmentToDetailsFragment(null)
            it.findNavController().navigate(actionToBack)
        }




        // TODO: kaydederken bu sekilde yap
        binding.fragmentAddProgButtonSave.setOnClickListener{
            val day = binding.autoCompleteTextView.text.toString()
            resources.getStringArray(R.array.Days).indexOf(day)
        }




        viewModel = ViewModelProvider(this).get(AddProgViewModel::class.java)
        viewModel.refreshData()

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