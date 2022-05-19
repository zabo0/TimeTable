package com.saboon.timetable.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saboon.timetable.R
import com.saboon.timetable.databinding.FragmentDetailsBinding
import com.saboon.timetable.databinding.FragmentLessonsBinding


class LessonsFragment : Fragment() {

    private var _binding : FragmentLessonsBinding ?= null
    private val binding get() =_binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_lessons, container, false)
        _binding = FragmentLessonsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}