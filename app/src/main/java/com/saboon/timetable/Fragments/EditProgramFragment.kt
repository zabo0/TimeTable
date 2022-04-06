package com.saboon.timetable.Fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saboon.timetable.R
import com.saboon.timetable.databinding.FragmentEditProgramBinding


class EditProgramFragment : Fragment() {


    private var _binding: FragmentEditProgramBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_edit_program, container, false)

        _binding = FragmentEditProgramBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fragmentEditProgImageViewEdit.setOnClickListener {
        }


        binding.fragmentEditProgImageViewSave.setOnClickListener {
        }


    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null

    }


}