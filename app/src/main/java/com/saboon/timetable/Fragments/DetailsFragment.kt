package com.saboon.timetable.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.saboon.timetable.R
import com.saboon.timetable.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding?=null
    private val binding get()=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_details, container, false)

        _binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fragmentDetailsTextViewAddProgram.setOnClickListener {
            val actionToAddProgram = DetailsFragmentDirections.actionDetailsFragmentToAddProgramFragment()
            it.findNavController().navigate(actionToAddProgram)
        }

        binding.fragmentDetailsTextViewLessonDetails.setOnClickListener {
            val actionToBack = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
            it.findNavController().navigate(actionToBack)
        }
    }


}