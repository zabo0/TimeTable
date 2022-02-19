package com.saboon.timetable.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.saboon.timetable.R
import com.saboon.timetable.databinding.FragmentMainBinding


class MainFragment : Fragment() {


    private var _binding: FragmentMainBinding?=null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)

        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fragmentMainImageViewAdd.setOnClickListener {
            val actionToAddLesson = MainFragmentDirections.actionMainFragmentToDetailsFragment()
            it.findNavController().navigate(actionToAddLesson)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}