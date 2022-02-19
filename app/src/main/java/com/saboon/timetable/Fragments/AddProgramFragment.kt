package com.saboon.timetable.Fragments

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.saboon.timetable.R
import com.saboon.timetable.databinding.FragmentAddProgramBinding


class AddProgramFragment : Fragment() {

    private var _binding: FragmentAddProgramBinding?=null
    private val binding get() = _binding!!




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



        val DayItems = resources.getStringArray(R.array.Days)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, DayItems)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)



        binding.editTextStartTimePicker.setOnClickListener {
            var time = "00:00"
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
                time = "$hour:$min"
                binding.fragmentAddProgEditTextStartTimePicker.editText?.setText(time)
            }

        }

        binding.editTextFinishTimePicker.setOnClickListener {
            var time = "00:00"
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
                time = "$hour:$min"
                binding.fragmentAddProgEditTextFinishTimePicker.editText?.setText(time)
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}